package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.dto.sms.SmsPayrollNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.PayrollTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.PayrollService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.PayrollDetailMapper;
import com.tuotiansudai.repository.mapper.PayrollMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PayrollServiceImpl implements PayrollService {

    static Logger logger = Logger.getLogger(UMPayRealTimeStatusServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    /**
     * 对于某一批次：
     * 1. 只有 [审核过的(AUDITED),发放失败(FAIL)] 的时候才可以发钱
     * 2. 当开始发钱时，将状态变更为 支付中(PAYING)
     * 3. 开始给此批次中的所有状态不为 发放成功(SUCCESS) 的人依次发钱
     * 4. 当所有人都发放成功，则将此批次记为成功, 否则记为失败
     * <p>
     * <p>
     * 对于某一批次中的某一个人：
     * 1. 若当前状态是 待支付(WAITING)或支付失败(FAIL), 则发钱
     * 2. 若当前状态是 支付中(PAYING), 则直接视为失败
     * 3. 调用联动优势前先将状态置为 支付中(PAYING)
     * 4. 调用联动优势接口给用户转账
     * 4.1. 根据联动优势的同步结果，修改状态
     * 4.2. 若出现异常，则短信报警
     * 5. 同时接收联动优势的后台回调，并更新此记录状态，和此批次的状态
     */
    @Override
    public void pay(long payrollId) {
        PayrollModel payroll = payrollMapper.findById(payrollId);
        if (payroll.getStatus() == PayrollStatusType.AUDITED || payroll.getStatus() == PayrollStatusType.FAIL) {
            payrollMapper.updateStatus(payrollId, PayrollStatusType.PAYING);
            List<PayrollDetailModel> payrollList = payrollDetailMapper.findByPayrollId(payrollId);
            payrollList.stream()
                    .filter(m -> m.getStatus() != PayrollPayStatus.SUCCESS)
                    .filter(this::payOnePerson)
                    .forEach(m -> this.sendUserMessage(payroll, m));
            refreshPayrollPayStatus(payrollId);
        } else if (payroll.getStatus() == PayrollStatusType.PAYING) {
            logger.info("cancel to payoff, because the status is PAYING, id: " + payrollId);
        } else {
            logger.error("execute payoff failed, status is not AUDITED, id: " + payrollId);
        }
    }

    private boolean payOnePerson(PayrollDetailModel payrollDetailModel) {
        // 对于待确认的记录，暂不处理，而是直接返回失败
        if (payrollDetailModel.getStatus() == PayrollPayStatus.PAYING) {
            return false;
        }

        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String orderId = String.format("%dX%dX%s", payrollDetailModel.getPayrollId(), payrollDetailModel.getId(), timestamp);
        AccountModel accountModel = accountMapper.findByLoginName(payrollDetailModel.getLoginName());
        if (accountModel == null) {
            payrollDetailMapper.updateStatus(payrollDetailModel.getId(), PayrollPayStatus.FAIL);
            logger.info(String.format("[Payroll %d - itemId : %d] paid failed, account not exists", payrollDetailModel.getPayrollId(), payrollDetailModel.getId()));
            return false;
        }

        TransferRequestModel requestModel = TransferRequestModel.newPayrollRequest(
                orderId,
                accountModel.getPayUserId(),
                accountModel.getPayAccountId(),
                String.valueOf(payrollDetailModel.getAmount()));

        boolean success = false;
        try {
            payrollDetailMapper.updateStatus(payrollDetailModel.getId(), PayrollPayStatus.PAYING);
            TransferResponseModel responseModel = paySyncClient.send(PayrollTransferMapper.class, requestModel, TransferResponseModel.class);
            success = responseModel.isSuccess();
            if (success) {
                paySuccess(payrollDetailModel);
            } else {
                payFail(payrollDetailModel, responseModel.getRetCode(), responseModel.getRetMsg());
            }

        } catch (Exception e) {
            logger.error(String.format("[Payroll %d - itemId : %d] paid failed, orderId: %s", payrollDetailModel.getPayrollId(),
                    payrollDetailModel.getId(), orderId), e);
            String fatalMsg = String.format("代发工资转账异常，订单号: %s", orderId);
            logger.fatal(fatalMsg, e);
            sendSmsErrNotify(fatalMsg);
        }
        return success;
    }

    private void updateUserAccount(PayrollDetailModel payrollDetailModel) {
        AmountTransferMessage atm = new AmountTransferMessage(
                TransferType.TRANSFER_IN_BALANCE, payrollDetailModel.getLoginName(),
                payrollDetailModel.getId(), payrollDetailModel.getAmount(),
                UserBillBusinessType.PAYROLL, null, null);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

        SystemBillMessage sbm = new SystemBillMessage(
                SystemBillMessageType.TRANSFER_OUT, payrollDetailModel.getId(),
                payrollDetailModel.getAmount(), SystemBillBusinessType.PAYROLL,
                SystemBillDetailTemplate.PAYROLL_DETAIL_TEMPLATE.buildDetail(payrollDetailModel.getMobile()));
        mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);
    }

    @Override
    public String payNotify(Map<String, String> paramsMap, String queryString) {
        logger.info("[Payroll Notify] pay notify callback begin.");
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null || Strings.isNullOrEmpty(callbackRequest.getOrderId()) || !callbackRequest.getOrderId().contains("X")) {
            logger.error(MessageFormat.format("[Payroll Notify] pay notify callback parse failed (queryString = {0})", queryString));
            return null;
        }
        String[] orderIdItems = callbackRequest.getOrderId().split("X");
        long payrollDetailId = Long.parseLong(orderIdItems[1]);
        PayrollDetailModel detailModel = payrollDetailMapper.findById(payrollDetailId);

        if (detailModel.getStatus() != PayrollPayStatus.SUCCESS) {
            if (callbackRequest.isSuccess()) {
                paySuccess(detailModel);
                refreshPayrollPayStatus(detailModel.getPayrollId());
                PayrollModel payroll = payrollMapper.findById(detailModel.getPayrollId());
                sendUserMessage(payroll, detailModel);
            } else {
                payFail(detailModel, callbackRequest.getRetCode(), callbackRequest.getRetMsg());
            }
        }
        return callbackRequest.getResponseData();
    }

    private void paySuccess(PayrollDetailModel payrollDetailModel) {
        payrollDetailMapper.updateStatus(payrollDetailModel.getId(), PayrollPayStatus.SUCCESS);
        updateUserAccount(payrollDetailModel);
        logger.info(String.format("[Payroll %d - itemId : %d] paid success", payrollDetailModel.getPayrollId(), payrollDetailModel.getId()));
    }

    private void payFail(PayrollDetailModel payrollDetailModel, String retCode, String retMsg) {
        payrollDetailMapper.updateStatus(payrollDetailModel.getId(), PayrollPayStatus.FAIL);
        logger.error(String.format("[Payroll %d - itemId : %d] paid failed, code:%s, message: %s", payrollDetailModel.getPayrollId(),
                payrollDetailModel.getId(), retCode, retMsg));
    }

    private void refreshPayrollPayStatus(long payrollId) {
        List<PayrollDetailModel> payrollDetailModels = payrollDetailMapper.findByPayrollId(payrollId);
        boolean allSuccess = payrollDetailModels.stream().allMatch(p -> p.getStatus() == PayrollPayStatus.SUCCESS);
        if (allSuccess) {
            payrollMapper.updateStatus(payrollId, PayrollStatusType.SUCCESS);
        } else {
            payrollMapper.updateStatus(payrollId, PayrollStatusType.FAIL);
        }
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("sent payroll fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(String.format("%s, %s", environment, errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }

    private void sendUserMessage(PayrollModel payrollModel, PayrollDetailModel payrollDetailModel) {
        String title = MessageFormat.format(MessageEventType.PAYROLL_HAS_BEEN_TRANSFERRED.getTitleTemplate(), payrollModel.getTitle());
        String message = MessageFormat.format(MessageEventType.PAYROLL_HAS_BEEN_TRANSFERRED.getContentTemplate(), payrollModel.getTitle());
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.PAYROLL_HAS_BEEN_TRANSFERRED,
                Collections.singletonList(payrollDetailModel.getLoginName()),
                title, message, null));

        smsWrapperClient.sendPayrollNotify(new SmsPayrollNotifyDto(Lists.newArrayList(payrollDetailModel.getMobile()), payrollModel.getTitle()));

    }

}
