package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.job.AutoInvestJob;
import com.tuotiansudai.job.DeadlineFundraisingJob;
import com.tuotiansudai.job.FundraisingStartJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanCreateService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private PayWrapperClient payWrapperClient;

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
