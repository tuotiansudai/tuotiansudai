package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.credit.CreditLoanBillService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanPwdRechargeMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentMapper;
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
import sun.dc.pr.PRError;

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
    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "${credit.loan.id}")
    private String creditLoanId;

    @Value(value = "${credit.loan.agent}")
    private String agentMobile;

    @Override
    @Transactional
    public void creditLoanTransferAgent() {
        long investAmountTotal = creditLoanBillMapper.findSumAmountByInAndRepayType();
        if (investAmountTotal <= 0) {
            return;
        }
        UserModel userModel = userMapper.findByMobile(agentMobile);
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());

        if (redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())) {
            investAmountTotal = investAmountTotal - Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName()));
        }
        logger.info("[信用贷还款转入代理人]：发起联动优势转账请求，信用贷账户:" + creditLoanId + "，代理人:" + userModel.getLoginName() + "，转入金额:" + investAmountTotal);

        String orderId = String.valueOf(IdGenerator.generate());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanTransferAgentRequest(
                creditLoanId, orderId,
                accountModel.getPayUserId(),
                String.valueOf(investAmountTotal));
        try {
            ProjectTransferResponseModel resp = paySyncClient.send(CreditLoanTransferAgentMapper.class, requestModel, ProjectTransferResponseModel.class);
            if (resp.isSuccess()) {
                redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT, orderId, String.valueOf(investAmountTotal));
                return;
            } else {
                logger.info(MessageFormat.format("[credit loan transfer {0}] fail({1}), mobile({2}) amount({3})",
                        String.valueOf(orderId), resp.getRetMsg(), agentMobile, String.valueOf(investAmountTotal)));
                this.sendFatalNotify(MessageFormat.format("慧租信用贷转账给代理人失败({0})，订单号({1}), 代理人({2}), 金额({3})",
                        resp.getRetMsg(),String.valueOf(orderId), agentMobile, String.valueOf(investAmountTotal)));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[信用贷还款转入代理人]:发起放款联动优势请求失败,信用贷账户 : {0}", creditLoanId, e));
            this.sendFatalNotify(MessageFormat.format("慧租信用贷转账给代理人异常，订单号({0}), 代理人({1}), 金额({2})",
                    String.valueOf(orderId), agentMobile, String.valueOf(investAmountTotal)));
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
            long orderId = Long.parseLong(callbackRequestModel.getOrderId());
            UserModel userModel = userMapper.findByMobile(agentMobile);
            long amount = 0;
            if (redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT, callbackRequestModel.getOrderId())) {
                amount = Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_AMOUNT, callbackRequestModel.getOrderId()));
            } else {
                logger.error("credit loan transfer agent not exists amount ");
                return;
            }
            if (callbackRequestModel.isSuccess()) {
                try {
                    amountTransfer.transferInBalance(userModel.getLoginName(), orderId, amount, UserBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT, null, null);
                    creditLoanBillService.transferOut(orderId, amount, CreditLoanBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT, userModel.getLoginName());

                    long transferSumAmount = redisWrapperClient.hexists(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName()) ?
                            Long.parseLong(redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName())) : 0;
                    redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_SUM_AMOUNT, userModel.getLoginName(), String.valueOf(amount + transferSumAmount));

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

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

}
