package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.InvestNotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.paywrapper.service.UserBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserBillService userBillService;

    @Autowired
    InvestNotifyRequestMapper investNotifyRequestMapper;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        // TODO : 这个方法里的事务如何处理
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        InvestModel investModel = new InvestModel(dto);
        investModel.setId(idGenerator.generate());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newInvestRequest(
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(investModel.getAmount()));
        try {
            checkLoanInvestAccountAmount(dto.getLoginName(), investModel.getLoanId(), investModel.getAmount());
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            investMapper.create(investModel);
            return baseDto;
        } catch (PayException e) {
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setStatus(false);
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    private void checkLoanInvestAccountAmount(String loginName, long loanId, long investAmount) throws PayException {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel.getBalance() < investAmount) {
            logger.error("投资失败，投资金额[" + investAmount + "]超过用户[" + loginName + "]账户余额[" + accountModel.getBalance() + "]");
            throw new PayException("账户余额不足");
        }
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            logger.error("投资失败，查找不到指定的标的[" + loanId + "]");
            throw new PayException("标的不存在");
        }
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long remainAmount = loan.getLoanAmount() - successInvestAmount;

        if (remainAmount < investAmount) {
            logger.error("投资失败，投资金额[" + investAmount + "]超过标的[" + loanId + "]可投金额[" + remainAmount + "]");
            throw new PayException("投资金额超过标的可投金额");
        }
    }

    /**
     * 投资回调接口，记录请求入库
     *
     * @param paramsMap
     * @param originalQueryString
     * @return
     */
    @Override
    @Transactional
    public String investCallback(Map<String, String> paramsMap, String originalQueryString) {
        // status标记此条记录是否已经被处理，0:未处理；1:已处理。
        paramsMap.put("status", InvestNotifyProcessStatus.NOT_DONE.toString());
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                InvestNotifyRequestMapper.class,
                InvestNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }
        String respData = callbackRequest.getResponseData();
        return respData;
    }


    public BaseDto<BaseDataDto> asyncInvestCallback(){
        int limitCount = 10;
        List<InvestNotifyRequestModel> todoList = investNotifyRequestMapper.getTodoList(limitCount);

        for(InvestNotifyRequestModel model : todoList) {
            updateInvestNotifyRequestStatus(model);
            processOneCallback(model);
        }

        BaseDto<BaseDataDto> asyncInvestNotifyDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        asyncInvestNotifyDto.setData(baseDataDto);

        return asyncInvestNotifyDto;
    }


    @Transactional
    private void updateInvestNotifyRequestStatus(InvestNotifyRequestModel model) {
        investNotifyRequestMapper.updateStatus(model.getId(), InvestNotifyProcessStatus.DONE);
    }

    @Transactional
    private synchronized void processOneCallback(InvestNotifyRequestModel callbackRequestModel) {

        long orderId = Long.parseLong(callbackRequestModel.getOrderId());
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest(project_transfer) callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        String loginName = investModel.getLoginName();
        if (callbackRequestModel.isSuccess()) {

            long loanId = investModel.getLoanId();
            long successInvestAmountTotal = investMapper.sumSuccessInvestAmount(loanId);

            LoanModel loanModel = loanMapper.findById(loanId);
            if (successInvestAmountTotal + investModel.getAmount() > loanModel.getLoanAmount()) {
                // 超投
                infoLog("超投", orderId, investModel.getAmount(), loginName, loanId);
                // 超投返款处理
                overInvestPaybackProcess(orderId, investModel, loginName, loanId);
            } else {
                // 投资成功
                infoLog("投资成功", orderId, investModel.getAmount(), loginName, loanId);
                // 投资成功，冻结用户资金，更新投资状态为success
                investSuccess(orderId, investModel, loginName);

                if (successInvestAmountTotal + investModel.getAmount() == loanModel.getLoanAmount()) {
                    // 满标，改标的状态 RECHECK
                    loanMapper.updateStatus(loanId, LoanStatus.RECHECK);
                }
            }
        } else {
            // 失败的话：改invest本身状态
            investMapper.updateStatus(investModel.getId(), InvestStatus.FAIL);
        }
    }

    /**
     * 超投处理：返款、解冻、记录userBill、更新投资状态为失败
     *
     * @param orderId
     * @param investModel
     * @param loginName
     * @param loanId
     */
    @Transactional
    private boolean overInvestPaybackProcess(long orderId, InvestModel investModel, String loginName, long loanId) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        boolean paybackSuccess = false;

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.overInvestPaybackRequest(
                String.valueOf(loanId), orderId + "X" + System.currentTimeMillis(), accountModel.getPayUserId(), String.valueOf(investModel.getAmount()));

        try {
            ProjectTransferResponseModel responseModel = paySyncClient.send(
                    ProjectTransferMapper.class,
                    requestModel,
                    ProjectTransferResponseModel.class);

            if (responseModel.isSuccess()) {
                // 超投返款成功
                infoLog("超投返款成功", orderId, investModel.getAmount(), loginName, loanId);
                paybackSuccess = true;
            } else {
                // 联动优势返回返款失败，但是标记此条请求已经处理完成，记录日志，在异步notify中进行投资成功处理
                errorLog("超投返款失败", orderId, investModel.getAmount(), loginName, loanId);
            }
        } catch (PayException e) {
            // 调用umpay时出现异常(可能已经返款成功了)
            fatalLog("超投返款PayException异常", orderId, investModel.getAmount(), loginName, loanId, e);
        } catch (Exception e) {
            // 所有其他异常，包括数据库链接，网络异常，记录日志，抛出异常，事务回滚
            fatalLog("超投返款其他异常", orderId, investModel.getAmount(), loginName, loanId, e);
            throw e;
        }

        if(!paybackSuccess) {
            // 如果返款失败，则记录本次投资为 超投返款失败
            investMapper.updateStatus(investModel.getId(), InvestStatus.OVER_INVEST_PAYBACK_FAIL);
        }
        return paybackSuccess;
    }

    /**
     * umpay 超投返款的回调
     *
     * @param paramsMap
     * @param queryString
     * @return
     */
    public String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString){

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        String orderIdOri = callbackRequest.getOrderId();
        String orderIdStr = orderIdOri == null ? "" : orderIdOri.split("X")[0];
        long orderId = Long.parseLong(orderIdStr);

        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest callback notify order is not exist (orderId = {0})", orderId));
            return null;
        }

        String loginName = investModel.getLoginName();
        if (callbackRequest.isSuccess()) {
            // 返款成功
            // 改 invest 本身状态为超投返款
            investMapper.updateStatus(investModel.getId(), InvestStatus.OVER_INVEST_PAYBACK);
        } else {
            // 返款失败，当作投资成功处理
            errorLog("返款失败，当作投资成功处理", orderId, investModel.getAmount(), loginName, investModel.getLoanId());

            investSuccess(orderId, investModel, loginName);

            long loanId = investModel.getLoanId();
            // 超投，改标的状态为满标 RECHECK
            loanMapper.updateStatus(loanId, LoanStatus.RECHECK);
        }

        String respData = callbackRequest.getResponseData();
        return respData;
    }

    /**
     * 投资成功处理：冻结资金＋更新invest状态
     *
     * @param orderId
     * @param investModel
     * @param loginName
     */
    private void investSuccess(long orderId, InvestModel investModel, String loginName) {
        try {
            // 冻结资金
            userBillService.freeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS);
        } catch (AmountTransferException e) {
            // 记录日志，发短信通知管理员
            fatalLog("投资成功，但资金冻结失败", orderId, investModel.getAmount(), loginName, investModel.getLoanId(), e);
        }
        // 改invest 本身状态为投资成功
        investMapper.updateStatus(investModel.getId(), InvestStatus.SUCCESS);
    }

    private void infoLog(String msg, long orderId, long amount, String loginName, long loanId) {
        logger.info(msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId);
    }

    private void errorLog(String msg, long orderId, long amount, String loginName, long loanId) {
        errorLog(msg, orderId, amount, loginName, loanId, null);
    }

    private void errorLog(String msg, long orderId, long amount, String loginName, long loanId, Throwable e) {
        logger.error(msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId, e);
    }

    private void fatalLog(String msg, long orderId, long amount, String loginName, long loanId) {
        fatalLog(msg, orderId, amount, loginName, loanId, null);
    }

    private void fatalLog(String msg, long orderId, long amount, String loginName, long loanId, Throwable e) {
        // TODO: 发短信提醒
        logger.fatal(msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId, e);
    }


}
