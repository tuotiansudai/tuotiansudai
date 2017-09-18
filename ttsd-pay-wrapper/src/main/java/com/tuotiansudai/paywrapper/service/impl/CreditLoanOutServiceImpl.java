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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
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

    private final static String creditLoanId = "78365173103632";

    private final static String agentMobile = "00000000000";

    @Override
    public void creditLoanOut(){
        long investAmountTotal = creditLoanBillMapper.findSumAmountByIncome(CreditLoanBillBusinessType.XYD_USER_REPAY, SystemBillOperationType.IN);
        if (investAmountTotal <= 0){
            return;
        }
        UserModel userModel = userMapper.findByMobile(agentMobile);
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());

        if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName())){
            investAmountTotal = investAmountTotal - Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName()));
        }
        logger.info("[标的放款]：发起联动优势放款请求，标的ID:" + creditLoanId + "，代理人:" + userModel.getLoginName() + "，放款金额:" + investAmountTotal);


        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanOutRequest(
                creditLoanId, String.valueOf(IdGenerator.generate()),
                accountModel.getPayUserId(),
                String.valueOf(investAmountTotal));

        try {
            ProjectTransferResponseModel resp = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
            redisWrapperClient.hset(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,userModel.getLoginName(), String.valueOf(investAmountTotal));
        }catch (PayException e){
            logger.error(MessageFormat.format("[信用贷标的放款]:发起放款联动优势请求失败,标的ID : {0}", creditLoanId, e));
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
            UserModel userModel = userMapper.findByMobile(agentMobile);
            long amount = 0;
            if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,userModel.getLoginName())){
                amount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_AMOUNT,userModel.getLoginName()));
            }else{
                logger.error("credit loan out not exists amount ");
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                try {
                    amountTransfer.transferInBalance(userModel.getLoginName(), orderId, amount, UserBillBusinessType.CREDIT_LOAN_OUT, null, null);
                    creditLoanBillService.transferOut(orderId, amount, CreditLoanBillBusinessType.CREDIT_LOAN_OUT,
                            MessageFormat.format("信用贷标的账户放款", userModel.getLoginName(), amount), userModel.getLoginName());

                    if (redisWrapperClient.hexists(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName())){
                        long repaySumAmount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName()));
                        redisWrapperClient.hset(CREDIT_LOAN_OUT_REPAY_AGENT_SUM_AMOUNT, userModel.getLoginName(), String.valueOf(amount+repaySumAmount));
                    }
                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("credit loan out transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
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
