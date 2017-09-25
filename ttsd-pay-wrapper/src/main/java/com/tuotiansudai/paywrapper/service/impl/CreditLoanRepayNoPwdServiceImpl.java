package com.tuotiansudai.paywrapper.service.impl;


import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayNoPwdMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.CreditLoanRepayNoPwdService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class CreditLoanRepayNoPwdServiceImpl implements CreditLoanRepayNoPwdService {
    private static Logger logger = Logger.getLogger(CreditLoanRepayNoPwdServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_REPAYING_REDIS_KEY = "credit:loan:repaying:{0}";

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "${credit.loan.id}")
    private String creditLoanId;

    @Override
    public BaseDto<PayDataDto> creditLoanRepayNoPwd(long orderId, String mobile, long amount) {
        logger.info(MessageFormat.format("[credit loan nopwd repay {0}] loan out starting, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)));

        PayDataDto payDataDto = new PayDataDto(false, "", String.valueOf(HttpStatus.OK));
        BaseDto<PayDataDto> dto = new BaseDto<>(payDataDto);

        if (amount <= 0) {
            payDataDto.setMessage("还款金额必须大于零");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if (accountModel == null) {
            payDataDto.setMessage("用户未开通支付账户");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (!accountModel.isNoPasswordInvest()) {
            payDataDto.setMessage("用户未开通免密支付功能");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }

        if (this.isRepaying(orderId)){
            payDataDto.setMessage("还款交易进行中, 请30分钟后查看");
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            return dto;
        }


        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.creditLoanNoPwdRepayRequest(
                creditLoanId,
                MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(orderId), String.valueOf(new Date().getTime())),
                accountModel.getPayUserId(),
                String.valueOf(amount));

        try {
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(CreditLoanRepayNoPwdMapper.class, requestModel, ProjectTransferNopwdResponseModel.class);
            redisWrapperClient.setex(MessageFormat.format(CREDIT_LOAN_REPAYING_REDIS_KEY, String.valueOf(orderId)), 30 * 60, SyncRequestStatus.SENT.name());
            if (responseModel.isSuccess()) {
                logger.info(MessageFormat.format("[credit loan repay]{0} nopwd is success, mobile:{1}, amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)));
                payDataDto.setMessage("还款成功");
            } else {
                logger.info(MessageFormat.format("[credit loan repay]{0} nopwd is fail, mobile:{1}, amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)));
                payDataDto.setMessage(MessageFormat.format("还款失败，{0}", responseModel.getRetMsg()));
                this.sendFatalNotify(MessageFormat.format("慧租信用贷无密还款失败，orderId:{0}, mobile:{1}, amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[慧租信用贷无密还款]orderid:{0} error, mobile:{1} amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)), e);
            this.sendFatalNotify(MessageFormat.format("慧租信用贷无密还款异常，orderId:{0}, mobile:{1}, amount:{2}", String.valueOf(orderId), mobile, String.valueOf(amount)));
            payDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
            payDataDto.setMessage("还款异常");
        }
        return dto;
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private boolean isRepaying(long orderId) {
        return redisWrapperClient.exists(MessageFormat.format(CREDIT_LOAN_REPAYING_REDIS_KEY, String.valueOf(orderId)));
    }
}
