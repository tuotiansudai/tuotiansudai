package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanBillService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanPwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.CreditLoanTransferAgentService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class CreditLoanTransferAgentServiceImpl implements CreditLoanTransferAgentService {

    private final static Logger logger = Logger.getLogger(CreditLoanTransferAgentServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT = "CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT";

    private final static String CREDIT_LOAN_TRANSFER_AGENT_AMOUNT = "CREDIT_LOAN_TRANSFER_AGENT_AMOUNT";

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;
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

    @Value(value = "${credit.loan.id}")
    private String creditLoanId;

    @Value(value = "${credit.loan.agent}")
    private String agentMobile;

    @Override
    public void creditLoanTransferAgent(){
        long investAmountTotal = creditLoanBillMapper.findSumAmountByIncome(CreditLoanBillBusinessType.CREDIT_LOAN_REPAY, SystemBillOperationType.IN);
        if (investAmountTotal <= 0){
            return;
        }
        UserModel userModel = userMapper.findByMobile(agentMobile);
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());

        if (redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())){
            investAmountTotal = investAmountTotal - Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName()));
        }
        logger.info("[信用贷还款转入代理人]：发起联动优势转账请求，信用贷账户:" + creditLoanId + "，代理人:" + userModel.getLoginName() + "，转入金额:" + investAmountTotal);

        String orderId = String.valueOf(IdGenerator.generate());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanTransferAgentRequest(
                creditLoanId, orderId,
                accountModel.getPayUserId(),
                String.valueOf(investAmountTotal));
        try {
            ProjectTransferResponseModel resp = paySyncClient.send(CreditLoanPwdRechargeMapper.class, requestModel, ProjectTransferResponseModel.class);
            if (resp.isSuccess()) {
                redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT, orderId, String.valueOf(investAmountTotal));
                return;
            }
        }catch (PayException e){
            logger.error(MessageFormat.format("[信用贷还款转入代理人]:发起放款联动优势请求失败,信用贷账户 : {0}", creditLoanId, e));
        }

    }

    @Override
    @Transactional
    public String creditLoanTransferAgentCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, CreditLoanTransferAgentNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postCreditLoanOutCallback(callbackRequest);
        return callbackRequest.getResponseData();
    }

    private void postCreditLoanOutCallback(BaseCallbackRequestModel callbackRequestModel) {
        try {
            long orderId= Long.parseLong(callbackRequestModel.getOrderId());
            UserModel userModel = userMapper.findByMobile(agentMobile);
            long amount = 0;
            if (redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT,callbackRequestModel.getOrderId())){
                amount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT,callbackRequestModel.getOrderId()));
            }else{
                logger.error("credit loan transfer agent not exists amount ");
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                try {
                    amountTransfer.transferInBalance(userModel.getLoginName(), orderId, amount, UserBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT, null, null);
                    creditLoanBillService.transferOut(orderId, amount, CreditLoanBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT,
                            MessageFormat.format("信用贷还款转入代理人:{0},金额:{1}", userModel.getLoginName(), amount), userModel.getLoginName());

                    long transferSumAmount = redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())?
                            Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())) : 0;
                    redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName(), String.valueOf(amount+transferSumAmount));

                    redisWrapperClient.hdel(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT, callbackRequestModel.getOrderId());

                } catch (AmountTransferException e) {
                    logger.error(MessageFormat.format("credit loan transfer agent out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
                }

            } else {
                logger.error(MessageFormat.format("credit loan transfer agent callback  is fail (orderId = {0})", callbackRequestModel.getOrderId()));
            }
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("credit loan transfer agent callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()));
            logger.error(e.getLocalizedMessage(), e);
        }
    }

}
