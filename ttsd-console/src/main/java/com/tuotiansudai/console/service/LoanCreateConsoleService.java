package com.tuotiansudai.console.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanCreateRequestDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.message.dto.MessageCompleteDto;
import com.tuotiansudai.message.repository.model.*;
import com.tuotiansudai.message.service.MessageService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoanCreateConsoleService {

    private static Logger logger = Logger.getLogger(LoanCreateConsoleService.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

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
    private MessageService messageService;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    private final static String LOAN_MESSAGE_REDIS_KEY = "web:loan:loanMessageMap";

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

        loanCreateRequestDto.getLoan().setId(loanId);
        editLoanMessage(loanCreateRequestDto);

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

        editLoanMessage(loanCreateRequestDto);

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

    private void editLoanMessage(LoanCreateRequestDto loanCreateRequestDto) {
        try {
            if (redisWrapperClient.hexists(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId()))) {
                long messageId = Long.valueOf(redisWrapperClient.hget(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId())));
                if (null == loanCreateRequestDto.getLoanMessage()) {
                    redisWrapperClient.hdel(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId()));
                    messageService.deleteMessage(messageId, loanCreateRequestDto.getLoan().getCreatedBy());
                } else {
                    MessageCompleteDto messageCompleteDto = loanCreateRequestDtoToMessageCompleteDto(loanCreateRequestDto);
                    messageCompleteDto.setId(messageId);
                    messageService.createAndEditManualMessage(messageCompleteDto, 0L);
                }
            } else {
                if (null != loanCreateRequestDto.getLoanMessage()) {
                    MessageCompleteDto messageCompleteDto = loanCreateRequestDtoToMessageCompleteDto(loanCreateRequestDto);
                    long messageId = messageService.createAndEditManualMessage(messageCompleteDto, 0L);
                    redisWrapperClient.hset(LOAN_MESSAGE_REDIS_KEY, String.valueOf(loanCreateRequestDto.getLoan().getId()), String.valueOf(messageId));
                }
            }
            logger.info(MessageFormat.format("[LoanCreateConsoleService] loan message create finished. loanId:{0}", loanCreateRequestDto.getLoan().getId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[LoanCreateConsoleService] loan message create fail. loanId:{0}", loanCreateRequestDto.getLoan().getId()), e);
        }
    }

    private MessageCompleteDto loanCreateRequestDtoToMessageCompleteDto(LoanCreateRequestDto loanCreateRequestDto) {
        MessageCompleteDto messageCompleteDto = new MessageCompleteDto();

        messageCompleteDto.setTitle(loanCreateRequestDto.getLoanMessage().getLoanMessageTitle());
        messageCompleteDto.setTemplate(loanCreateRequestDto.getLoanMessage().getLoanMessageContent());
        messageCompleteDto.setTemplateTxt(loanCreateRequestDto.getLoanMessage().getLoanMessageContent());
        messageCompleteDto.setType(MessageType.MANUAL);
        messageCompleteDto.setUserGroups(Lists.newArrayList(MessageUserGroup.ALL_USER));
        messageCompleteDto.setChannels(Lists.newArrayList(MessageChannel.WEBSITE, MessageChannel.APP_MESSAGE));
        messageCompleteDto.setMessageCategory(MessageCategory.NOTIFY);
        messageCompleteDto.setWebUrl(MessageFormat.format("/loan-list", loanCreateRequestDto.getLoan().getId()));
        messageCompleteDto.setAppUrl(AppUrl.INVEST_NORMAL);
        messageCompleteDto.setJpush(true);
        messageCompleteDto.setPushType(PushType.IMPORTANT_EVENT);
        messageCompleteDto.setPushSource(PushSource.ALL);
        messageCompleteDto.setStatus(MessageStatus.APPROVED);
        messageCompleteDto.setReadCount(0);
        messageCompleteDto.setActivatedBy(null);
        messageCompleteDto.setActivatedTime(null);
        messageCompleteDto.setExpiredTime(new DateTime().withDate(9999, 12, 31).toDate());
        messageCompleteDto.setUpdatedBy(loanCreateRequestDto.getLoan().getCreatedBy());
        messageCompleteDto.setUpdatedTime(new Date());
        messageCompleteDto.setCreatedBy(loanCreateRequestDto.getLoan().getCreatedBy());
        messageCompleteDto.setCreatedTime(new Date());

        return messageCompleteDto;
    }
}
