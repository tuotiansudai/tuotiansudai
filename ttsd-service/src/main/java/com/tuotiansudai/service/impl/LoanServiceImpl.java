package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.job.AutoInvestJob;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
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

    private final static String REDIS_KEY_TEMPLATE = "webmobile:{0}:{1}:showinvestorname";

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
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

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
    public LoanDetailDto getLoanDetail(String loginName, long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return null;
        }

        LoanDetailDto loanDto = convertModelToDto(loanModel, loginName);
        loanDto.setStatus(true);
        return loanDto;
    }

    private LoanDetailDto convertModelToDto(LoanModel loanModel, String loginName) {
        long investedAmount = investMapper.sumSuccessInvestAmount(loanModel.getId());
        LoanDetailDto loanDto = new LoanDetailDto();
        loanDto.setId(loanModel.getId());
        loanDto.setName(loanModel.getName());
        loanDto.setProgress(new BigDecimal(investedAmount).divide(new BigDecimal(loanModel.getLoanAmount()), 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue());
        loanDto.setBasicRate(new BigDecimal(loanModel.getBaseRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        loanDto.setActivityRate(new BigDecimal(loanModel.getActivityRate()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        loanDto.setLoanAmount(loanModel.getLoanAmount());
        loanDto.setAgentLoginName(loanModel.getAgentLoginName());
        loanDto.setLoanerLoginName(loanModel.getLoanerLoginName());
        loanDto.setPeriods(loanModel.getPeriods());
        loanDto.setType(loanModel.getType());
        loanDto.setMinInvestAmount(loanModel.getMinInvestAmount());
        loanDto.setInvestIncreasingAmount(loanModel.getInvestIncreasingAmount());
        loanDto.setProductType(loanModel.getProductType());
        loanDto.setLoanStatus(loanModel.getStatus());
        loanDto.setAmountNeedRaised(loanModel.getLoanAmount() - investedAmount);
        loanDto.setMaxInvestAmount(AmountConverter.convertCentToString(loanModel.getMaxInvestAmount()));

        loanDto.setDescriptionHtml(loanModel.getDescriptionHtml());
        loanDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            loanDto.setHasRemindInvestNoPassword(investService.isRemindNoPassword(loginName));
            loanDto.setAutoInvest(accountModel.isAutoInvest());
            loanDto.setInvestNoPassword(accountModel.isNoPasswordInvest());
            long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanModel.getId(), loginName);
            loanDto.setUserBalance(accountModel.getBalance());
            loanDto.setMaxAvailableInvestAmount(AmountConverter.convertCentToString(calculateMaxAvailableInvestAmount(
                    NumberUtils.min(accountModel.getBalance(), loanModel.getLoanAmount() - investedAmount, loanModel.getMaxInvestAmount() - sumSuccessInvestAmount),
                    loanModel.getMinInvestAmount(),
                    loanModel.getInvestIncreasingAmount())));
        }
        loanDto.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanModel.getId()));
        loanDto.setLoanTitleDto(loanTitleMapper.findAll());
        if (loanModel.getStatus() == LoanStatus.PREHEAT) {
            loanDto.setPreheatSeconds((loanModel.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000);
        }
        return loanDto;
    }

    private long calculateMaxAvailableInvestAmount(long maxAvailableInvestAmount, long minInvestAmount, long investIncreasingAmount) {
        if (maxAvailableInvestAmount < minInvestAmount) {
            return 0L;
        }
        return maxAvailableInvestAmount - (maxAvailableInvestAmount - minInvestAmount) % investIncreasingAmount;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> openLoan(LoanDto loanDto, String ip) {
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
                    item.setLoginName(encryptLoginName(loginName, input.getLoginName(), 6, input.getId()));
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
    public String encryptLoginName(String loginName, String investorLoginName, int showLength, long investId) {
        if (investorLoginName.equalsIgnoreCase(loginName)) {
            return investorLoginName;
        }

        String redisKey = MessageFormat.format(REDIS_KEY_TEMPLATE, String.valueOf(investId), investorLoginName);

        if (showRandomLoginNameList.contains(investorLoginName) && !redisWrapperClient.exists(redisKey)) {
            redisWrapperClient.set(redisKey, RandomUtils.generateLowerString(3) + RandomUtils.showChar(showLength));
        }

        String encryptLoginName = investorLoginName.substring(0, 3) + RandomUtils.showChar(showLength);

        return redisWrapperClient.exists(redisKey) ? redisWrapperClient.get(redisKey) :encryptLoginName;
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
    public BaseDto<PayDataDto> loanOut(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = this.checkLoanAmount(loanDto.getId());
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }

        // 如果存在未处理完成的记录，则不允许放款
        // 放款并记账，同时生成还款计划，处理推荐人奖励，处理短信和邮件通知
        baseDto = processLoanOutPayRequest(loanDto.getId());
        if (baseDto.getData().getStatus()) {
            loanDto.setLoanStatus(LoanStatus.REPAYING);
            this.updateLoanAndLoanTitleRelation(loanDto);
            return baseDto;
        }

        return baseDto;
    }

    private BaseDto<PayDataDto> processLoanOutPayRequest(long loanId) {
        LoanOutDto loanOutDto = new LoanOutDto();
        loanOutDto.setLoanId(String.valueOf(loanId));
        BaseDto<PayDataDto> dto = payWrapperClient.loanOut(loanOutDto);
        if (dto.isSuccess() && !dto.getData().getStatus()) {
            logger.error(MessageFormat.format("放款失败: {0}", dto.getData().getMessage()));
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
        investMapper.cleanWaitingInvest(loanDto.getId());
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
            loanListDto.setProductType(loanModel.getProductType());
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }

    @Override
    public List<LoanItemDto> findLoanItems(ProductType productType, LoanStatus status, double rateStart, double rateEnd, int index) {
        index = (index - 1) * 10;

        List<LoanModel> loanModels = loanMapper.findLoanListWeb(productType, status, rateStart, rateEnd, index);

        return Lists.transform(loanModels, new Function<LoanModel, LoanItemDto>() {
            @Override
            public LoanItemDto apply(LoanModel loanModel) {
                LoanItemDto loanItemDto = new LoanItemDto();
                loanItemDto.setId(loanModel.getId());
                loanItemDto.setName(loanModel.getName());
                loanItemDto.setProductType(loanModel.getProductType());
                loanItemDto.setBaseRate(new BigDecimal(loanModel.getBaseRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                loanItemDto.setActivityRate(new BigDecimal(loanModel.getActivityRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                loanItemDto.setPeriods(loanModel.getPeriods());
                loanItemDto.setType(loanModel.getType());
                loanItemDto.setStatus(loanModel.getStatus());
                loanItemDto.setLoanAmount(loanModel.getLoanAmount());
                loanItemDto.setActivityType(loanModel.getActivityType());
                BigDecimal loanAmountBigDecimal = new BigDecimal(loanModel.getLoanAmount());
                BigDecimal sumInvestAmountBigDecimal = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModel.getId()));
                if (LoanStatus.PREHEAT == loanModel.getStatus()) {
                    long intervalMinutes = new Duration(new Date().getTime(), loanModel.getFundraisingStartTime().getTime()).getStandardMinutes();
                    if (intervalMinutes < 30) {
                        loanItemDto.setAlert(MessageFormat.format("{0}分钟后 放标", String.valueOf(intervalMinutes)));
                    } else {
                        loanItemDto.setAlert(MessageFormat.format("{0} 放标", new DateTime(loanModel.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm")));
                    }
                    loanItemDto.setProgress(0.0);
                }
                if (LoanStatus.RAISING == loanModel.getStatus()) {
                    loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                    loanItemDto.setProgress(sumInvestAmountBigDecimal.divide(loanAmountBigDecimal, 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue());
                }
                if (LoanStatus.RECHECK == loanModel.getStatus()) {
                    loanItemDto.setAlert("放款审核");
                    loanItemDto.setProgress(100);
                }
                if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE).contains(loanModel.getStatus())) {
                    loanItemDto.setAlert(MessageFormat.format("还款进度：{0}/{1}期", loanRepayMapper.sumSuccessLoanRepayMaxPeriod(loanModel.getId()), loanModel.calculateLoanRepayTimes()));
                    loanItemDto.setProgress(100);
                }

                return loanItemDto;
            }
        });
    }

    @Override
    public int findLoanListCountWeb(ProductType productType, LoanStatus status, double rateStart, double rateEnd) {
        return loanMapper.findLoanListCountWeb(productType, status, rateStart, rateEnd);
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

    private BaseDto<PayDataDto> checkLoanAmount(long loanId) {
        BaseDto<PayDataDto> dto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        dto.setData(payDataDto);

        if (!payWrapperClient.checkLoanAmount(loanId).getData().getStatus()) {
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("标的({0})投资金额与募集金额不符，放款失败。", String.valueOf(loanId))));
            payDataDto.setStatus(false);
            payDataDto.setMessage(MessageFormat.format("放款失败: {0}", "标的投资金额与募集金额不符"));
        }
        return dto;
    }

    @Override
    public BaseDto<PayDataDto> applyAuditLoan(LoanDto loanDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        baseDto.setSuccess(true);
        payDataDto.setStatus(true);
        return baseDto;
    }
}
