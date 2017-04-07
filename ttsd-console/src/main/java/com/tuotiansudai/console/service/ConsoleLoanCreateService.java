package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleLoanCreateService {

    private static Logger logger = Logger.getLogger(ConsoleLoanCreateService.class);

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
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    @Autowired
    private LoanerEnterpriseDetailsMapper loanerEnterpriseDetailsMapper;

    @Autowired
    private PledgeHouseMapper pledgeHouseMapper;

    @Autowired
    private PledgeVehicleMapper pledgeVehicleMapper;

    @Autowired
    private PledgeEnterpriseMapper pledgeEnterpriseMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private AnxinSignPropertyMapper anxinSignPropertyMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

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
            loanCreateRequestDto.getLoanDetails().setExtraSource(loanDetailsModel.getExtraSource());
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
            DelayMessageDeliveryJobCreator.createOrReplaceStopRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingEndTime());
        }
        DelayMessageDeliveryJobCreator.createOrReplaceStartRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingStartTime());

        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanTitleModel createTitle(LoanTitleDto loanTitleDto) {
        LoanTitleModel loanTitleModel = new LoanTitleModel(idGenerator.generate(), LoanTitleType.NEW_TITLE_TYPE, loanTitleDto.getTitle());
        loanTitleMapper.create(loanTitleModel);
        return loanTitleModel;
    }

    public List<LoanTitleModel> findAllTitles() {
        return loanTitleMapper.findAll();
    }

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

            DelayMessageDeliveryJobCreator.createOrReplaceStartRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingStartTime());
            DelayMessageDeliveryJobCreator.createOrReplaceStopRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingEndTime());
        }
        return baseDto;
    }

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

        DelayMessageDeliveryJobCreator.createOrReplaceStopRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingEndTime());
        payDataDto.setStatus(true);

        return baseDto;
    }

    public BaseDto<PayDataDto> applyAuditLoan(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        return new BaseDto<>(payDataDto);
    }

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
            logger.info("流标失败，存在等待第三方资金托管确认的投资!");
            return baseDto;
        }

        investMapper.cleanWaitingInvest(loanId);

        return payWrapperClient.cancelLoan(loanId);
    }

    private BaseDto<BaseDataDto> checkCreateLoanData(LoanCreateRequestDto loanCreateRequestDto) {
        if (userRoleMapper.findByLoginNameAndRole(loanCreateRequestDto.getLoan().getAgent(), Role.LOANER) == null) {
            return new BaseDto<>(new BaseDataDto(false, "代理用户不存在"));
        }

        if (loanCreateRequestDto.getLoan().getOriginalDuration() < 1) {
            return new BaseDto<>(new BaseDataDto(false, "原借款期限不能小于1天"));
        }

        if (loanCreateRequestDto.getLoan().getDeadline() == null || loanCreateRequestDto.getLoan().getDeadline().before(new Date())) {
            return new BaseDto<>(new BaseDataDto(false, "借款截止时间不能为过去的时间"));
        }

        if (!Lists.newArrayList(LoanType.INVEST_INTEREST_MONTHLY_REPAY, LoanType.INVEST_INTEREST_LUMP_SUM_REPAY).contains(loanCreateRequestDto.getLoan().getLoanType())) {
            return new BaseDto<>(new BaseDataDto(false, "标的类型不正确"));
        }

        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loanCreateRequestDto.getLoan().getAgent());
        if (anxinProp == null || !anxinProp.isSkipAuth()) {
            return new BaseDto<>(new BaseDataDto(false, "代理/借款 用户未开通安心签免短信验证"));
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

    private void updateExtraRate(final long loanId, List<Long> extraRateIds) {
        extraLoanRateMapper.deleteByLoanId(loanId);

        if (CollectionUtils.isEmpty(extraRateIds)) {
            return;
        }

        extraLoanRateMapper.create(extraRateIds.stream().map(extraRateId -> {
            ExtraLoanRateRuleModel extraLoanRateRuleModel = extraLoanRateRuleMapper.findById(extraRateId);
            return new ExtraLoanRateModel(loanId,
                    extraRateId,
                    extraLoanRateRuleModel.getMinInvestAmount(),
                    extraLoanRateRuleModel.getMaxInvestAmount(),
                    extraLoanRateRuleModel.getRate());
        }).collect(Collectors.toList()));
    }
}
