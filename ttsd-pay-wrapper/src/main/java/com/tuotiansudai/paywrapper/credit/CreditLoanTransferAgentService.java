package com.tuotiansudai.paywrapper.credit;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CreditLoanTransferAgentNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanBillMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CreditLoanBillBusinessType;
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
public class CreditLoanTransferAgentService {

    private final static Logger logger = Logger.getLogger(CreditLoanTransferAgentService.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String CREDIT_LOAN_TRANSFER_AGENT_IN_BALANCE = "CREDIT_LOAN_TRANSFER_AGENT_IN_BALANCE:{0}";

    private final static String CREDIT_LOAN_TRANSFER = "CREDIT_LOAN_TRANSFER";

    private final static String CREDIT_LOAN_TRANSFER_AGENT_KEY = "CREDIT_LOAN_TRANSFER_AGENT_KEY";

    @Autowired
    private CreditLoanBillMapper creditLoanBillMapper;
    @Autowired
    private PaySyncClient paySyncClient;
    @Autowired
    private PayAsyncClient payAsyncClient;
    @Autowired
    private MQWrapperClient mqWrapperClient;
    @Autowired
    private CreditLoanBillService creditLoanBillService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "${credit.loan.agent}")
    private String creditLoanAgent;

    @Transactional
    public void creditLoanTransferAgent() {

        long transferAmount = this.transferAmount();
        if (transferAmount <= 0) {
            return;
        }
        AccountModel accountModel = accountMapper.findByMobile(creditLoanAgent);
        if (accountModel == null) {
            return;
        }

        logger.info("[信用贷还款转入代理人]：发起联动优势转账请求，代理人:" + creditLoanAgent + "，转入金额:" + transferAmount);

        String orderId = String.valueOf(IdGenerator.generate());

        if (!this.checkStatus(orderId)) {
            return;
        }

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCreditLoanTransferAgentRequest(
                orderId,
                accountModel.getPayUserId(),
                String.valueOf(transferAmount));
        try {
            redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_KEY, orderId, SyncRequestStatus.SENT.name());
            ProjectTransferResponseModel resp = paySyncClient.send(CreditLoanTransferAgentMapper.class, requestModel, ProjectTransferResponseModel.class);
            boolean isSuccess = resp.isSuccess();
            redisWrapperClient.hset(CREDIT_LOAN_TRANSFER_AGENT_KEY, orderId, isSuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());

            if (isSuccess) {
                logger.info(MessageFormat.format("[credit loan transfer {0}] success({1}), mobile({2}) amount({3})",
                        String.valueOf(orderId), resp.getRetMsg(), creditLoanAgent, String.valueOf(transferAmount)));
                return;
            } else {
                logger.info(MessageFormat.format("[credit loan transfer {0}] fail({1}), mobile({2}) amount({3})",
                        String.valueOf(orderId), resp.getRetMsg(), creditLoanAgent, String.valueOf(transferAmount)));
                this.sendFatalNotify(MessageFormat.format("慧租信用贷转账给代理人失败({0})，订单号({1}), 代理人({2}), 金额({3})",
                        resp.getRetMsg(), String.valueOf(orderId), creditLoanAgent, String.valueOf(transferAmount)));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[信用贷还款转入代理人]:发起放款联动优势请求失败:{0}", e.getLocalizedMessage()));
            this.sendFatalNotify(MessageFormat.format("慧租信用贷转账给代理人异常，订单号({0}), 代理人({1}), 金额({2})",
                    String.valueOf(orderId), creditLoanAgent, String.valueOf(transferAmount)));
        }

    }

    @Transactional
    public String creditLoanTransferAgentCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, CreditLoanTransferAgentNotifyMapper.class, ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postCreditLoanTransferAgentCallback(callbackRequest);
        return callbackRequest.getResponseData();
    }

    private void postCreditLoanTransferAgentCallback(BaseCallbackRequestModel callbackRequestModel) {
        long orderId = Long.parseLong(callbackRequestModel.getOrderId());
        AccountModel accountModel = accountMapper.findByMobile(creditLoanAgent);
        long transferAmount = this.transferAmount();
        if (transferAmount <= 0) {
            return;
        }
        if (callbackRequestModel.isSuccess()) {
            String redisKey = MessageFormat.format(CREDIT_LOAN_TRANSFER_AGENT_IN_BALANCE, String.valueOf(orderId));
            try {
                String statusString = redisWrapperClient.hget(redisKey, CREDIT_LOAN_TRANSFER);
                if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {
                    AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, accountModel.getLoginName(), orderId, transferAmount, UserBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT, null, null);
                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                    creditLoanBillService.transferOut(orderId, transferAmount, CreditLoanBillBusinessType.CREDIT_LOAN_TRANSFER_AGENT, accountModel.getLoginName());
                    redisWrapperClient.hset(redisKey, CREDIT_LOAN_TRANSFER, SyncRequestStatus.SUCCESS.name());
                }
            } catch (Exception e) {
                redisWrapperClient.hset(redisKey, CREDIT_LOAN_TRANSFER, SyncRequestStatus.FAILURE.name());
                logger.error(MessageFormat.format("credit loan transfer agent out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
            }
        } else {
            logger.error(MessageFormat.format("credit loan transfer agent callback  is fail (orderId = {0})", callbackRequestModel.getOrderId()));
            this.sendFatalNotify(MessageFormat.format("慧租信用贷转账给代理人回调失败，订单号({0}), 代理人({1}), 金额({2})",
                    String.valueOf(orderId), creditLoanAgent, String.valueOf(transferAmount)));
        }
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private long transferAmount() {
        long inSumAmountTotal = creditLoanBillMapper.findSumAmountByIn();
        long outSumAmountTotal = creditLoanBillMapper.findSumAmountByOut();
        return inSumAmountTotal - outSumAmountTotal - 10000000l;
    }

    private boolean checkStatus(String orderId) {
        try {
            String status = redisWrapperClient.hget(CREDIT_LOAN_TRANSFER_AGENT_KEY, orderId);
            if (Strings.isNullOrEmpty(status) || SyncRequestStatus.valueOf(status) == SyncRequestStatus.FAILURE) {
                return true;
            }
            logger.error(MessageFormat.format("[credit loan transfer agent {0}] status is {1}, do not try again", orderId, status));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[credit loan transfer agent {0}] status check error", orderId), e);
        }
        return false;
    }
}
