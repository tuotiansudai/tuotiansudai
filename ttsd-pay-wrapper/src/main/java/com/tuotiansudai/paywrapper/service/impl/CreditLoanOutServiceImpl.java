package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.impl.LoanServiceImpl;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.CreditLoanBillService;
import com.tuotiansudai.paywrapper.service.CreditLoanOutService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

public class CreditLoanOutServiceImpl implements CreditLoanOutService {

    private final static Logger logger = Logger.getLogger(CreditLoanOutServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT = "CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT";

    private final static String CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT = "CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT";

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;
    @Autowired
    private CreditLoanRechargeMapper creditLoanRechargeMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private AmountTransfer amountTransfer;
    @Autowired
    private CreditLoanBillService creditLoanBillService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void creditLoanOut(){
        long sumAmountByIncome = creditLoanBillMapper.findSumAmountByIncome(CreditLoanBillBusinessType.XYD_USER_REPAY, SystemBillOperationType.IN);
        if (sumAmountByIncome <= 0){
            return;
        }

        long investAmountTotal = 0;
        if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, null)){
            investAmountTotal = sumAmountByIncome - Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT, null));
        }
        logger.info("[标的放款]：发起联动优势放款请求，标的ID:" + null + "，代理人:" + null + "，放款金额:" + investAmountTotal);

        AccountModel accountModel = accountMapper.findByLoginName(userMapper.findByMobile(null).getLoginName());

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanOutRequest(
                null, String.valueOf(IdGenerator.generate()),
                accountModel.getPayUserId(),
                String.valueOf(investAmountTotal));

        try {
            ProjectTransferResponseModel resp = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
            redisWrapperClient.hset(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,null, String.valueOf(investAmountTotal));
        }catch (PayException e){
            logger.error(MessageFormat.format("[信用贷标的放款]:发起放款联动优势请求失败,标的ID : {0}", String.valueOf(null)), e);
        }

    }

    @Override
    public String creditLoanOutCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, ProjectTransferNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postCreditLoanOutCallback(callbackRequest);
        return callbackRequest.getResponseData();
    }

    @Transactional
    private void postCreditLoanOutCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId= Long.parseLong(callbackRequestModel.getOrderId());
            UserModel userModel = userMapper.findByMobile(null);
            long amount = 0;
            if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,null)){
                amount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,null));
            }else{
                logger.error("credit loan out not exists amount ");
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                try {
                    amountTransfer.transferInBalance(userModel.getLoginName(), orderId, amount, UserBillBusinessType.CREDIT_LOAN_OUT, null, null);
                    creditLoanBillService.transferOut(orderId, amount, CreditLoanBillBusinessType.CREDIT_LOAN_OUT,
                            MessageFormat.format("信用贷标的账户放款", userModel.getLoginName(), amount), userModel.getLoginName());

                    if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, null)){
                        long repaySumAmount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, null));
                        redisWrapperClient.hset(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, null, String.valueOf(amount+repaySumAmount));
                    }
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("credit loan recharge transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }

            } else {
                logger.error(MessageFormat.format("credit loan out callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("credit loan out callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
