package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.job.AutoInvestJob;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("${console.auto.invest.delay.minutes}")
    private int autoInvestDelayMinutes;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private JobManager jobManager;

    /**
     * @param loanTitleDto
     * @function 创建标题
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoanTitleModel createTitle(LoanTitleDto loanTitleDto) {
        LoanTitleModel loanTitleModel = new LoanTitleModel();
        long id = idGenerator.generate();
        loanTitleModel.setId(id);
        loanTitleModel.setTitle(loanTitleDto.getTitle());
        loanTitleModel.setType(LoanTitleType.NEW_TITLE_TYPE);
        loanTitleMapper.create(loanTitleModel);
        return loanTitleModel;
    }

    /**
     * @return
     * @function 获取所有标题
     */
    @Override
    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

    /**
     * @param loanDto
     * @return BaseDto<PayDataDto>
     * @function 创建标的
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        long minInvestAmount = AmountConverter.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountConverter.convertStringToCent(loanDto.getLoanAmount());
        String agentLoginName = getLoginName(loanDto.getAgentLoginName());
        if (agentLoginName == null) {
            dataDto.setMessage("代理用户不存在");
            baseDto.setData(dataDto);
            return baseDto;
        } else {
            if (userRoleMapper.findByLoginNameAndRole(loanDto.getAgentLoginName(), Role.LOANER.name()) == null) {
                dataDto.setMessage("代理用户不具有借款人角色");
                return baseDto;
            }
        }
        if (loanDto.getPeriods() <= 0) {
            dataDto.setMessage("借款期限最小为1");
            return baseDto;
        }
        if (loanAmount <= 0) {
            dataDto.setMessage("预计出借金额应大于0");
            return baseDto;
        }
        if (minInvestAmount <= 0) {
            dataDto.setMessage("最小投资金额应大于0");
            return baseDto;
        }
        if (maxInvestAmount < minInvestAmount) {
            dataDto.setMessage("最小投资金额不得大于最大投资金额");
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            dataDto.setMessage("最大投资金额不得大于预计出借金额");
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            dataDto.setMessage("筹款启动时间不得晚于筹款截止时间");
            return baseDto;
        }
        long projectId = idGenerator.generate();/****标的号****/
        loanDto.setId(projectId);
        loanMapper.create(new LoanModel(loanDto));
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanDto.getLoanTitles();
        if (loanTitleRelationModelList.size() > 0) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanDto.getLoanTitles()) {
                loanTitleRelationModel.setId(idGenerator.generate());
                loanTitleRelationModel.setLoanId(projectId);
            }
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
        dataDto.setStatus(true);
        return baseDto;
    }

    private String getLoginName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            return accountModel.getPayUserId();
        }
        return null;
    }

    @Override
    public BaseDto<LoanDto> getLoanDetail(String loginName, long loanId) {
        BaseDto<LoanDto> dto = new BaseDto<>();
        LoanDto loanDto = new LoanDto();

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return dto;
        }

        loanDto = convertModelToDto(loanModel, loginName);
        loanDto.setStatus(true);
        dto.setData(loanDto);
        return dto;
    }

    private LoanDto convertModelToDto(LoanModel loanModel, String loginName) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanModel.getId());
        loanDto.setProjectName(loanModel.getName());
        loanDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDto.setLoanerUserName(loanModel.getLoanerUserName());
        loanDto.setLoanerIdentityNumber(loanModel.getLoanerIdentityNumber());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDto.setDescriptionText(loanModel.getDescriptionText());
        loanDto.setLoanAmount(new BigDecimal(loanModel.getLoanAmount()).toString());
        loanDto.setInvestIncreasingAmount("" + loanModel.getInvestIncreasingAmount());
        loanDto.setMinInvestAmount("" + loanModel.getMinInvestAmount());
        loanDto.setActivityType(loanModel.getActivityType());
        loanDto.setBasicRate(new BigDecimal(String.valueOf(loanModel.getBaseRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString());
        if (loanModel.getActivityRate() > 0) {
            loanDto.setActivityRate(new BigDecimal(String.valueOf(loanModel.getActivityRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).toString());
        }
        loanDto.setLoanStatus(loanModel.getStatus());
        loanDto.setType(loanModel.getType());
        loanDto.setMaxInvestAmount(AmountConverter.convertCentToString(loanModel.getMaxInvestAmount()));
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), loginName);
            loanDto.setBalance(accountModel.getBalance() / 100d);
            loanDto.setMaxAvailableInvestAmount(AmountConverter.convertCentToString(calculateMaxAvailableInvestAmount(
                    accountModel.getBalance(), loanModel.getLoanAmount() - investedAmount,
                    loanModel.getMinInvestAmount(), loanModel.getInvestIncreasingAmount(),
                    loanModel.getMaxInvestAmount(), sumSuccessInvestAmount)));
        }

        loanDto.setAmountNeedRaised(calculateAmountNeedRaised(investedAmount, loanModel.getLoanAmount()));
        loanDto.setRaiseCompletedRate(calculateRaiseCompletedRate(investedAmount, loanModel.getLoanAmount()));
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        loanDto.setLoanTitleDto(loanTitleMapper.findAll());
        loanDto.setPreheatSeconds(calculatorPreheatSeconds(loanModel.getFundraisingStartTime()));
        loanDto.setFundraisingEndTime(loanModel.getFundraisingEndTime());
        loanDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());
        loanDto.setRaisingCompleteTime(loanModel.getRaisingCompleteTime());

        return loanDto;
    }

    private long calculateMaxAvailableInvestAmount(long balance, long amountNeedRaised, long minInvestAmount,
                                                   long investIncreasingAmount, long maxInvestAmount, long userInvestedAmount) {
        long maxAvailableInvestAmount = NumberUtils.min(balance, amountNeedRaised, maxInvestAmount - userInvestedAmount);

        if (maxAvailableInvestAmount >= minInvestAmount) {
            maxAvailableInvestAmount = maxAvailableInvestAmount - (maxAvailableInvestAmount - minInvestAmount) % investIncreasingAmount;
        } else {
            maxAvailableInvestAmount = 0L;
        }
        return maxAvailableInvestAmount;

    }

    private long calculatorPreheatSeconds(Date fundraisingStartTime) {
        if (fundraisingStartTime == null) {
            return 0L;
        }
        long time = (fundraisingStartTime.getTime() - System
                .currentTimeMillis()) / 1000;
        if (time < 0) {
            return 0L;
        }
        return time;

    }

    private double calculateAmountNeedRaised(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return loanAmountBig.subtract(investedAmountBig)
                .divide(new BigDecimal(100D), 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private double calculateRaiseCompletedRate(long investedAmount, long loanAmount) {
        BigDecimal investedAmountBig = new BigDecimal(investedAmount);
        BigDecimal loanAmountBig = new BigDecimal(loanAmount);
        return investedAmountBig.divide(loanAmountBig, 4, BigDecimal.ROUND_DOWN).doubleValue();
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> openLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        PayDataDto payDataDto = new PayDataDto();
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        if (loanMapper.findById(loanDto.getId()) == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (LoanStatus.WAITING_VERIFY == loanDto.getLoanStatus()) {
            loanDto.setLoanStatus(LoanStatus.PREHEAT);
            baseDto = payWrapperClient.createLoan(loanDto);
            BaseDto<PayDataDto> openLoanDto = payWrapperClient.updateLoan(loanDto);
            if (baseDto.getData().getStatus() && openLoanDto.getData().getStatus()) {
                loanDto.setLoanStatus(LoanStatus.RAISING);
                BaseDto<PayDataDto> investLoanDto = payWrapperClient.updateLoan(loanDto);
                if (investLoanDto.getData().getStatus()) {
                    loanDto.setLoanStatus(LoanStatus.PREHEAT);
                    loanDto.setVerifyTime(new Date());
                    updateLoanAndLoanTitleRelation(loanDto);

                    // 建标成功后，再次校验Loan状态，以确保只有建标成功后才创建job
                    LoanModel loanModel = loanMapper.findById(loanDto.getId());
                    if (loanModel.getStatus() == LoanStatus.PREHEAT) {
                        createFundraisingStartJob(loanModel);
                        createDeadLineFundraisingJob(loanModel);
                    }
                    return investLoanDto;
                }
            }
            return baseDto;
        }
        payDataDto.setStatus(false);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> updateLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        PayDataDto payDataDto = new PayDataDto();
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        LoanModel loanModelBefore = loanMapper.findById(loanDto.getId());
        if (loanModelBefore == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        updateLoanAndLoanTitleRelation(loanDto);
        LoanModel loanModelAfter = loanMapper.findById(loanDto.getId());
        if (loanModelBefore.getFundraisingEndTime() != loanModelAfter.getFundraisingEndTime()) {
            createDeadLineFundraisingJob(loanModelAfter);
        }
        createFundraisingStartJob(loanModelAfter);
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> delayLoan(LoanDto loanDto) {
        LoanModel nowLoanModel = loanMapper.findById(loanDto.getId());
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        PayDataDto payDataDto = new PayDataDto();
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        if (loanMapper.findById(loanDto.getId()) == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        loanMapper.updateStatus(loanDto.getId(), LoanStatus.RAISING);
        if (nowLoanModel.getFundraisingEndTime() != loanDto.getFundraisingEndTime()) {
            createDeadLineFundraisingJob(new LoanModel(loanDto));
        }
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    public void startFundraising(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (LoanStatus.PREHEAT == loanModel.getStatus()) {
            loanMapper.updateStatus(loanId, LoanStatus.RAISING);
            this.createAutoInvestJob(loanId);
        }
    }

    private void createFundraisingStartJob(LoanModel loanModel) {
        try {
            jobManager.newJob(JobType.LoanStatusToRaising, FundraisingStartJob.class)
                    .withIdentity("FundraisingStartJob", "Loan-" + loanModel.getId())
                    .replaceExistingJob(true)
                    .runOnceAt(loanModel.getFundraisingStartTime())
                    .addJobData(FundraisingStartJob.LOAN_ID_KEY, String.valueOf(loanModel.getId()))
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create fundraising start job for loan[" + loanModel.getId() + "] fail", e);
        }
    }

    private void createAutoInvestJob(long loanId) {
        try {
            jobManager.newJob(JobType.AutoInvest, AutoInvestJob.class)
                    .runOnceAt(new DateTime().plusMinutes(autoInvestDelayMinutes).toDate())
                    .addJobData(AutoInvestJob.LOAN_ID_KEY, String.valueOf(loanId))
                    .withIdentity("AutoInvestJob", "Loan-" + loanId)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create auto invest job for loan[" + loanId + "] fail", e);
        }
    }

    @Override
    public LoanModel findLoanById(long loanId) {
        // TODO: 重构 Mybatis 直接查询关联关系
        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanTitleRelationMapper.findByLoanId(loanId);
        loanModel.setLoanTitles(loanTitleRelationModelList);
        return loanModel;
    }

    @Override
    public boolean loanIsExist(long loanId) {
        return findLoanById(loanId) != null;
    }

    private BaseDto<PayDataDto> loanParamValidate(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        long minInvestAmount = AmountConverter.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountConverter.convertStringToCent(loanDto.getLoanAmount());
        if (maxInvestAmount < minInvestAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (maxInvestAmount > loanAmount) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        String loanAgentId = getLoginName(loanDto.getAgentLoginName());
        if (loanAgentId == null) {
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        } else {
            if (userRoleMapper.findByLoginNameAndRole(loanDto.getAgentLoginName(), Role.LOANER.name()) == null) {
                payDataDto.setStatus(false);
                baseDto.setData(payDataDto);
                return baseDto;
            }
        }
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);
        return baseDto;
    }

    private void updateLoanAndLoanTitleRelation(LoanDto loanDto) {
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanDto.getLoanStatus());
        loanMapper.update(loanModel);
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanTitleRelationMapper.findByLoanId(loanDto.getId());
        if (CollectionUtils.isNotEmpty(loanTitleRelationModelList)) {
            loanTitleRelationMapper.delete(loanDto.getId());
        }
        loanTitleRelationModelList = loanDto.getLoanTitles();
        if (CollectionUtils.isNotEmpty(loanTitleRelationModelList)) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanTitleRelationModelList) {
                loanTitleRelationModel.setId(idGenerator.generate());
                loanTitleRelationModel.setLoanId(loanModel.getId());
            }
            loanTitleRelationMapper.create(loanTitleRelationModelList);
        }
    }

    @Override
    public BaseDto<BasePaginationDataDto> getInvests(final String loginName, long loanId, int index, int pageSize) {
        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestPaginationItemDto> records = Lists.newArrayList();

        if (CollectionUtils.isNotEmpty(investModels)) {
            records = Lists.transform(investModels, new Function<InvestModel, InvestPaginationItemDto>() {
                @Override
                public InvestPaginationItemDto apply(InvestModel input) {
                    InvestPaginationItemDto item = new InvestPaginationItemDto();
                    item.setLoginName(!Strings.isNullOrEmpty(loginName) && loginName.equalsIgnoreCase(input.getLoginName())
                            ? input.getLoginName()
                            : input.getLoginName().substring(0, 3) + "******");
                    item.setAmount(AmountConverter.convertCentToString(input.getAmount()));
                    item.setSource(input.getSource());
                    item.setAutoInvest(input.isAutoInvest());

                    long amount = 0;
                    List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(input.getId());
                    for (InvestRepayModel investRepayModel : investRepayModels) {
                        amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                    }

                    if (CollectionUtils.isEmpty(investRepayModels)) {
                        amount = investService.estimateInvestIncome(input.getLoanId(), input.getAmount());
                    }

                    item.setExpectedInterest(AmountConverter.convertCentToString(amount));
                    item.setCreatedTime(input.getCreatedTime());
                    return item;
                }
            });
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        BasePaginationDataDto<InvestPaginationItemDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        baseDto.setData(dataDto);
        dataDto.setStatus(true);
        return baseDto;
    }

    @Override
    public BaseDto<BasePaginationDataDto> getLoanerLoanData(String loginName, int index, int pageSize, LoanStatus status, Date startTime, Date endTime) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate();
        }

        List<LoanModel> loanModels = Lists.newArrayList();
        long count = 0;
        if (LoanStatus.REPAYING == status) {
            count = loanMapper.findCountRepayingByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findRepayingPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.COMPLETE == status) {
            count = loanMapper.findCountCompletedByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCompletedPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.CANCEL == status) {
            count = loanMapper.findCountCanceledByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCanceledPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        List<LoanPaginationItemDataDto> records = Lists.transform(loanModels, new Function<LoanModel, LoanPaginationItemDataDto>() {
            @Override
            public LoanPaginationItemDataDto apply(LoanModel input) {
                return new LoanPaginationItemDataDto(input);
            }
        });

        BasePaginationDataDto<LoanPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @Override
    @Transactional
    public BaseDto<PayDataDto> loanOut(LoanDto loanDto) throws BaseException {
        this.checkLoanAmount(loanDto.getId());

        this.updateLoanAndLoanTitleRelation(loanDto);

        // 如果存在未处理完成的记录，则不允许放款
        // 放款并记账，同时生成还款计划，处理推荐人奖励，处理短信和邮件通知
        return processLoanOutPayRequest(loanDto.getId());
    }

    private BaseDto<PayDataDto> processLoanOutPayRequest(long loanId) throws BaseException {
        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(loanId));
        BaseDto<PayDataDto> dto = payWrapperClient.loanOut(loanOutDto);
        if (dto.isSuccess()) {
            PayDataDto data = dto.getData();
            if (!data.getStatus()) {
                logger.error(MessageFormat.format("放款失败: {0}", dto.getData().getMessage()));
                throw new BaseException("放款失败");
            }
        }
        return dto;
    }

    @Override
    public BaseDto<PayDataDto> cancelLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = loanParamValidate(loanDto);
        PayDataDto payDataDto = new PayDataDto();
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        Date validInvestTime = new DateTime().minusSeconds(UmpayConstants.TIMEOUT_IN_SECOND_PROJECT_TRANSFER).toDate();
        int waitingInvestCount = investMapper.findWaitingInvestCountAfter(loanDto.getId(), validInvestTime);
        if (waitingInvestCount > 0) {
            logger.debug("流标失败，存在等待第三方资金托管确认的投资!");
            payDataDto.setStatus(false);
            baseDto.setData(payDataDto);
            return baseDto;
        }
        investMapper.cleanWaitingInvestBefore(loanDto.getId(), validInvestTime);
        return payWrapperClient.cancelLoan(loanDto.getId());
    }

    @Override
    public int findLoanListCount(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime) {
        return loanMapper.findLoanListCount(status, loanId, loanName, startTime, endTime);
    }


    @Override
    public List<LoanListDto> findLoanList(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize) {
        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanList(status, loanId, loanName, startTime, endTime, currentPageNo, pageSize);
        List<LoanListDto> loanListDtos = Lists.newArrayList();
        for (int i = 0; i < loanModels.size(); i++) {
            LoanModel loanModel = loanModels.get(i);
            LoanListDto loanListDto = new LoanListDto();
            loanListDto.setId(loanModel.getId());
            loanListDto.setName(loanModel.getName());
            loanListDto.setType(loanModel.getType());
            loanListDto.setAgentLoginName(loanModel.getAgentLoginName());
            loanListDto.setLoanerUserName(loanModel.getLoanerUserName());
            loanListDto.setLoanAmount(loanModel.getLoanAmount());
            loanListDto.setPeriods(loanModel.getPeriods());
            loanListDto.setBasicRate(String.valueOf(new BigDecimal(loanModel.getBaseRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP)) + "%");
            loanListDto.setActivityRate(String.valueOf(new BigDecimal(loanModel.getActivityRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP)) + "%");
            loanListDto.setStatus(loanModel.getStatus());
            loanListDto.setCreatedTime(loanModel.getCreatedTime());
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }

    @Override
    public List<LoanListWebDto> findLoanListWeb(ActivityType activityType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd, int currentPageNo) {

        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanListWeb(activityType, status, periodsStart, periodsEnd, rateStart,
                rateEnd, currentPageNo);
        List<LoanListWebDto> loanListWebDtos = Lists.newArrayList();
        String added;
        for (int i = 0; i < loanModels.size(); i++) {
            LoanListWebDto loanListWebDto = new LoanListWebDto();
            loanListWebDto.setId(loanModels.get(i).getId());
            loanListWebDto.setName(loanModels.get(i).getName());
            loanListWebDto.setBaseRate(new BigDecimal(String.valueOf(loanModels.get(i).getBaseRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            if (loanModels.get(i).getActivityRate() > 0) {
                loanListWebDto.setActivityRate(new BigDecimal(String.valueOf(loanModels.get(i).getActivityRate())).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
            }
            loanListWebDto.setPeriods(loanModels.get(i).getPeriods());
            loanListWebDto.setType(loanModels.get(i).getType());
            loanListWebDto.setStatus(loanModels.get(i).getStatus());
            loanListWebDto.setLoanAmount(new BigDecimal(loanModels.get(i).getLoanAmount()).toString());
            loanListWebDto.setActivityType(loanModels.get(i).getActivityType());
            if (loanModels.get(i).getStatus() == LoanStatus.PREHEAT) {
                if (DateUtil.differenceMinute(new Date(), loanModels.get(i).getFundraisingStartTime()) < 30) {
                    added = String.valueOf(DateUtil.differenceMinute(new Date(), loanModels.get(i).getFundraisingStartTime())) + " 分钟后";
                } else {
                    added = new DateTime(loanModels.get(i).getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm");
                }
            } else if (loanModels.get(i).getStatus() == LoanStatus.RAISING || loanModels.get(i).getStatus() == LoanStatus.RECHECK) {
                added = AmountConverter.convertCentToString(loanModels.get(i).getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModels.get(i).getId()));
                BigDecimal b1 = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModels.get(i).getId()));
                BigDecimal b2 = new BigDecimal(loanModels.get(i).getLoanAmount());
                loanListWebDto.setRateOfAdvance(String.valueOf(b1.divide(b2, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue()));
            } else {
                added = loanRepayMapper.sumSuccessLoanRepayMaxPeriod(loanModels.get(i).getId()) + "/" + loanModels.get(i).getPeriods();
                BigDecimal b1 = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModels.get(i).getId()));
                BigDecimal b2 = new BigDecimal(loanModels.get(i).getLoanAmount());
                loanListWebDto.setRateOfAdvance(String.valueOf(b1.divide(b2, 2, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue()));
            }
            loanListWebDto.setAdded(added);
            loanListWebDtos.add(loanListWebDto);
        }
        return loanListWebDtos;
    }

    @Override
    public int findLoanListCountWeb(ActivityType activityType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd) {
        return loanMapper.findLoanListCountWeb(activityType, status, periodsStart, periodsEnd, rateStart, rateEnd);
    }

    private void createDeadLineFundraisingJob(LoanModel loanModel) {
        try {
            jobManager.newJob(JobType.LoanStatusToRecheck, DeadlineFundraisingJob.class)
                    .withIdentity(JobType.LoanStatusToRecheck.name(), "Loan-" + loanModel.getId())
                    .replaceExistingJob(true)
                    .addJobData("loanId", loanModel.getId())
                    .runOnceAt(loanModel.getFundraisingEndTime()).submit();
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private boolean checkLoanAmount(long loanId) {
        BaseDto<PayDataDto> dto = payWrapperClient.checkLoanAmount(loanId);

        if (!dto.getData().getStatus()) {
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("标的({0})投资金额与募集金额不符，放款失败。", String.valueOf(loanId))));
        }
        return dto.getData().getStatus();
    }
}
