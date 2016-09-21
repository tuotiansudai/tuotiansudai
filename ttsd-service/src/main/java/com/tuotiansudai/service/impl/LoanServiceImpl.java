package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.job.AutoInvestJob;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private CouponMapper couponMapper;

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private CouponService couponService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;


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

    private BaseDto<BaseDataDto> loanCheck(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                           AbstractPledgeDetailsDto pledgeDetailsDto) {
        long minInvestAmount = AmountConverter.convertStringToCent(loanDto.getMinInvestAmount());
        long maxInvestAmount = AmountConverter.convertStringToCent(loanDto.getMaxInvestAmount());
        long loanAmount = AmountConverter.convertStringToCent(loanDto.getLoanAmount());
        String agentLoginName = getLoginName(loanDto.getAgentLoginName());
        if (agentLoginName == null) {
            return new BaseDto<>(new BaseDataDto(false, "代理用户不存在"));
        } else {
            if (userRoleMapper.findByLoginNameAndRole(loanDto.getAgentLoginName(), Role.LOANER.name()) == null) {
                return new BaseDto<>(new BaseDataDto(false, "代理用户不具有借款人角色"));
            }
        }
        if (loanDto.getPeriods() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "借款期限最小为1"));
        }
        if (loanAmount <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "预计出借金额应大于0"));
        }
        if (minInvestAmount <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "最小投资金额应大于0"));
        }
        if (maxInvestAmount < minInvestAmount) {
            return new BaseDto<>(new BaseDataDto(false, "最小投资金额不得大于最大投资金额"));
        }
        if (maxInvestAmount > loanAmount) {
            return new BaseDto<>(new BaseDataDto(false, "最大投资金额不得大于预计出借金额"));
        }
        if (loanDto.getFundraisingEndTime().before(loanDto.getFundraisingStartTime())) {
            return new BaseDto<>(new BaseDataDto(false, "筹款启动时间不得晚于筹款截止时间"));
        }
        if (StringUtils.isEmpty(loanDetailsDto.getDeclaration())) {
            return new BaseDto<>(new BaseDataDto(false, "声明为空"));
        }
        if (StringUtils.isEmpty(loanerDetailsDto.getLoanerUserName()) || StringUtils.isEmpty(loanerDetailsDto.getLoanerEmploymentStatus()) ||
                StringUtils.isEmpty(loanerDetailsDto.getLoanerIncome()) || StringUtils.isEmpty(loanerDetailsDto.getLoanerRegion()) ||
                StringUtils.isEmpty(loanerDetailsDto.getLoanerIdentityNumber())) {
            return new BaseDto<>(new BaseDataDto(false, "借款人信息不完善"));
        }
        if (pledgeDetailsDto instanceof PledgeHouseDto) {
            PledgeHouseDto pledgeHouseDto = (PledgeHouseDto) pledgeDetailsDto;
            if (StringUtils.isEmpty(pledgeHouseDto.getPledgeLocation()) || StringUtils.isEmpty(pledgeHouseDto.getLoanAmount()) ||
                    StringUtils.isEmpty(pledgeHouseDto.getEstimateAmount()) || StringUtils.isEmpty(pledgeHouseDto.getSquare()) ||
                    StringUtils.isEmpty(pledgeHouseDto.getAuthenticAct()) || StringUtils.isEmpty(pledgeHouseDto.getEstateRegisterId()) ||
                    StringUtils.isEmpty(pledgeHouseDto.getPropertyCardId())) {
                return new BaseDto<>(new BaseDataDto(false, "房屋抵押物信息不完善"));
            }
        } else if (pledgeDetailsDto instanceof PledgeVehicleDto) {
            PledgeVehicleDto pledgeVehicleDto = (PledgeVehicleDto) pledgeDetailsDto;
            if (StringUtils.isEmpty(pledgeVehicleDto.getPledgeLocation()) || StringUtils.isEmpty(pledgeVehicleDto.getLoanAmount()) ||
                    StringUtils.isEmpty(pledgeVehicleDto.getEstimateAmount()) || StringUtils.isEmpty(pledgeVehicleDto.getBrand()) ||
                    StringUtils.isEmpty(pledgeVehicleDto.getModel())) {
                return new BaseDto<>(new BaseDataDto(false, "车辆抵押物信息不完善"));
            }
        } else {
            return new BaseDto<>(new BaseDataDto(false, "抵押物类型错误"));
        }
        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<BaseDataDto> createLoan(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                           AbstractPledgeDetailsDto pledgeDetailsDto) {
        BaseDto<BaseDataDto> baseDto = loanCheck(loanDto, loanDetailsDto, loanerDetailsDto, pledgeDetailsDto);
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }

        long loanId = idGenerator.generate();/****标的号****/
        loanDto.setId(loanId);
        loanDetailsDto.setLoanId(loanId);
        loanerDetailsDto.setLoanId(loanId);
        pledgeDetailsDto.setLoanId(loanId);
        for (LoanTitleRelationModel loanTitleRelationModel : loanDto.getLoanTitles()) {
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanId);
        }

        loanMapper.create(new LoanModel(loanDto));
        if (!CollectionUtils.isEmpty(loanDto.getLoanTitles())) {
            loanTitleRelationMapper.create(loanDto.getLoanTitles());
        }
        loanDetailsMapper.create(new LoanDetailsModel(loanDetailsDto));
        loanerDetailsMapper.create(new LoanerDetailsModel(loanerDetailsDto));
        if (pledgeDetailsDto instanceof PledgeHouseDto) {
            pledgeHouseMapper.create(new PledgeHouseModel((PledgeHouseDto) pledgeDetailsDto));
        } else if (pledgeDetailsDto instanceof PledgeVehicleDto) {
            pledgeVehicleMapper.create(new PledgeVehicleModel((PledgeVehicleDto) pledgeDetailsDto));
        }

        baseDto.getData().setMessage(String.valueOf(loanId));
        updateExtraRate(loanDto, loanId);
        return baseDto;
    }

    private void updateExtraRate(LoanDto loanDto, final long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() != LoanStatus.WAITING_VERIFY) {
            return;
        } else {
            extraLoanRateMapper.deleteByLoanId(loanId);
            List<Long> extraRateIds = loanDto.getExtraRateIds();
            if (CollectionUtils.isNotEmpty(extraRateIds)) {
                extraLoanRateMapper.create(Lists.transform(extraRateIds, new Function<Long, ExtraLoanRateModel>() {
                    @Override
                    public ExtraLoanRateModel apply(Long input) {
                        ExtraLoanRateModel extraLoanRateModel = new ExtraLoanRateModel();
                        ExtraLoanRateRuleModel extraLoanRateRuleModel = extraLoanRateRuleMapper.findById(input);
                        extraLoanRateModel.setLoanId(loanId);
                        extraLoanRateModel.setExtraRateRuleId(input);
                        extraLoanRateModel.setMaxInvestAmount(extraLoanRateRuleModel.getMaxInvestAmount());
                        extraLoanRateModel.setMinInvestAmount(extraLoanRateRuleModel.getMinInvestAmount());
                        extraLoanRateModel.setRate(extraLoanRateRuleModel.getRate());
                        return extraLoanRateModel;
                    }
                }));
            }
        }
    }

    private String getLoginName(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel != null) {
            return accountModel.getPayUserId();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> openLoan(LoanDto loanDto, String ip) {
        // TODO:fake
        if (loanDto.getId() == 41650602422768L) {
            loanDto.setLoanStatus(LoanStatus.PREHEAT);
            loanDto.setVerifyTime(new Date());
            updateAllLoan(loanDto);
            PayDataDto payDataDto = new PayDataDto();
            payDataDto.setStatus(true);
            createFundraisingStartJob(loanMapper.findById(loanDto.getId()));
            createDeadLineFundraisingJob(loanMapper.findById(loanDto.getId()));
            return new BaseDto<>(payDataDto);
        }

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
                    updateAllLoan(loanDto);

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
    public BaseDto<BaseDataDto> updateLoan(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                           AbstractPledgeDetailsDto pledgeDetailsDto) {
        BaseDto<BaseDataDto> baseDto = loanCheck(loanDto, loanDetailsDto, loanerDetailsDto, pledgeDetailsDto);
        if (!baseDto.getData().getStatus()) {
            return baseDto;
        }
        LoanModel loanModelBefore = loanMapper.findById(loanDto.getId());
        if (null == loanModelBefore) {
            baseDto.getData().setStatus(false);
            baseDto.getData().setMessage("标的不存在");
            return baseDto;
        }
        if (loanDto.getId() != loanDetailsDto.getLoanId() || loanDto.getId() != loanerDetailsDto.getLoanId() || loanDto.getId() != pledgeDetailsDto.getLoanId()) {
            baseDto.getData().setStatus(false);
            baseDto.getData().setMessage("loanID不一致");
            return baseDto;
        }
        if (!((PledgeType.HOUSE == loanDto.getPledgeType() && pledgeDetailsDto instanceof PledgeHouseDto) ||
                (PledgeType.VEHICLE == loanDto.getPledgeType() && pledgeDetailsDto instanceof PledgeVehicleDto))) {
            baseDto.getData().setStatus(false);
            baseDto.getData().setMessage("抵押物类型与抵押物详细信息不一致");
            return baseDto;
        }

        //修改抵押物类型的标的需要删除原有的抵押物类型
        if (PledgeType.HOUSE == loanDto.getPledgeType()) {
            pledgeVehicleMapper.deleteByLoanId(loanDto.getId());
        } else if (PledgeType.VEHICLE == loanDto.getPledgeType()) {
            pledgeHouseMapper.deleteByLoanId(loanDto.getId());
        }

        updateLoanWithoutStatus(loanDto, loanDetailsDto, loanerDetailsDto, pledgeDetailsDto);
        LoanModel loanModelAfter = loanMapper.findById(loanDto.getId());
        if (loanModelBefore.getFundraisingEndTime() != loanModelAfter.getFundraisingEndTime()) {
            createDeadLineFundraisingJob(loanModelAfter);
        }
        createFundraisingStartJob(loanModelAfter);
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
    public AbstractCreateLoanDto findCreateLoanDto(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<LoanTitleRelationModel> loanTitleRelationModelList = loanTitleRelationMapper.findByLoanId(loanId);
        loanModel.setLoanTitles(loanTitleRelationModelList);
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(loanId);
        LoanerDetailsModel loanerDetailsModel = loanerDetailsMapper.getLoanerDetailByLoanId(loanId);
        AbstractCreateLoanDto createLoanDto = null;
        if (PledgeType.HOUSE == loanModel.getPledgeType()) {
            AbstractPledgeDetail pledgeDetail = pledgeHouseMapper.getPledgeHouseDetailByLoanId(loanId);
            createLoanDto = new CreateHouseLoanDto(loanModel, loanDetailsModel, loanerDetailsModel, (PledgeHouseModel) pledgeDetail);
        } else if (PledgeType.VEHICLE == loanModel.getPledgeType()) {
            AbstractPledgeDetail pledgeDetail = pledgeVehicleMapper.getPledgeVehicleDetailByLoanId(loanId);
            createLoanDto = new CreateVehicleLoanDto(loanModel, loanDetailsModel, loanerDetailsModel, (PledgeVehicleModel) pledgeDetail);
        } else {
            return null;
        }
        createLoanDto.setExtraSource(loanDetailsModel != null?loanDetailsModel.getExtraSource():"");
        return createLoanDto;
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

    private void updateAllLoan(LoanDto loanDto) {
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanDto.getLoanStatus());
        loanMapper.update(loanModel);
        updateLoanTitleAndExtraRate(loanDto, loanModel);
    }

    private void updateLoanWithoutStatus(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                         AbstractPledgeDetailsDto pledgeDetailsDto) {
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanDto.getLoanStatus());
        loanMapper.updateWithoutStatus(loanModel);

        loanDetailsMapper.updateByLoanId(new LoanDetailsModel(loanDetailsDto));
        loanerDetailsMapper.updateByLoanId(new LoanerDetailsModel(loanerDetailsDto));
        if (pledgeDetailsDto instanceof PledgeHouseDto) {
            if (null == pledgeHouseMapper.getPledgeHouseDetailByLoanId(loanDto.getId())) {
                pledgeHouseMapper.create(new PledgeHouseModel((PledgeHouseDto) pledgeDetailsDto));
            } else {
                pledgeHouseMapper.updateByLoanId(new PledgeHouseModel((PledgeHouseDto) pledgeDetailsDto));
            }
        } else if (pledgeDetailsDto instanceof PledgeVehicleDto) {
            if (null == pledgeVehicleMapper.getPledgeVehicleDetailByLoanId(loanDto.getId())) {
                pledgeVehicleMapper.create(new PledgeVehicleModel((PledgeVehicleDto) pledgeDetailsDto));
            } else {
                pledgeVehicleMapper.updateByLoanId(new PledgeVehicleModel((PledgeVehicleDto) pledgeDetailsDto));
            }
        }

        updateLoanTitleAndExtraRate(loanDto, loanModel);
    }

    private void updateLoanTitleAndExtraRate(LoanDto loanDto, LoanModel loanModel) {
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
        updateExtraRate(loanDto, loanModel.getId());
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
                int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findRepayingPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.COMPLETE == status) {
            count = loanMapper.findCountCompletedByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                loanModels = loanMapper.findCompletedPaginationByAgentLoginName(loginName, (index - 1) * pageSize, pageSize, startTime, endTime);
            }
        }

        if (LoanStatus.CANCEL == status) {
            count = loanMapper.findCountCanceledByAgentLoginName(loginName, startTime, endTime);
            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
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
            this.updateAllLoan(loanDto);
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
        for (LoanModel loanModel : loanModels) {
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
            loanListDto.setPledgeType(loanModel.getPledgeType());
            List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanModel.getId());
            if (CollectionUtils.isNotEmpty(extraLoanRateModels)) {
                loanListDto.setExtraLoanRateModels(fillExtraLoanRate(extraLoanRateModels));
            }
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(loanModel.getId());
            loanListDto.setExtraSource(loanDetailsModel != null?loanDetailsModel.getExtraSource():"");
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }

    @Override
    public List<LoanItemDto> findLoanItems(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd, int index) {
        index = (index - 1) * 10;

        List<LoanModel> loanModels = loanMapper.findLoanListWeb(name, status, rateStart, rateEnd, durationStart, durationEnd, index);

        final List<CouponModel> allActiveCoupons = couponMapper.findAllActiveCoupons();

        CouponModel newbieInterestCouponModel = null;
        for (CouponModel activeCoupon : allActiveCoupons) {
            if (activeCoupon.getCouponType() == CouponType.INTEREST_COUPON
                    && activeCoupon.getUserGroup() == UserGroup.NEW_REGISTERED_USER
                    && activeCoupon.getProductTypes().contains(ProductType._30)
                    && (newbieInterestCouponModel == null || activeCoupon.getRate() > newbieInterestCouponModel.getRate())) {
                newbieInterestCouponModel = activeCoupon;
            }
        }
        final double newbieInterestCouponRate = newbieInterestCouponModel != null ? newbieInterestCouponModel.getRate() : 0;

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
                loanItemDto.setInterestCouponRate(new BigDecimal(String.valueOf(newbieInterestCouponRate)).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
                BigDecimal loanAmountBigDecimal = new BigDecimal(loanModel.getLoanAmount());
                BigDecimal sumInvestAmountBigDecimal = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModel.getId()));
                if (LoanStatus.PREHEAT == loanModel.getStatus()) {
                    loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                    loanItemDto.setProgress(0.0);
                    loanItemDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());
                    loanItemDto.setPreheatSeconds((loanModel.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000);
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
                    loanItemDto.setAlert(MessageFormat.format("还款进度：{0}/{1}期", loanRepayMapper.sumSuccessLoanRepayMaxPeriod(loanModel.getId()), loanModel.getPeriods()));
                    loanItemDto.setProgress(100);
                }
                if (loanItemDto.getProductType() == ProductType.EXPERIENCE) {
                    Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
                    Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
                    List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loanModel.getId(), beginTime, endTime);
                    long investCount = investModelList.size() % 100;
                    long investAmount = couponService.findExperienceInvestAmount(investModelList);
                    loanItemDto.setAlert(AmountConverter.convertCentToString(loanModel.getLoanAmount() - investAmount));
                    loanItemDto.setProgress(investCount);
                }
                loanItemDto.setDuration(loanModel.getDuration());
                double rate = extraLoanRateMapper.findMaxRateByLoanId(loanModel.getId());
                String extraSource = "";
                String activityDesc = "";
                LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(loanModel.getId());
                if(loanDetailsModel != null){
                    extraSource = loanDetailsModel.getExtraSource();
                    activityDesc = loanDetailsModel.getActivityDesc();
                }
                if (rate > 0) {
                    loanItemDto.setExtraRate(rate * 100);
                    loanItemDto.setExtraSource(extraSource);
                }
                loanItemDto.setActivityDesc(activityDesc);
                return loanItemDto;
            }
        });
    }

    @Override
    public int findLoanListCountWeb(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd) {
        return loanMapper.findLoanListCountWeb(name, status, rateStart, rateEnd, durationStart, durationEnd);
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

    private List<ExtraLoanRateItemDto> fillExtraLoanRate(List<ExtraLoanRateModel> extraLoanRateModels) {
        return Lists.transform(extraLoanRateModels, new Function<ExtraLoanRateModel, ExtraLoanRateItemDto>() {
            @Override
            public ExtraLoanRateItemDto apply(ExtraLoanRateModel model) {
                return new ExtraLoanRateItemDto(model);
            }
        });
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
