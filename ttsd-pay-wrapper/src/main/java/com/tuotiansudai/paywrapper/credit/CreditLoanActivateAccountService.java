package com.tuotiansudai.paywrapper.credit;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanNopwdActivateAccountMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CreditLoanActivateAccountService {

    private static Logger logger = Logger.getLogger(CreditLoanActivateAccountService.class);

    private final static int ACTIVATE_ACCOUNT_MONEY = 1;

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR = "X";

    private final static String CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY = "credit:loan:activate:account:{0}";

    private final static String CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY = "credit:loan:activate:account:concurrency:{0}";

    private final static String ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE = "{0}" + ACTIVATE_ACCOUNT_ORDER_ID_SEPARATOR + "{1}";

    @Value("${common.environment}")
    private Environment environment;

    private final AccountMapper accountMapper;

    private final PaySyncClient paySyncClient;

    private final PayAsyncClient payAsyncClient;

    private final SmsWrapperClient smsWrapperClient;

    private final MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    public CreditLoanActivateAccountService(AccountMapper accountMapper,
                                            PaySyncClient paySyncClient,
                                            PayAsyncClient payAsyncClient,
                                            SmsWrapperClient smsWrapperClient,
                                            MQWrapperClient mqWrapperClient) {
        this.accountMapper = accountMapper;
        this.paySyncClient = paySyncClient;
        this.payAsyncClient = payAsyncClient;
        this.smsWrapperClient = smsWrapperClient;
        this.mqWrapperClient = mqWrapperClient;
    }

    public BaseDto<PayFormDataDto> passwordActivateAccount(String mobile) {
        logger.info(MessageFormat.format("[credit loan password activate account {0}] starting", String.valueOf(mobile)));

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setCode("0000");
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);

        AccountModel account = this.getAccount(mobile);
        if (account == null) {
            payFormDataDto.setMessage("用户未开通支付账户");
            payFormDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if(this.isActive(mobile)){
            payFormDataDto.setMessage("正在激活账户, 请30分钟后查看");
            payFormDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanActivateAccountRequest(
                    MessageFormat.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, mobile, String.valueOf(new Date().getTime())),
                    account.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));

            BaseDto<PayFormDataDto> payFormDataDtoBaseDto = payAsyncClient.generateFormData(CreditLoanActivateAccountMapper.class, requestModel);
            redisWrapperClient.setex(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY,mobile),30*60,SyncRequestStatus.SENT.name());
            return payFormDataDtoBaseDto;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan password activate account {0}] activate account error, mobile({1})",  mobile), e);
            payFormDataDto.setMessage("发送激活账户数据失败");
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return dto;
    }

    public BaseDto<PayDataDto> noPasswordActivateAccount(String mobile) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        AccountModel account = this.getAccount(mobile);
        if (account == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        if (!account.isNoPasswordInvest()) {
            payDataDto.setMessage("用户未开通免密支付功能");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        if (!checkActivateAccountStatus(mobile)){
            payDataDto.setMessage("您已经激活过账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return baseDto;
        }

        try {

            ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCreditLoanActivateAccountNopwdRequest(
                    MessageFormat.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, mobile, String.valueOf(new Date().getTime())),
                    account.getPayUserId(),
                    String.valueOf(ACTIVATE_ACCOUNT_MONEY));

            redisWrapperClient.set(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, mobile), SyncRequestStatus.SENT.name());

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    CreditLoanNopwdActivateAccountMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);

            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", MessageFormat.format(ACTIVATE_ACCOUNT_ORDER_ID_TEMPLATE, mobile))
                    .build()));

            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            logger.error(MessageFormat.format("[慧租无密激活账户] error, mobile:{0}",  mobile), e);
            this.sendFatalNotify(MessageFormat.format("慧租无密激活账户异常，mobile:{0}", mobile));

            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    private AccountModel getAccount(String mobile) {
        AccountModel accountModel = accountMapper.findByMobile(mobile);
        if (accountModel == null) {
            logger.error(MessageFormat.format("[credit loan no pwd activate account {0}] does not exist ", mobile));
            return null;
        }
        return accountModel;
    }

    private boolean isActive(String mobile) {
        return redisWrapperClient.exists(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_CONCURRENCY_REDIS_KEY, mobile));
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private boolean checkActivateAccountStatus(String mobile){
        try {
            String status = redisWrapperClient.get(MessageFormat.format(CREDIT_LOAN_ACTIVATE_ACCOUNT_REDIS_KEY, mobile));
            if (Strings.isNullOrEmpty(status) || SyncRequestStatus.valueOf(status) == SyncRequestStatus.FAILURE) {
                return true;
            }
            logger.error(MessageFormat.format("[credit loan no pwd activate account {0}] status is {1}, do not try again", mobile, status));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan no pwd activate account {0}] status check error", mobile), e);
        }
        return false;
    }

}
