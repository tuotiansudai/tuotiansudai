package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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
    private JobManager jobManager;

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
     * 3. 发放时若出现异常，则将状态设置为 支付中(PAYING), 本次发放视为失败
     * 3.1. 并添加定时任务：30分钟后若仍为 支付中，则将状态改为发放失败(FAIL)
     * 3.2. 若收到异步回调，则根据异步回调的结果修改支付状态，并更新此批次的状态
     * 4. 发放时若无异常，则根据联动优势的结果，修改状态
     */
    @Override
    public void pay(long payrollId) {
        PayrollModel payroll = payrollMapper.findById(payrollId);
        if (payroll.getStatus() == PayrollStatusType.AUDITED || payroll.getStatus() == PayrollStatusType.FAIL) {
            payrollMapper.updateStatus(payrollId, PayrollStatusType.PAYING);
            List<PayrollDetailModel> payrollList = payrollDetailMapper.findByPayrollId(payrollId);
            payrollList.stream()
                    .filter(m -> m.getStatus() != PayrollPayStatus.SUCCESS)
                    .forEach(this::payOnePerson);
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

        TransferRequestModel requestModel = TransferRequestModel.newPayrollRequest(
                orderId,
                accountModel.getPayUserId(),
                accountModel.getPayAccountId(),
                String.valueOf(payrollDetailModel.getAmount()));

        boolean success = false;
        try {
            TransferResponseModel responseModel = paySyncClient.send(PayrollTransferMapper.class, requestModel, TransferResponseModel.class);
            success = responseModel.isSuccess();
            if (success) {
                logger.info(String.format("[Payroll %d - itemId : %d] paid success", payrollDetailModel.getPayrollId(), payrollDetailModel.getId()));
            } else {
                logger.error(String.format("[Payroll %d - itemId : %d] paid failed, code:%s, message: %s", payrollDetailModel.getPayrollId(),
                        payrollDetailModel.getId(), responseModel.getRetCode(), responseModel.getRetMsg()));
            }
            payrollDetailMapper.updateStatus(payrollDetailModel.getId(), success ? PayrollPayStatus.SUCCESS : PayrollPayStatus.FAIL);
        } catch (Exception e) {
            logger.error(String.format("[Payroll %d - itemId : %d] paid failed", payrollDetailModel.getPayrollId(), payrollDetailModel.getId()), e);
            // 异常时，将该笔工资状态记为正在处理
            payrollDetailMapper.updateStatus(payrollDetailModel.getId(), PayrollPayStatus.PAYING);
            // 并且在30分钟后再将状态置为失败
            DelayMessageDeliveryJobCreator.createConfirmPayrollFailedDelayJob(jobManager, payrollDetailModel.getId());
        }
        return success;
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
            payrollDetailMapper.updateStatus(payrollDetailId, callbackRequest.isSuccess() ? PayrollPayStatus.SUCCESS : PayrollPayStatus.FAIL);
            logger.info("[Payroll Notify] payroll status update success, payrollDetailId: " + payrollDetailId + ", success: " + callbackRequest.isSuccess());
            refreshPayrollPayStatus(detailModel.getPayrollId());
        }
        return callbackRequest.getResponseData();
    }

    private void refreshPayrollPayStatus(long payrollId) {
        List<PayrollDetailModel> payrollDetailModels = payrollDetailMapper.findByPayrollId(payrollId);
        boolean allSuccess = payrollDetailModels.stream().allMatch(p -> p.getStatus() == PayrollPayStatus.SUCCESS);
        if (allSuccess) {
            payrollMapper.updateStatus(payrollId, PayrollStatusType.SUCCESS);
        }
    }
}
