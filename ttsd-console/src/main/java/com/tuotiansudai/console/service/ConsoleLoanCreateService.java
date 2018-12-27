package com.tuotiansudai.console.service;

import com.google.common.base.Strings;
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

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleLoanCreateService {

    private static Logger logger = Logger.getLogger(ConsoleLoanCreateService.class);

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
    private LoanerEnterpriseInfoMapper loanerEnterpriseInfoMapper;

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

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private LoanOutTailAfterMapper loanOutTailAfterMapper;

    @Autowired
    private LoanRiskManagementTitleRelationMapper loanRiskManagementTitleRelationMapper;

    protected final static String generateLoanName = "{0}{1}";

    @Transactional
    public BaseDto<BaseDataDto> createLoan(LoanCreateRequestDto loanCreateRequestDto) {
        BaseDto<BaseDataDto> dto = this.checkCreateLoanData(loanCreateRequestDto);
        if (!dto.getData().getStatus()) {
            return dto;
        }

        long loanId = IdGenerator.generate();

        LoanModel loanModel = new LoanModel(loanId, loanCreateRequestDto);
        loanModel.setName(generateLoanName(loanModel.getName(), loanModel.getPledgeType()));

        loanMapper.create(loanModel);

        if (CollectionUtils.isNotEmpty(loanCreateRequestDto.getLoan().getLoanTitles())) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanCreateRequestDto.getLoan().getLoanTitles()) {
                loanTitleRelationModel.setLoanId(loanId);
                loanTitleRelationModel.setId(IdGenerator.generate());
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
            for (int i = 0; i < loanCreateRequestDto.getPledgeHouse().size(); i++) {
                pledgeHouseMapper.create(new PledgeHouseModel(loanId, loanCreateRequestDto.getPledgeHouse().get(i)));
            }
        }

        if (loanCreateRequestDto.getPledgeVehicle() != null) {
            for (int i = 0; i < loanCreateRequestDto.getPledgeVehicle().size(); i++) {
                pledgeVehicleMapper.create(new PledgeVehicleModel(loanId, loanCreateRequestDto.getPledgeVehicle().get(i)));
            }
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null && loanCreateRequestDto.getLoan().getPledgeType() == PledgeType.ENTERPRISE_PLEDGE) {
            if (loanCreateRequestDto.getPledgeEnterprise() != null) {
                for (int i = 0; i < loanCreateRequestDto.getPledgeEnterprise().size(); i++) {
                    pledgeEnterpriseMapper.create(new PledgeEnterpriseModel(loanId, loanCreateRequestDto.getPledgeEnterprise().get(i)));
                }
            }
        }

        if (loanCreateRequestDto.getLoanerEnterpriseInfo() != null) {
            loanerEnterpriseInfoMapper.create(new LoanerEnterpriseInfoModel(loanId, loanCreateRequestDto.getLoanerEnterpriseInfo()));
        }

        if (loanModel.getPledgeType() == PledgeType.NONE && !Strings.isNullOrEmpty(loanCreateRequestDto.getLoanApplicationId())){
            loanApplicationMapper.updateLoanId(Long.parseLong(loanCreateRequestDto.getLoanApplicationId()), loanId);
            loanRiskManagementTitleRelationMapper.updateLoanIdByLoanApplicationId(loanId, Long.parseLong(loanCreateRequestDto.getLoanApplicationId()));
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

        //序号是从年的后两位截取的，所以是1或者2
        String loanNameSeq = getLoanNameSeq(loanModel.getName());
        boolean fundraisingEndTimeChanged = loanCreateRequestDto.getLoan().getFundraisingEndTime() != loanModel.getFundraisingEndTime();

        loanTitleRelationMapper.delete(loanId);
        if (CollectionUtils.isNotEmpty(loanCreateRequestDto.getLoan().getLoanTitles())) {
            for (LoanTitleRelationModel loanTitleRelationModel : loanCreateRequestDto.getLoan().getLoanTitles()) {
                loanTitleRelationModel.setId(IdGenerator.generate());
                loanTitleRelationModel.setLoanId(loanId);
            }
            loanTitleRelationMapper.create(loanCreateRequestDto.getLoan().getLoanTitles());
        }

        loanCreateRequestDto.getLoan().setName(loanModel.getPledgeType() == loanCreateRequestDto.getLoan().getPledgeType() ? loanCreateRequestDto.getLoan().getName() + loanNameSeq : generateLoanName(loanCreateRequestDto.getLoan().getName(), loanCreateRequestDto.getLoan().getPledgeType()));

        loanMapper.update(loanModel.updateLoan(loanCreateRequestDto));

        if (Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.PREHEAT, LoanStatus.DRAFT).contains(loanModel.getStatus())) {
            this.updateExtraRate(loanId, loanCreateRequestDto.getLoanDetails().getExtraRateRuleIds());
        } else {
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
            loanCreateRequestDto.getLoanDetails().setExtraSource(loanDetailsModel.getExtraSource());
        }

        loanDetailsMapper.deleteByLoanId(loanId);
        loanDetailsMapper.create(new LoanDetailsModel(loanId, loanCreateRequestDto.getLoanDetails()));

        loanerDetailsMapper.deleteByLoanId(loanId);
        loanerEnterpriseDetailsMapper.deleteByLoanId(loanId);
        loanerEnterpriseInfoMapper.deleteByLoanId(loanId);

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
            for (int i = 0; i < loanCreateRequestDto.getPledgeHouse().size(); i++) {
                pledgeHouseMapper.create(new PledgeHouseModel(loanId, loanCreateRequestDto.getPledgeHouse().get(i)));
            }
        }

        if (loanCreateRequestDto.getPledgeVehicle() != null) {
            for (int i = 0; i < loanCreateRequestDto.getPledgeVehicle().size(); i++) {
                pledgeVehicleMapper.create(new PledgeVehicleModel(loanId, loanCreateRequestDto.getPledgeVehicle().get(i)));
            }
        }

        if (loanCreateRequestDto.getLoanerEnterpriseDetails() != null && loanCreateRequestDto.getLoan().getPledgeType() == PledgeType.ENTERPRISE_PLEDGE) {
            if (loanCreateRequestDto.getPledgeEnterprise() != null) {
                for (int i = 0; i < loanCreateRequestDto.getPledgeEnterprise().size(); i++) {
                    pledgeEnterpriseMapper.create(new PledgeEnterpriseModel(loanId, loanCreateRequestDto.getPledgeEnterprise().get(i)));
                }
            }
        }

        if (loanCreateRequestDto.getLoanerEnterpriseInfo() != null) {
            loanerEnterpriseInfoMapper.create(new LoanerEnterpriseInfoModel(loanId, loanCreateRequestDto.getLoanerEnterpriseInfo()));
        }

        if (fundraisingEndTimeChanged) {
            DelayMessageDeliveryJobCreator.createOrReplaceStopRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingEndTime());
        }
        DelayMessageDeliveryJobCreator.createOrReplaceStartRaisingDelayJob(jobManager, loanId, loanModel.getFundraisingStartTime());

        return new BaseDto<>(new BaseDataDto(true));
    }

    public LoanTitleModel createTitle(LoanTitleDto loanTitleDto) {
        LoanTitleModel loanTitleModel = new LoanTitleModel(IdGenerator.generate(), LoanTitleType.NEW_TITLE_TYPE, loanTitleDto.getTitle());
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

        if (PledgeType.NONE == loanModel.getPledgeType()){
            loanCreateRequestDto.setLoanerDetails(new LoanCreateLoanerDetailsRequestDto(loanerDetailsMapper.getByLoanId(loanId)));
        }

        if (Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE, PledgeType.PERSONAL_CAPITAL_TURNOVER).contains(loanModel.getPledgeType())) {
            loanCreateRequestDto.setLoanerDetails(new LoanCreateLoanerDetailsRequestDto(loanerDetailsMapper.getByLoanId(loanId)));
            List<LoanCreatePledgeHouseRequestDto> loanCreatePledgeHouseRequestDtoList = pledgeHouseMapper.getByLoanId(loanId).stream()
                    .map(LoanCreatePledgeHouseRequestDto::new).collect(Collectors.toList());
            loanCreateRequestDto.setPledgeHouse(loanCreatePledgeHouseRequestDtoList);

            List<LoanCreatePledgeVehicleRequestDto> loanCreatePledgeVehicleRequestDtoList = pledgeVehicleMapper.getByLoanId(loanId).stream()
                    .map(LoanCreatePledgeVehicleRequestDto::new).collect(Collectors.toList());
            loanCreateRequestDto.setPledgeVehicle(loanCreatePledgeVehicleRequestDtoList);
        }

        if (PledgeType.ENTERPRISE_PLEDGE == loanModel.getPledgeType()) {
            loanCreateRequestDto.setLoanerEnterpriseDetails(new LoanCreateLoanerEnterpriseDetailsDto(loanerEnterpriseDetailsMapper.getByLoanId(loanId)));
            List<LoanCreatePledgeEnterpriseRequestDto> loanCreatePledgeEnterpriseRequestDtoList = pledgeEnterpriseMapper.getByLoanId(loanId).stream()
                    .map(LoanCreatePledgeEnterpriseRequestDto::new).collect(Collectors.toList());
            loanCreateRequestDto.setPledgeEnterprise(loanCreatePledgeEnterpriseRequestDtoList);
        }
        if (PledgeType.ENTERPRISE_CREDIT == loanModel.getPledgeType()) {
            loanCreateRequestDto.setLoanerEnterpriseDetails(new LoanCreateLoanerEnterpriseDetailsDto(loanerEnterpriseDetailsMapper.getByLoanId(loanId)));
        }

        if (PledgeType.ENTERPRISE_FACTORING == loanModel.getPledgeType() || PledgeType.ENTERPRISE_BILL == loanModel.getPledgeType()) {
            loanCreateRequestDto.setLoanerEnterpriseInfo(new LoanCreateLoanerEnterpriseInfoDto(loanerEnterpriseInfoMapper.getByLoanId(loanId)));
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
            logger.info("流标失败，存在等待第三方资金托管确认的出借!");
            return baseDto;
        }

        investMapper.cleanWaitingInvest(loanId);

        return payWrapperClient.cancelLoan(loanId);
    }

    public BaseDto<BaseDataDto> checkCreateLoanData(LoanCreateRequestDto loanCreateRequestDto) {
        if (loanCreateRequestDto.getLoan().getStatus() != LoanStatus.DRAFT && userRoleMapper.findByLoginNameAndRole(loanCreateRequestDto.getLoan().getAgent(), Role.LOANER) == null) {
            return new BaseDto<>(new BaseDataDto(false, "代理用户不存在"));
        }

        if (loanCreateRequestDto.getLoan().getOriginalDuration() < 1) {
            return new BaseDto<>(new BaseDataDto(false, "原借款期限不能小于1天"));
        }

        if (loanCreateRequestDto.getLoan().getPledgeType() != PledgeType.NONE && !loanCreateRequestDto.getLoan().getStatus().equals(LoanStatus.COMPLETE) && (loanCreateRequestDto.getLoan().getDeadline() == null || loanCreateRequestDto.getLoan().getDeadline().before(new Date()))) {
            return new BaseDto<>(new BaseDataDto(false, "借款截止时间不能为过去的时间"));
        }

        AnxinSignPropertyModel anxinProp = anxinSignPropertyMapper.findByLoginName(loanCreateRequestDto.getLoan().getAgent());
        if (anxinProp == null || !anxinProp.isSkipAuth()) {
            return new BaseDto<>(new BaseDataDto(false, "代理/借款 用户未开通安心签免短信验证"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount()) < AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMinInvestAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "最小出借金额不得大于最大出借金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getLoanAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "最大出借金额不得大于预计出借金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getInvestIncreasingAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getLoanAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "出借递增金额不得大于预计出借金额"));
        }

        if (AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getInvestIncreasingAmount()) > AmountConverter.convertStringToCent(loanCreateRequestDto.getLoan().getMaxInvestAmount())) {
            return new BaseDto<>(new BaseDataDto(false, "出借递增金额不得大于最大出借金额"));
        }

        if (loanCreateRequestDto.getLoan().getFundraisingEndTime().before(loanCreateRequestDto.getLoan().getFundraisingStartTime())) {
            return new BaseDto<>(new BaseDataDto(false, "筹款启动时间不得晚于筹款截止时间"));
        }
        if(Lists.newArrayList(LoanStatus.WAITING_VERIFY, LoanStatus.DRAFT).contains(loanCreateRequestDto.getLoan().getStatus())){
            if((loanCreateRequestDto.getLoan().getFundraisingEndTime().getTime()-loanCreateRequestDto.getLoan().getFundraisingStartTime().getTime())> 7*24*60*60*1000){
                return new BaseDto<>(new BaseDataDto(false, "筹款启动时间与筹款截止时间不能超过7天"));
            }
            if(new Date().after(loanCreateRequestDto.getLoan().getFundraisingEndTime())){
                return new BaseDto<>(new BaseDataDto(false, "筹款截止时间不能小于当前时间"));
            }
        }

        if (Lists.newArrayList(PledgeType.HOUSE, PledgeType.VEHICLE, PledgeType.NONE).contains(loanCreateRequestDto.getLoan().getPledgeType())) {
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

        if (PledgeType.ENTERPRISE_CREDIT == loanCreateRequestDto.getLoan().getPledgeType() || PledgeType.ENTERPRISE_PLEDGE == loanCreateRequestDto.getLoan().getPledgeType()) {
            if (loanCreateRequestDto.getLoanerEnterpriseDetails() == null) {
                return new BaseDto<>(new BaseDataDto(false, "企业借款人信息不完整"));
            }
        }

        if (PledgeType.ENTERPRISE_PLEDGE == loanCreateRequestDto.getLoan().getPledgeType()) {
            if (loanCreateRequestDto.getPledgeEnterprise() == null) {
                return new BaseDto<>(new BaseDataDto(false, "企业抵押信息不完整"));
            }
        }

        if (PledgeType.ENTERPRISE_FACTORING == loanCreateRequestDto.getLoan().getPledgeType() || PledgeType.ENTERPRISE_BILL == loanCreateRequestDto.getLoan().getPledgeType()) {
            if (loanCreateRequestDto.getLoanerEnterpriseInfo() == null) {
                return new BaseDto<>(new BaseDataDto(false, "企业借款人信息不完整"));
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

    protected String generateLoanName(String loanName, PledgeType pledgeType) {
        String date = new DateTime().toString("yy");
        String createdTime = new DateTime().toString("yyyy");
        String number = String.format("%03d", loanMapper.findLoanCountByYear(createdTime, pledgeType) + 1);
        return MessageFormat.format(generateLoanName, loanName, date + number);
    }

    private String getLoanNameSeq(String loanName) {
        String loanNameSeq = "";
        if (Strings.isNullOrEmpty(loanName))
            return loanNameSeq;
        if (loanName.contains("1") || loanName.contains("2")) {
            loanNameSeq = loanName.substring(loanName.length() - 5, loanName.length());
        }
        return loanNameSeq;
    }

    public LoanOutTailAfterModel findLoanOutTailAfter(long loanId){
        LoanModel loanModel = loanMapper.findById(loanId);
        LoanOutTailAfterModel loanOutTailAfterModel = null;
        if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE).contains(loanModel.getStatus()) && loanModel.getPledgeType() == PledgeType.NONE){
            loanOutTailAfterModel = loanOutTailAfterMapper.findByLoanId(loanModel.getId());
            if (loanOutTailAfterModel == null){
                loanOutTailAfterModel = new LoanOutTailAfterModel(loanModel.getId(), "良好", "无变化", false, false, "按照借款用途使用");
                loanOutTailAfterMapper.create(loanOutTailAfterModel);
            }
        }
        return loanOutTailAfterModel;
    }

    public LoanOutTailAfterModel updateLoanOutTailAfter(long loanId, String financeState, String repayPower, boolean isOverdue, boolean isAdministrativePenalty, String amountUsage){
        LoanOutTailAfterModel model = new LoanOutTailAfterModel(loanId, financeState, repayPower, isOverdue, isAdministrativePenalty, amountUsage);
        loanOutTailAfterMapper.update(model);
        return model;
    }
}
