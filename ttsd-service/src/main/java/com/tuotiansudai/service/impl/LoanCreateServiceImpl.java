package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.cfca.service.AnxinSignConnectService;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.job.AutoInvestJob;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AnxinSignService;
import com.tuotiansudai.service.LoanCreateService;
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

import java.util.Date;
import java.util.List;

@Service
public class LoanCreateServiceImpl implements LoanCreateService {

    static Logger logger = Logger.getLogger(LoanCreateServiceImpl.class);

    @Value("${console.auto.invest.delay.minutes}")
    private int autoInvestDelayMinutes;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanTitleMapper loanTitleMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private LoanerEnterpriseDetailsMapper loanerEnterpriseDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private PledgeEnterpriseMapper pledgeEnterpriseMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AnxinSignService anxinSignService;

    @Override
    public LoanTitleModel createTitle(LoanTitleDto loanTitleDto) {
        LoanTitleModel loanTitleModel = new LoanTitleModel(idGenerator.generate(), LoanTitleType.NEW_TITLE_TYPE, loanTitleDto.getTitle());
        loanTitleMapper.create(loanTitleModel);
        return loanTitleModel;
    }

    @Override
    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

    @Override
    @Transactional
    public BaseDto<BaseDataDto> createLoan(LoanCreateRequestDto loanCreateRequestDto) {
        BaseDto<BaseDataDto> dto = this.checkCreateLoanData(loanCreateRequestDto);
        if (!dto.getData().getStatus()) {
            return dto;
        }

        long loanId = idGenerator.generate();

        LoanModel loanModel = new LoanModel(loanId, loanCreateRequestDto);
        loanMapper.create(loanModel);

        if (CollectionUtils.isNotEmpty(loanCreateRequestDto.getLoan().getLoanTitles())) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanCreateRequestDto.getLoan().getLoanTitles()) {
                loanTitleRelationModel.setLoanId(loanId);
                loanTitleRelationModel.setId(idGenerator.generate());
            }
            loanTitleRelationMapper.create(loanCreateRequestDto.getLoan().getLoanTitles());
        }

        loanDetailsMapper.create(new LoanDetailsModel(loanId, loanCreateRequestDto.getLoanDetails()));
        this.updateExtraRate(loanId, loanCreateRequestDto.getLoanDetails().getExtraRateRuleIds());

        if (loanCreateRequestDto.getLoanerDetails() != null) {
            loanerDetailsMapper.create(new LoanerDetailsModel(loanId, loanCreateRequestDto.getLoanerDetails()));
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null) {
            loanerEnterpriseDetailsMapper.create(new LoanerEnterpriseDetailsModel(loanId, loanCreateRequestDto.getLoanerEnterpriseDetails()));
        }

        if (loanCreateRequestDto.getPledgeHouse() != null) {
            pledgeHouseMapper.create(new PledgeHouseModel(loanId, loanCreateRequestDto.getPledgeHouse()));
        }

        if (loanCreateRequestDto.getPledgeVehicle() != null) {
            pledgeVehicleMapper.create(new PledgeVehicleModel(loanId, loanCreateRequestDto.getPledgeVehicle()));
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null) {
            pledgeEnterpriseMapper.create(new PledgeEnterpriseModel(loanId, loanCreateRequestDto.getPledgeEnterprise()));
        }

        return new BaseDto<>(new BaseDataDto(true));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<BaseDataDto> updateLoan(LoanCreateRequestDto loanCreateRequestDto) {
        BaseDto<BaseDataDto> dto = this.checkCreateLoanData(loanCreateRequestDto);
        if (!dto.getData().getStatus()) {
            return dto;
        }

        long loanId = loanCreateRequestDto.getLoan().getId();
        LoanModel loanModel = loanMapper.findById(loanId);
        boolean fundraisingEndTimeChanged = loanCreateRequestDto.getLoan().getFundraisingEndTime() != loanModel.getFundraisingEndTime();

        loanTitleRelationMapper.delete(loanId);
        if (CollectionUtils.isNotEmpty(loanCreateRequestDto.getLoan().getLoanTitles())) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanCreateRequestDto.getLoan().getLoanTitles()) {
                loanTitleRelationModel.setId(idGenerator.generate());
                loanTitleRelationModel.setLoanId(loanId);
            }
            loanTitleRelationMapper.create(loanCreateRequestDto.getLoan().getLoanTitles());
        }

        loanMapper.update(loanModel.updateLoan(loanCreateRequestDto));

        if (Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT).contains(loanModel.getStatus())) {
            this.updateExtraRate(loanId, loanCreateRequestDto.getLoanDetails().getExtraRateRuleIds());
        } else {
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
            if (!Strings.isNullOrEmpty(loanDetailsModel.getExtraSource())) {
                loanCreateRequestDto.getLoanDetails().setExtraSource(Lists.transform(Lists.newArrayList(loanDetailsModel.getExtraSource().split(",")), new Function<String, Source>() {
                    @Override
                    public Source apply(String input) {
                        return Source.valueOf(input);
                    }
                }));
            }
        }

        loanDetailsMapper.deleteByLoanId(loanId);
        loanDetailsMapper.create(new LoanDetailsModel(loanId, loanCreateRequestDto.getLoanDetails()));

        loanerDetailsMapper.deleteByLoanId(loanId);
        loanerEnterpriseDetailsMapper.deleteByLoanId(loanId);

        if (loanCreateRequestDto.getLoanerDetails() != null) {
            loanerDetailsMapper.create(new LoanerDetailsModel(loanId, loanCreateRequestDto.getLoanerDetails()));
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null) {
            loanerEnterpriseDetailsMapper.create(new LoanerEnterpriseDetailsModel(loanId, loanCreateRequestDto.getLoanerEnterpriseDetails()));
        }

        pledgeHouseMapper.deleteByLoanId(loanId);
        pledgeVehicleMapper.deleteByLoanId(loanId);
        pledgeEnterpriseMapper.deleteByLoanId(loanId);
        if (loanCreateRequestDto.getPledgeHouse() != null) {
            pledgeHouseMapper.create(new PledgeHouseModel(loanId, loanCreateRequestDto.getPledgeHouse()));
        }

        if (loanCreateRequestDto.getPledgeVehicle() != null) {
            pledgeVehicleMapper.create(new PledgeVehicleModel(loanId, loanCreateRequestDto.getPledgeVehicle()));
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null) {
            pledgeEnterpriseMapper.create(new PledgeEnterpriseModel(loanId, loanCreateRequestDto.getPledgeEnterprise()));
        }

        if (fundraisingEndTimeChanged) {
            createDeadLineFundraisingJob(loanModel);
        }
        createFundraisingStartJob(loanModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    private BaseDto<BaseDataDto> checkCreateLoanData(LoanCreateRequestDto loanCreateRequestDto) {
        if (userRoleMapper.findByLoginNameAndRole(loanCreateRequestDto.getLoan().getAgent(), Role.LOANER) == null) {
            return new BaseDto<>(new BaseDataDto(false, "代理用户不存在"));
        }

        if (!anxinSignService.hasAuthed(loanCreateRequestDto.getLoan().getAgent())) {
            return new BaseDto<>(new BaseDataDto(false, "代理/借款 用户未授权安心签"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount()) < AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMinInvestAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "最小投资金额不得大于最大投资金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getLoanAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "最大投资金额不得大于预计出借金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getInvestIncreasingAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getLoanAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "投资递增金额不得大于预计出借金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getInvestIncreasingAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "投资递增金额不得大于最大投资金额"));
        }

        if (loanCreateRequestDto.getLoan().getFundraisingEndTime().before(loanCreateRequestDto.getLoan().getFundraisingStartTime())) {
            return new BaseDto<>(new BaseDataDto(false, "筹款启动时间不得晚于筹款截止时间"));
        }

        if (Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE).contains(loanCreateRequestDto.getLoan().getPledgeType())) {
            if (loanCreateRequestDto.getLoanerDetails() == null) {
                return new BaseDto<>(new BaseDataDto(false, "借款人信息不完整"));
            }

            if (PledgeType.HOUSE == loanCreateRequestDto.getLoan().getPledgeType() && loanCreateRequestDto.getPledgeHouse() == null) {
                return new BaseDto<>(new BaseDataDto(false, "房产抵押信息不完整"));
            }

            if (PledgeType.VEHICLE == loanCreateRequestDto.getLoan().getPledgeType() && loanCreateRequestDto.getPledgeVehicle() == null) {
                return new BaseDto<>(new BaseDataDto(false, "车辆抵押信息不完整"));
            }
        }

        if (PledgeType.ENTERPRISE == loanCreateRequestDto.getLoan().getPledgeType()) {
            if (loanCreateRequestDto.getLoanerEnterpriseDetails() == null) {
                return new BaseDto<>(new BaseDataDto(false, "企业借款人信息不完整"));
            }

            if (loanCreateRequestDto.getPledgeEnterprise() == null) {
                return new BaseDto<>(new BaseDataDto(false, "企业抵押信息不完整"));
            }
        }

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public LoanCreateRequestDto getEditLoanDetails(long loanId) {
        LoanCreateRequestDto loanCreateRequestDto = new LoanCreateRequestDto();

        LoanModel loanModel = loanMapper.findById(loanId);
        loanModel.setLoanTitles(loanTitleRelationMapper.findByLoanId(loanId));

        loanCreateRequestDto.setLoan(new LoanCreateBaseRequestDto(loanModel));

        loanCreateRequestDto.setLoanDetails(new LoanCreateDetailsRequestDto(loanDetailsMapper.getByLoanId(loanId)));

        if (Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE).contains(loanModel.getPledgeType())) {
            loanCreateRequestDto.setLoanerDetails(new LoanCreateLoanerDetailsRequestDto(loanerDetailsMapper.getByLoanId(loanId)));

            if (loanModel.getPledgeType() == PledgeType.HOUSE) {
                loanCreateRequestDto.setPledgeHouse(new LoanCreatePledgeHouseRequestDto(pledgeHouseMapper.getByLoanId(loanId)));
            }

            if (loanModel.getPledgeType() == PledgeType.VEHICLE) {
                loanCreateRequestDto.setPledgeVehicle(new LoanCreatePledgeVehicleRequestDto(pledgeVehicleMapper.getByLoanId(loanId)));
            }
        }

        if (PledgeType.ENTERPRISE == loanModel.getPledgeType()) {
            loanCreateRequestDto.setLoanerEnterpriseDetails(new LoanCreateLoanerEnterpriseDetailsDto(loanerEnterpriseDetailsMapper.getByLoanId(loanId)));
            loanCreateRequestDto.setPledgeEnterprise(new LoanCreatePledgeEnterpriseRequestDto(pledgeEnterpriseMapper.getByLoanId(loanId)));
        }

        return loanCreateRequestDto;
    }

    private void updateExtraRate(final long loanId, List<Long> extraRateIds) {
        extraLoanRateMapper.deleteByLoanId(loanId);

        if (CollectionUtils.isEmpty(extraRateIds)) {
            return;
        }

        extraLoanRateMapper.create(Lists.transform(extraRateIds, new Function<Long, ExtraLoanRateModel>() {
            @Override
            public ExtraLoanRateModel apply(Long extraRateRuleId) {
                ExtraLoanRateRuleModel extraLoanRateRuleModel = extraLoanRateRuleMapper.findById(extraRateRuleId);
                return new ExtraLoanRateModel(loanId,
                        extraRateRuleId,
                        extraLoanRateRuleModel.getMinInvestAmount(),
                        extraLoanRateRuleModel.getMaxInvestAmount(),
                        extraLoanRateRuleModel.getRate());
            }
        }));
    }

    @Override
    public BaseDto<PayDataDto> openLoan(LoanCreateRequestDto loanCreateRequestDto, String ip) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        long loanId = loanCreateRequestDto.getLoan().getId();
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null || loanModel.getStatus() != LoanStatus.WAITING_VERIFY) {
            return baseDto;
        }


        baseDto = payWrapperClient.createLoan(loanId);

        if (baseDto.getData().getStatus()) {
            loanModel.setVerifyLoginName(loanCreateRequestDto.getLoan().getVerifyLoginName());
            loanModel.setVerifyTime(new Date());
            loanModel.setStatus(LoanStatus.PREHEAT);
            loanMapper.update(loanModel);

            createFundraisingStartJob(loanModel);
            createDeadLineFundraisingJob(loanModel);
        }
        return baseDto;
    }

    @Override
    public BaseDto<PayDataDto> delayLoan(LoanCreateRequestDto loanCreateRequestDto) {
        long loanId = loanCreateRequestDto.getLoan().getId();

        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);

        if (loanModel == null || loanCreateRequestDto.getLoan().getFundraisingEndTime().before(loanModel.getFundraisingEndTime())) {
            return baseDto;
        }

        loanModel.setFundraisingEndTime(loanCreateRequestDto.getLoan().getFundraisingEndTime());
        loanModel.setStatus(LoanStatus.RAISING);
        loanModel.setUpdateTime(new Date());
        loanMapper.update(loanModel);

        createDeadLineFundraisingJob(loanModel);
        payDataDto.setStatus(true);

        return baseDto;
    }

    @Override
    public void startRaising(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel != null && LoanStatus.PREHEAT == loanModel.getStatus()) {
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
    public BaseDto<PayDataDto> loanOut(LoanCreateRequestDto loanCreateRequestDto) {
        Long loanId = loanCreateRequestDto.getLoan().getId();

        BaseDto<PayDataDto> dto = payWrapperClient.loanOut(loanId);
        if (dto.getData().getStatus()) {
            LoanModel loanModel = loanMapper.findById(loanId);
            loanModel.setRecheckLoginName(loanCreateRequestDto.getLoan().getRecheckLoginName());
            loanMapper.update(loanModel);
        }
        return dto;
    }

    @Override
    public BaseDto<PayDataDto> cancelLoan(LoanCreateRequestDto loanCreateRequestDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        long loanId = loanCreateRequestDto.getLoan().getId();

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return baseDto;
        }

        Date validInvestTime = new DateTime().minusMinutes(30).toDate();

        if (investMapper.findWaitingInvestCountAfter(loanId, validInvestTime) > 0) {
            logger.debug("流标失败，存在等待第三方资金托管确认的投资!");
            return baseDto;
        }

        investMapper.cleanWaitingInvest(loanId);

        return payWrapperClient.cancelLoan(loanId);
    }

    private void createDeadLineFundraisingJob(LoanModel loanModel) {
        try {
            jobManager.newJob(JobType.LoanStatusToRecheck, DeadlineFundraisingJob.class)
                    .withIdentity(JobType.LoanStatusToRecheck.name(), "Loan-" + loanModel.getId())
                    .replaceExistingJob(true)
                    .addJobData(DeadlineFundraisingJob.LOAN_ID_KEY, String.valueOf(loanModel.getId()))
                    .runOnceAt(loanModel.getFundraisingEndTime()).submit();
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public BaseDto<PayDataDto> applyAuditLoan(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        return new BaseDto<>(payDataDto);
    }
}
