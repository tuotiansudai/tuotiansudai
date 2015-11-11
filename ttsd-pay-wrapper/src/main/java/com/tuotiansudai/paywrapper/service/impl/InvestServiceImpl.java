package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.InvestNotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    private AmountTransfer amountTransfer;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private SendCloudMailUtil sendCloudMailUtil;


    @Autowired
    private InvestNotifyRequestMapper investNotifyRequestMapper;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;


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
            investMapper.create(investModel);
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
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

    @Transactional
    private BaseDto<PayDataDto> investNopwd(long loanId, long amount, String loginName) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        InvestModel investModel = new InvestModel(loanId, amount, loginName, Source.AUTO);
        investModel.setIsAutoInvest(true);
        investModel.setId(idGenerator.generate());
        investMapper.create(investModel);
        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newInvestNopwdRequest(
                String.valueOf(loanId),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(investModel.getAmount())
        );
        try {
            checkLoanInvestAccountAmount(loginName, investModel.getLoanId(), investModel.getAmount());
            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            onInvestFail(investModel);
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
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

    @Override
    public BaseDto<PayDataDto> asyncInvestCallback(){
        int limitCount = 10;
        List<InvestNotifyRequestModel> todoList = investNotifyRequestMapper.getTodoList(limitCount);

        for(InvestNotifyRequestModel model : todoList) {
            if(updateInvestNotifyRequestStatus(model)) {
                processOneCallback(model);
            }
        }

        BaseDto<PayDataDto> asyncInvestNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncInvestNotifyDto.setData(baseDataDto);

        return asyncInvestNotifyDto;
    }


    private boolean updateInvestNotifyRequestStatus(InvestNotifyRequestModel model) {
        try {
            investNotifyRequestMapper.updateStatus(model.getId(), InvestNotifyProcessStatus.DONE);
        }catch (Exception e) {
            // TODO: 发送短信
            logger.fatal("update_invest_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId());
            return false;
        }
        return true;
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
                infoLog("over_invest", orderId, investModel.getAmount(), loginName, loanId);
                // 超投返款处理
                overInvestPaybackProcess(orderId, investModel, loginName, loanId);
            } else {
                // 投资成功
                infoLog("invest_success", orderId, investModel.getAmount(), loginName, loanId);
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
                infoLog("pay_back_success", orderId, investModel.getAmount(), loginName, loanId);
                paybackSuccess = true;
            } else {
                // 联动优势返回返款失败，但是标记此条请求已经处理完成，记录日志，在异步notify中进行投资成功处理
                errorLog("pay_back_fail", orderId, investModel.getAmount(), loginName, loanId);
            }
        } catch (PayException e) {
            // 调用umpay时出现异常(可能已经返款成功了)。发短信通知管理员
            fatalLog("pay_back_PayException", orderId, investModel.getAmount(), loginName, loanId, e);
        } catch (Exception e) {
            // 所有其他异常，包括数据库链接，网络异常，记录日志，发短信通知管理员，抛出异常，事务回滚。
            fatalLog("pay_back_other_exceptions", orderId, investModel.getAmount(), loginName, loanId, e);
            throw e;
        }

        if(!paybackSuccess) {
            // 如果返款失败，则记录本次投资为 超投返款失败
            investMapper.updateStatus(investModel.getId(), InvestStatus.OVER_INVEST_PAYBACK_FAIL);
        }
        return paybackSuccess;
    }

    private void onInvestFail(InvestModel investModel) {
        investMapper.updateStatus(investModel.getId(), InvestStatus.FAIL);
    }

    @Override
    public List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return autoInvestPlanMapper.findEnabledPlanByPeriod(period.getPeriodValue(), cal.getTime());
    }

    @Override
    public void autoInvest(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<AutoInvestPlanModel> autoInvestPlanModels = this.findValidPlanByPeriod(AutoInvestMonthPeriod.generateFromLoanPeriod(loanModel.getType().getLoanPeriodUnit(), loanModel.getPeriods()));
        for (AutoInvestPlanModel autoInvestPlanModel : autoInvestPlanModels) {
            try {
                long availableLoanAmount = loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanId);
                if (availableLoanAmount <= 0) {
                    return;
                }
                long availableSelfLoanAmount = loanModel.getMaxInvestAmount() - investMapper.sumSuccessInvestAmountByLoginName(loanId, autoInvestPlanModel.getLoginName());
                if (availableSelfLoanAmount <= 0) {
                    continue;
                }
                long autoInvestAmount = this.calculateAutoInvestAmount(autoInvestPlanModel, NumberUtils.min(availableLoanAmount, availableSelfLoanAmount, loanModel.getMinInvestAmount()), loanModel.getInvestIncreasingAmount(), loanModel.getMinInvestAmount());
                if (autoInvestAmount == 0) {
                    continue;
                }
                BaseDto<PayDataDto> baseDto = this.investNopwd(loanId, autoInvestAmount, autoInvestPlanModel.getLoginName());
                if (!baseDto.isSuccess()) {
                    logger.debug(MessageFormat.format("auto invest failed auto invest plan id is {0} and invest amount is {1} and loanId id {2}", autoInvestPlanModel.getId(), autoInvestAmount, loanId));
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                continue;
            }
        }
    }
    @Override
    public void notifyInvestorRepaySuccessfulByEmail(long loanId,int period){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<InvestNotifyInfo> notifyInfos = investMapper.findSuccessInvestMobileEmailAndAmount(loanId);

        for(InvestNotifyInfo investNotifyInfo:notifyInfos){
            long investId = investNotifyInfo.getInvestId();
            String email = investNotifyInfo.getEmail();
            String loanName = investNotifyInfo.getLoanName();
            int periods = investNotifyInfo.getPeriods();
            InvestRepayModel investRepay = investRepayMapper.findCompletedInvestRepayByIdAndPeriod(investId, period);
            if(investRepay != null){

                Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                        .put("loanName", loanName)
                        .put("periods", investRepay.getPeriod() + "/" + periods)
                        .put("repayDate", simpleDateFormat.format(investRepay.getActualRepayDate()))
                        .put("amount", AmountConverter.convertCentToString(calculateProfit(investRepay.getCorpus(), investRepay.getActualInterest(),
                                investRepay.getDefaultInterest(), investRepay.getActualFee())))
                        .build());
                if (StringUtils.isNotEmpty(email)) {
                    sendCloudMailUtil.sendMailByRepayCompleted(email, emailParameters);
                }
            }
        }
    }

    private long calculateProfit(long corpus,long actualInterest,long defaultInterest,long actualFee){
        return corpus + actualInterest + defaultInterest - actualFee;
    }
    /**
     * umpay 超投返款的回调
     *
     * @param paramsMap
     * @param queryString
     * @return
     */
    public String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString){

        logger.debug("into over_invest_payback_callback, queryString: " + queryString);

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
            errorLog("pay_back_notify_fail,take_as_invest_success", orderId, investModel.getAmount(), loginName, investModel.getLoanId());

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
            amountTransfer.freeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS, null, null);
        } catch (AmountTransferException e) {
            // 记录日志，发短信通知管理员
            fatalLog("invest success, but freeze account fail", orderId, investModel.getAmount(), loginName, investModel.getLoanId(), e);
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


    private long calculateAutoInvestAmount(AutoInvestPlanModel autoInvestPlanModel, long availableLoanAmount, long investIncreasingAmount, long minLoanInvestAmount) {
        long availableAmount = accountMapper.findByLoginName(autoInvestPlanModel.getLoginName()).getBalance() - autoInvestPlanModel.getRetentionAmount();
        long maxInvestAmount = autoInvestPlanModel.getMaxInvestAmount();
        long minInvestAmount = autoInvestPlanModel.getMinInvestAmount();
        long returnAmount = 0;
        if (availableLoanAmount < minInvestAmount) {
            return returnAmount;
        }
        if (availableAmount >= maxInvestAmount) {
            returnAmount = maxInvestAmount;
        } else if (availableAmount < maxInvestAmount && availableAmount >= minInvestAmount) {
            returnAmount = availableAmount;
        }
        if (returnAmount >= availableLoanAmount) {
            returnAmount = availableLoanAmount;
        }
        return returnAmount - (returnAmount - minLoanInvestAmount) % investIncreasingAmount;
    }

}
