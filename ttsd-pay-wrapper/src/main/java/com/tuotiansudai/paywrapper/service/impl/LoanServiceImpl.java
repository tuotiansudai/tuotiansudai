package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.cfca.dto.AnxinContractType;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.AnxinContractHandleJob;
import com.tuotiansudai.job.AutoLoanOutJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.LoanOutSuccessHandleJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.JobManager;
import com.tuotiansudai.util.SendCloudMailUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR = "P";

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_TEMPLATE = "{0}" + CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RepayGeneratorService repayGeneratorService;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private SendCloudMailUtil sendCloudMailUtil;

    @Autowired
    private ReferrerRewardService referrerRewardService;

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AnxinSignService anxinSignService;

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        String payUserId = accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId();
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                payUserId,
                String.valueOf(loanModel.getLoanAmount()),
                String.valueOf(loanModel.getId()),
                String.valueOf(loanModel.getId()));

        MerUpdateProjectRequestModel merUpdateProjectPreheatRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()),
                LoanStatus.PREHEAT.getCode());

        MerUpdateProjectRequestModel merUpdateProjectRaisingRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()),
                LoanStatus.RAISING.getCode());
        try {
            MerBindProjectResponseModel createLoanResponseModel = paySyncClient.send(MerBindProjectMapper.class,
                    merBindProjectRequestModel,
                    MerBindProjectResponseModel.class);

            if (!createLoanResponseModel.isSuccess()) {
                payDataDto.setCode(createLoanResponseModel.getRetCode());
                payDataDto.setMessage(createLoanResponseModel.getRetMsg());
                return baseDto;
            }

            MerUpdateProjectResponseModel openLoanPhaseOneResponseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectPreheatRequestModel,
                    MerUpdateProjectResponseModel.class);

            if (!openLoanPhaseOneResponseModel.isSuccess()) {
                payDataDto.setCode(openLoanPhaseOneResponseModel.getRetCode());
                payDataDto.setMessage(openLoanPhaseOneResponseModel.getRetMsg());
                return baseDto;
            }

            MerUpdateProjectResponseModel openLoanPhaseTwoResponseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectRaisingRequestModel,
                    MerUpdateProjectResponseModel.class);

            if (!openLoanPhaseTwoResponseModel.isSuccess()) {
                payDataDto.setCode(openLoanPhaseTwoResponseModel.getRetCode());
                payDataDto.setMessage(openLoanPhaseTwoResponseModel.getRetMsg());
                return baseDto;
            }

            payDataDto.setStatus(true);

        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> cancelLoan(long loanId) {
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            InvestDto investDto = new InvestDto();
            investDto.setLoanId(String.valueOf(loanId));
            investDto.setLoginName(investModel.getLoginName());
            investDto.setAmount(String.valueOf(investModel.getAmount()));
            try {
                if (this.cancelPayBack(investDto, investModel.getId()).isSuccess()) {
                    logger.debug(investModel.getId() + " cancel payBack is success!");
                } else {
                    logger.debug(investModel.getId() + " cancel payBack is fail!");
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return this.updateLoanStatus(loanId, LoanStatus.CANCEL);
    }

    @Transactional
    public BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() == loanStatus) {
            payDataDto.setStatus(true);
            return baseDto;
        }

        try {
            boolean updateSuccess = Strings.isNullOrEmpty(loanStatus.getCode());
            if (!updateSuccess) {
                MerUpdateProjectRequestModel merUpdateProjectRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()), loanStatus.getCode());

                MerUpdateProjectResponseModel responseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                        merUpdateProjectRequestModel,
                        MerUpdateProjectResponseModel.class);
                updateSuccess = responseModel.isSuccess();
                payDataDto.setCode(responseModel.getRetCode());
                payDataDto.setMessage(responseModel.getRetMsg());
            }

            if (updateSuccess) {
                if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.CANCEL).contains(loanStatus)) {
                    loanModel.setRecheckTime(new Date());
                }
                loanModel.setStatus(loanStatus);
                loanMapper.update(loanModel);
                payDataDto.setStatus(true);
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> loanOut(long loanId) {

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        if (redisWrapperClient.setnx(AutoLoanOutJob.LOAN_OUT_IN_PROCESS_KEY + loanId, "1")) {
            try {
                ProjectTransferResponseModel umPayReturn = this.doLoanOut(loanId);
                payDataDto.setStatus(umPayReturn.isSuccess());
                payDataDto.setCode(umPayReturn.getRetCode());
                payDataDto.setMessage(umPayReturn.getRetMsg());
            } catch (PayException e) {
                payDataDto.setStatus(false);
                payDataDto.setMessage(e.getLocalizedMessage());
                logger.error(e.getLocalizedMessage(), e);
            } finally {
                redisWrapperClient.del(AutoLoanOutJob.LOAN_OUT_IN_PROCESS_KEY + loanId);
            }
        } else {
            payDataDto.setStatus(true);
            payDataDto.setCode(BaseSyncResponseModel.SUCCESS_CODE);
            payDataDto.setMessage("some other loan out process is running.");
            logger.error("some other thread is loan-outing for this loan, loanId:" + loanId);
        }
        return baseDto;
    }

    private ProjectTransferResponseModel doLoanOut(long loanId) throws PayException {

        // 查找借款人
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new PayException("loan is not exists [" + loanId + "]");
        }

        if (LoanStatus.REPAYING == loan.getStatus()) {
            logger.warn("loan has already been outed. [" + loanId + "]");
            ProjectTransferResponseModel umPayReturn = new ProjectTransferResponseModel();
            umPayReturn.setRetCode(AutoLoanOutJob.ALREADY_OUT);
            umPayReturn.setRetMsg("放款失败：标的已经被自动放款。");
            return umPayReturn;
        }

        if (LoanStatus.RECHECK != loan.getStatus()) {
            throw new PayException("loan is not ready for recheck [" + loanId + "]");
        }

        // 将已失效的投资记录状态置为失败
        investMapper.cleanWaitingInvest(loanId);

        String agentPayUserId = accountMapper.findByLoginName(loan.getAgentLoginName()).getPayUserId();

        // 查找所有投资成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        logger.debug("标的放款：查找到" + successInvestList.size() + "条成功的投资，标的ID:" + loanId);

        // 计算投资总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException("invest amount should great than 0");
        }

        BaseDto<PayDataDto> checkLoanAmount = umPayRealTimeStatusService.checkLoanAmount(loanId);
        if (!checkLoanAmount.getData().getStatus()) {
            throw new PayException(MessageFormat.format("标的(loanId={0})借款金额与投资金额不一致", String.valueOf(loanId)));
        }

        logger.debug("标的放款：发起联动优势放款请求，标的ID:" + loanId + "，代理人:" + agentPayUserId + "，放款金额:" + investAmountTotal);
        ProjectTransferResponseModel resp = doPayRequest(loanId, agentPayUserId, investAmountTotal);

        if (resp.isSuccess()) {
            logger.debug("标的放款：更新标的状态，标的ID:" + loanId);
            processLoanStatusForLoanOut(loan);

            logger.debug("标的放款：处理该标的的所有投资的账务信息，标的ID:" + loanId);
            processInvestForLoanOut(successInvestList);

            logger.debug("标的放款：把借款转给代理人账户，标的ID:" + loanId);
            processLoanAccountForLoanOut(loan, investAmountTotal);

            // loanOutSuccessHandle(loanId);
            createLoanOutSuccessHandleJob(loanId);

        }
        return resp;
    }

    private void createLoanOutSuccessHandleJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(LoanOutSuccessHandleJob.HANDLE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.LoanOut, LoanOutSuccessHandleJob.class)
                    .addJobData(LoanOutSuccessHandleJob.LOAN_ID_KEY, loanId)
                    .withIdentity(JobType.LoanOut.name(), "Loan-" + loanId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create loan out success handle job for loan[" + loanId + "] fail", e);
        }
    }

    @Override
    public boolean postLoanOut(long loanId) {
        LoanModel loan = loanMapper.findById(loanId);

        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);

        logger.debug("标的放款：生成还款计划，标的ID:" + loanId);
        try {
            repayGeneratorService.generateRepay(loanId);
        }catch (Exception e) {
            logger.error(MessageFormat.format("生成还款计划失败 (loanId = {0})", String.valueOf(loanId)), e);
            return false;
        }

        logger.debug("标的放款：处理推荐人奖励，标的ID:" + loanId);
        try {
            referrerRewardService.rewardReferrer(loan, successInvestList);
        } catch (Exception e) {
            logger.error(MessageFormat.format("发放推荐人奖励失败 (loanId = {0})", String.valueOf(loanId)), e);
            return false;
        }

        logger.debug("标的放款：处理短信和邮件通知，标的ID:" + loanId);
        try {
            processNotifyForLoanOut(loanId);
        } catch (Exception e) {
            logger.error(MessageFormat.format("放款短信邮件通知失败 (loanId = {0})", String.valueOf(loanId)), e);
        }

        logger.debug("标的放款：生成合同，标的ID:" + loanId);
        anxinSignService.createContracts(loanId);

        logger.debug("标的放款：创建job —— 十分钟后，查询并更新合同状态。标的ID:" + loanId);
        updateContractResponseHandleJob(loanId);

        return true;
    }

    private ProjectTransferResponseModel doPayRequest(long loanId, String payUserId, long amount) throws PayException {
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                String.valueOf(loanId), String.valueOf(loanId), payUserId, String.valueOf(amount));
        ProjectTransferResponseModel responseModel = paySyncClient.send(
                ProjectTransferMapper.class,
                requestModel,
                ProjectTransferResponseModel.class);
        return responseModel;
    }

    private long computeInvestAmountTotal(List<InvestModel> investList) {
        long amount = 0L;
        for (InvestModel investModel : investList) {
            amount += investModel.getAmount();
        }
        return amount;
    }

    private void processInvestForLoanOut(List<InvestModel> investList) {
        if (investList == null) {
            return;
        }
        for (InvestModel invest : investList) {
            try {
                amountTransfer.transferOutFreeze(invest.getLoginName(),
                        invest.getId(), invest.getAmount(), UserBillBusinessType.LOAN_SUCCESS, null, null);
            } catch (AmountTransferException e) {
                logger.error("transferOutFreeze Fail while loan out, invest [" + invest.getId() + "]", e);
            }
        }
    }

    // 把借款转给代理人账户
    private void processLoanAccountForLoanOut(LoanModel loan, long amount) {
        try {
            long orderId = loan.getId();
            amountTransfer.transferInBalance(loan.getAgentLoginName(), orderId, amount, UserBillBusinessType.LOAN_SUCCESS, null, null);
        } catch (Exception e) {
            logger.error("transferInBalance Fail while loan out, loan[" + loan.getId() + "]", e);
        }
    }

    private void processNotifyForLoanOut(long loanId) {
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);

        logger.debug(MessageFormat.format("标的: {0} 放款短信通知", loanId));
        notifyInvestorsLoanOutSuccessfulBySMS(investModels);

        logger.debug(MessageFormat.format("标的: {0} 放款邮件通知", loanId));
        notifyInvestorsLoanOutSuccessfulByEmail(investModels);

    }

    private void notifyInvestorsLoanOutSuccessfulBySMS(List<InvestModel> investModels) {
        for (InvestModel investModel : investModels) {
            UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
            LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
            InvestNotifyInfo notifyInfo = new InvestNotifyInfo(investModel, loanModel, userModel);
            InvestSmsNotifyDto dto = new InvestSmsNotifyDto();
            dto.setLoanName(notifyInfo.getLoanName());
            dto.setMobile(notifyInfo.getMobile());
            dto.setAmount(AmountConverter.convertCentToString(notifyInfo.getAmount()));
            smsWrapperClient.sendInvestNotify(dto);
        }
    }

    private void notifyInvestorsLoanOutSuccessfulByEmail(List<InvestModel> investModels) {
        for (InvestModel investModel : investModels) {
            UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
            LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
            InvestNotifyInfo notifyInfo = new InvestNotifyInfo(investModel, loanModel, userModel);

            Map<String, String> emailParameters = Maps.newHashMap(new ImmutableMap.Builder<String, String>()
                    .put("loanName", notifyInfo.getLoanName())
                    .put("money", AmountConverter.convertCentToString(notifyInfo.getAmount()))
                    .build());
            String userEmail = notifyInfo.getEmail();
            if (StringUtils.isNotEmpty(userEmail)) {
                sendCloudMailUtil.sendMailByLoanOut(userEmail, emailParameters);
            }
        }
    }

    private void processLoanStatusForLoanOut(LoanModel loan) {
        BaseDto<PayDataDto> dto = updateLoanStatus(loan.getId(), LoanStatus.REPAYING);
        if (dto.getData().getStatus()) {
            LoanModel loan4Update = new LoanModel();
            loan4Update.setId(loan.getId());
            loan4Update.setRecheckTime(new Date());
            loanMapper.update(loan4Update);
        } else {
            logger.error("update loan status failed : " + dto.getData().getMessage());
        }
    }

    private BaseDto<PayDataDto> cancelPayBack(InvestDto dto, long investId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newCancelPayBackRequest(dto.getLoanId(),
                MessageFormat.format(CANCEL_INVEST_PAY_BACK_ORDER_ID_TEMPLATE, String.valueOf(investId), String.valueOf(System.currentTimeMillis())),
                accountModel.getPayUserId(), String.valueOf(dto.getAmount()));
        try {
            ProjectTransferResponseModel responseModel = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelPayBackCallback(Map<String, String> paramsMap, String queryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        String orderIdOri = callbackRequest.getOrderId();
        String orderIdStr = orderIdOri == null ? "" : orderIdOri.split(CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR)[0];
        long orderId = Long.parseLong(orderIdStr);
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest callback notify order is not exist (orderId = {0})", orderId));
            return null;
        }
        String loginName = investModel.getLoginName();
        if (callbackRequest.isSuccess()) {
            if (investMapper.findById(investModel.getId()).getStatus() != InvestStatus.CANCEL_INVEST_PAYBACK) {
                investModel.setStatus(InvestStatus.CANCEL_INVEST_PAYBACK);
                investMapper.update(investModel);
                try {
                    amountTransfer.unfreeze(loginName, orderId, investModel.getAmount(), UserBillBusinessType.CANCEL_INVEST_PAYBACK, null, null);
                } catch (AmountTransferException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        } else {
            //TODO SEND_SMS
        }
        String respData = callbackRequest.getResponseData();
        return respData;
    }

    private void updateContractResponseHandleJob(long businessId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AnxinContractHandleJob.HANDLE_DELAY_MINUTES)
                    .toDate();
            jobManager.newJob(JobType.ContractResponse, AnxinContractHandleJob.class)
                    .addJobData(AnxinContractHandleJob.BUSINESS_ID, businessId)
                    .addJobData(AnxinContractHandleJob.ANXIN_CONTRACT_TYPE, AnxinContractType.TRANSFER_CONTRACT)
                    .withIdentity(JobType.ContractResponse.name(), "Loan-" + businessId)
                    .withIdentity(JobType.ContractResponse.name(), "Loan-" + businessId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create update contract response  handle job for loan[" + businessId + "] fail", e);
        }
    }

}
