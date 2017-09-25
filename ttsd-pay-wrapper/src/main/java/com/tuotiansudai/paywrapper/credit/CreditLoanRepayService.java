package com.tuotiansudai.paywrapper.credit;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanRepayProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
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

    private final static String CREDIT_LOAN_REPAYING_REDIS_KEY = "credit:loan:repaying:{0}";

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final UserMapper userMapper;

    private final AccountMapper accountMapper;

    private final PayAsyncClient payAsyncClient;

    @Autowired
    public CreditLoanRepayService(UserMapper userMapper, AccountMapper accountMapper, PayAsyncClient payAsyncClient) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
        this.payAsyncClient = payAsyncClient;
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
            redisWrapperClient.setex(MessageFormat.format(CREDIT_LOAN_REPAYING_REDIS_KEY, String.valueOf(orderId)), 30 * 60, SyncRequestStatus.SENT.name());
            return payFormDataDtoBaseDto;
        } catch (PayException e) {
            logger.error(MessageFormat.format("[credit loan repay {0}] generate form error, mobile({1}) amount({2})", String.valueOf(orderId), mobile, String.valueOf(amount)), e);
            payFormDataDto.setMessage("生成交易数据失败");
            payFormDataDto.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
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
        return redisWrapperClient.exists(MessageFormat.format(CREDIT_LOAN_REPAYING_REDIS_KEY, String.valueOf(orderId)));

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
}
