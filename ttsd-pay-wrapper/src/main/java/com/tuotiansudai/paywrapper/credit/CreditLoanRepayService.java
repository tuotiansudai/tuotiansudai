package com.tuotiansudai.paywrapper.credit;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CreditLoanRepayService {

    private static Logger logger = Logger.getLogger(CreditLoanRepayService.class);

    private final static String CREDIT_LOAN_REPAY_REDIS_KEY = "credit:loan:repay:{0}";

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final PayAsyncClient payAsyncClient;

    private final PaySyncClient paySyncClient;

    private final SmsWrapperClient smsWrapperClient;

    @Autowired
    public CreditLoanRepayService(UserMapper userMapper, AccountMapper accountMapper, PayAsyncClient payAsyncClient, PaySyncClient paySyncClient, SmsWrapperClient smsWrapperClient) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.payAsyncClient = payAsyncClient;
        this.paySyncClient = paySyncClient;
        this.smsWrapperClient = smsWrapperClient;
    }

    @Transactional
    public BaseDto<PayFormDataDto> passwordRepay(long orderId, String mobile, long amount) {
        logger.info(MessageFormat.format("[credit loan repay {0}] repay starting, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)));

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setCode("0000");
        BaseDto<PayFormDataDto> dto = new BaseDto<>(payFormDataDto);

        if (!this.checkAmount(orderId, amount)) {
            payFormDataDto.setMessage("还款金额必须大于零");
            payFormDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        AccountModel account = this.getAccount(orderId, mobile);
        if (account == null) {
            payFormDataDto.setMessage("用户未开通支付账户");
            payFormDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (this.isRepaying(orderId)) {
            payFormDataDto.setMessage("还款交易进行中, 请30分钟后查看");
            payFormDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanRepayRequest(
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(orderId), String.valueOf(new Date().getTime())),
                    account.getPayUserId(),
                    String.valueOf(amount));
            BaseDto<PayFormDataDto> payFormDataDtoBaseDto = payAsyncClient.generateFormData(CreditLoanRepayProjectTransferMapper.class, requestModel);
            redisWrapperClient.setex(MessageFormat.format(CREDIT_LOAN_REPAY_REDIS_KEY, String.valueOf(orderId)), 30 * 60, SyncRequestStatus.SENT.name());
            return payFormDataDtoBaseDto;
        } catch (PayException e) {
            logger.error(MessageFormat.format("[credit loan repay {0}] generate form error, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)), e);
            payFormDataDto.setMessage("生成交易数据失败");
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return dto;
    }

    @Transactional
    public BaseDto<PayDataDto> noPwdRepay(long orderId, String mobile, long amount) {
        logger.info(MessageFormat.format("[credit loan nopwd repay {0}] loan out starting, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)));

        PayDataDto payDataDto = new PayDataDto(false, "", String.valueOf(HttpStatus.OK));
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);

        if (!this.checkAmount(orderId, amount)) {
            payDataDto.setMessage("还款金额必须大于零");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        AccountModel account = this.getAccount(orderId, mobile);
        if (account == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (!account.isNoPasswordInvest()) {
            payDataDto.setMessage("用户未开通免密支付功能");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (!checkRepayStatus(orderId)){
            payDataDto.setMessage("您已还款成功");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newCreditLoanNoPwdRepayRequest(
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(orderId), String.valueOf(new Date().getTime())),
                account.getPayUserId(),
                String.valueOf(amount));

        try {
            redisWrapperClient.set(MessageFormat.format(CREDIT_LOAN_REPAY_REDIS_KEY, String.valueOf(orderId)), SyncRequestStatus.SENT.name());
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    CreditLoanRepayNoPwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(orderId)))
                    .build()));
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            logger.error(MessageFormat.format("[慧租信用贷无密还款]orderid:{0} error, mobile:{1} amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)), e);
            this.sendFatalNotify(MessageFormat.format("慧租信用贷无密还款异常，orderId:{0}, mobile:{1}, amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)));
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
        }
        return dto;
    }

    private boolean checkAmount(long orderId, long amount) {
        if (amount < 1) {
            logger.error(MessageFormat.format("[credit loan repay {0}] amount({1}) less than 0", String.valueOf(orderId), String.valueOf(amount)));
            return false;
        }

        return true;
    }

    private boolean isRepaying(long orderId) {
        return redisWrapperClient.exists(MessageFormat.format(CREDIT_LOAN_REPAY_REDIS_KEY, String.valueOf(orderId)));

    }

    private AccountModel getAccount(long orderId, String mobile) {
        AccountModel accountModel = accountMapper.findByMobile(mobile);
        if (accountModel == null) {
            logger.error(MessageFormat.format("[credit loan repay {0}] account({1}) does not exist ", String.valueOf(orderId), mobile));
            return null;
        }

        userMapper.lockByLoginName(accountModel.getLoginName());
        return accountModel;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private boolean checkRepayStatus(long orderId){
        try {
            String status = redisWrapperClient.get(MessageFormat.format(CREDIT_LOAN_REPAY_REDIS_KEY, String.valueOf(orderId)));
            if (Strings.isNullOrEmpty(status) || SyncRequestStatus.valueOf(status) == SyncRequestStatus.FAILURE) {
                return true;
            }
            logger.error(MessageFormat.format("[credit loan out {0}] status is {1}, do not try again", String.valueOf(orderId), status));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan out {0}] status check error", String.valueOf(orderId)), e);
        }
        return false;
    }
}
