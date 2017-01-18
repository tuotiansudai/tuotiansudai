package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.InvestSmsNotifyDto;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.AnxinCreateContractJob;
import com.tuotiansudai.job.AutoLoanOutJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.LoanOutSuccessHandleJob;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
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
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
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
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.util.SendCloudMailUtil;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR = "P";

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_TEMPLATE = "{0}" + CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR + "{1}";

    private final static String LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE = "LOAN_OUT_IDEMPOTENT_CHECK:{0}";

    private final static String DO_PAY_REQUEST = "DO_PAY_REQUEST";

    private final static String SMS_AND_EMAIL = "SMS_AND_EMAIL";

    private final static String TRANSFER_OUT_FREEZE = "TRANSFER_OUT_FREEZE:INVEST:{0}";

    private final static String TRANSFER_IN_BALANCE = "TRANSFER_IN_BALANCE";

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
    private MQWrapperClient mqWrapperClient;

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
                    logger.info(investModel.getId() + " cancel payBack is success!");
                } else {
                    logger.info(investModel.getId() + " cancel payBack is fail!");
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
    public BaseDto<PayDataDto> loanOut(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

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

            this.sendMessage(loanId);
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
            throw new PayException(MessageFormat.format("loan{0} status{1) is not RECHECK, loan out is failed", String.valueOf(loanId), loan.getStatus().name()));
        }

        // 将已失效的投资记录状态置为失败
        investMapper.cleanWaitingInvest(loanId);

        String agentPayUserId = accountMapper.findByLoginName(loan.getAgentLoginName()).getPayUserId();

        // 查找所有投资成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        logger.info("标的放款：查找到" + successInvestList.size() + "条成功的投资，标的ID:" + loanId);

        // 计算投资总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException(MessageFormat.format("loan{0} out is failed, invest amount sum is less then 0", String.valueOf(loanId)));
        }

        BaseDto<PayDataDto> checkLoanAmount = umPayRealTimeStatusService.checkLoanAmount(loanId, investAmountTotal);

        if (!checkLoanAmount.getData().getStatus()) {
            throw new PayException(MessageFormat.format("标的(loanId={0})借款金额与投资金额不一致", String.valueOf(loanId)));
        }

        logger.info("[标的放款]：发起联动优势放款请求，标的ID:" + loanId + "，代理人:" + agentPayUserId + "，放款金额:" + investAmountTotal);

        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        String beforeSendStatus = redisWrapperClient.hget(redisKey, DO_PAY_REQUEST);
        ProjectTransferResponseModel resp = new ProjectTransferResponseModel();
        if (Strings.isNullOrEmpty(beforeSendStatus) || beforeSendStatus.equals(SyncRequestStatus.FAILURE.name())){
            try {
                redisWrapperClient.hset(redisKey, DO_PAY_REQUEST, SyncRequestStatus.SENT.name());
                ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                        String.valueOf(loanId), String.valueOf(loanId), agentPayUserId, String.valueOf(investAmountTotal));
                resp = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
                redisWrapperClient.hset(redisKey, DO_PAY_REQUEST, SyncRequestStatus.SUCCESS.name());
            } catch (PayException e) {
                logger.error(MessageFormat.format("[标的放款]:发起放款联动优势请求失败,标的ID : {0}", String.valueOf(loanId)), e);
            }
        }

        String afterSendStatus = redisWrapperClient.hget(redisKey, DO_PAY_REQUEST);

        if (SyncRequestStatus.SENT.name().equals(afterSendStatus)) {
            resp.setRetMsg(MessageFormat.format("[标的放款]:发起放款联动优势请求重复,标的ID : {0}", String.valueOf(loanId)));
            logger.error(resp.getRetMsg());
            return resp;

        }

        if (SyncRequestStatus.SUCCESS.name().equals(afterSendStatus)) {
            logger.info("[标的放款]：更新标的状态，标的ID:" + loanId);
            this.updateLoanStatus(loanId, LoanStatus.REPAYING);

            logger.info("[标的放款]：处理该标的的所有投资的账务信息，标的ID:" + loanId);
            this.processInvestForLoanOut(successInvestList,loanId);

            logger.info("[标的放款]：把借款转给代理人账户，标的ID:" + loanId);
            this.processLoanAccountForLoanOut(loanId, loan.getAgentLoginName(), investAmountTotal);

            this.createLoanOutSuccessHandleJob(loanId);
        }

        return resp;
    }

    private void createLoanOutSuccessHandleJob(long loanId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(LoanOutSuccessHandleJob.HANDLE_DELAY_MINUTES).toDate();
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

        logger.info("[标的放款]：生成还款计划，标的ID:" + loanId);
        try {
            repayGeneratorService.generateRepay(loanId);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[标的放款]:生成还款计划失败 (loanId = {0})", String.valueOf(loanId)), e);
            return false;
        }

        logger.info("[标的放款]：处理推荐人奖励，标的ID:" + loanId);
        try {
            referrerRewardService.rewardReferrer(loan, successInvestList);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[标的放款]:发放推荐人奖励失败 (loanId = {0})", String.valueOf(loanId)), e);
            return false;
        }

        logger.info("[标的放款]：处理短信和邮件通知，标的ID:" + loanId);

        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        String statusString = redisWrapperClient.hget(redisKey, SMS_AND_EMAIL);
        if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {
            try {
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, SyncRequestStatus.SENT.name());
                processNotifyForLoanOut(loanId);
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, SyncRequestStatus.SUCCESS.name());
            } catch (Exception e) {
                redisWrapperClient.hset(redisKey, SMS_AND_EMAIL, SyncRequestStatus.FAILURE.name());
                logger.error(MessageFormat.format("[标的放款]:放款短信邮件通知失败 (loanId = {0})", String.valueOf(loanId)), e);
            }
        } else {
            logger.info(MessageFormat.format("[标的放款]:重复发送放款短信邮件通知,标的ID : {0}", String.valueOf(loanId)));
        }

        logger.info("标的放款：生成合同，标的ID:" + loanId);
        try {
            createAnxinContractJob(loanId);
        } catch (Exception e) {
            logger.error(MessageFormat.format("放款生成合同失败 (loanId = {0})", String.valueOf(loanId)), e);
        }

        return true;
    }

    private long computeInvestAmountTotal(List<InvestModel> investList) {
        long amount = 0L;
        for (InvestModel investModel : investList) {
            amount += investModel.getAmount();
        }
        return amount;
    }

    private void processInvestForLoanOut(List<InvestModel> investList,long loanId) {
        if (CollectionUtils.isEmpty(investList)) {
            return;
        }

        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        investList.forEach(invest -> {
            String transferKey = MessageFormat.format(TRANSFER_OUT_FREEZE,invest.getId());
            try {
                String statusString = redisWrapperClient.hget(redisKey, transferKey);
                if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {
                    amountTransfer.transferOutFreeze(invest.getLoginName(),
                            invest.getId(),
                            invest.getAmount(),
                            UserBillBusinessType.LOAN_SUCCESS,
                            null,
                            null);
                    redisWrapperClient.hset(redisKey, transferKey, SyncRequestStatus.SUCCESS.name());
                }
            } catch (AmountTransferException e) {
                redisWrapperClient.hset(redisKey, transferKey, SyncRequestStatus.FAILURE.name());
                logger.error("transferOutFreeze Fail while loan out, invest [" + invest.getId() + "]", e);
            }
        });
    }

    // 把借款转给代理人账户
    private void processLoanAccountForLoanOut(long loanId, String agentLoginName, long amount) {
        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        try {
            String statusString = redisWrapperClient.hget(redisKey, TRANSFER_IN_BALANCE);
            if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {
                amountTransfer.transferInBalance(agentLoginName, loanId, amount, UserBillBusinessType.LOAN_SUCCESS, null, null);
                redisWrapperClient.hset(redisKey, TRANSFER_IN_BALANCE, SyncRequestStatus.SUCCESS.name());
            }
        } catch (Exception e) {
            redisWrapperClient.hset(redisKey, TRANSFER_IN_BALANCE, SyncRequestStatus.FAILURE.name());
            logger.error("transferInBalance Fail while loan out, loan[" + loanId + "]", e);
        }
    }

    private void processNotifyForLoanOut(long loanId) {
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);

        logger.info(MessageFormat.format("[标的放款]:标的: {0} 放款短信通知", loanId));
        notifyInvestorsLoanOutSuccessfulBySMS(investModels);

        logger.info(MessageFormat.format("[标的放款]:标的: {0} 放款邮件通知", loanId));
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
        }
        return callbackRequest.getResponseData();
    }

    private void createAnxinContractJob(long businessId) {
        try {
            Date triggerTime = new DateTime().plusMinutes(AnxinCreateContractJob.HANDLE_DELAY_MINUTES).toDate();

            jobManager.newJob(JobType.CreateAnXinContract, AnxinCreateContractJob.class)
                    .addJobData(AnxinCreateContractJob.BUSINESS_ID, businessId)
                    .withIdentity(JobType.CreateAnXinContract.name(), "businessId-" + businessId)
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create query contract job for loan/transfer[" + businessId + "] fail", e);
        }
    }

    private void sendMessage(long loanId) {
        //Title:您投资的{0}已经满额放款，预期年化收益{1}%
        //Content:尊敬的用户，您投资的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。
        LoanModel loanModel = loanMapper.findById(loanId);
        List<String> loginNames =  investMapper.findSuccessInvestsByLoanId(loanId).stream().map(InvestModel::getLoginName).collect(Collectors.toList());
        String title = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getTitleTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);
        String content = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getContentTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);

        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.LOAN_OUT_SUCCESS,
                loginNames, title, content, loanId));

        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(loginNames, PushSource.ALL, PushType.LOAN_OUT_SUCCESS, title));
    }
}
