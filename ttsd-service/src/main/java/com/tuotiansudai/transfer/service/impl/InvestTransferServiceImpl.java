package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.TransferApplicationAutoCancelJob;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestTransferServiceImpl implements InvestTransferService{

    static Logger logger = Logger.getLogger(InvestTransferServiceImpl.class);

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private TransferRuleMapper transferRuleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    protected final static String TRANSFER_APPLY_NAME = "ZR{0}-{1}";

    public static String redisTransferApplicationNumber = "web:{0}:transferApplicationNumber";

    @Override
    @Transactional
    public void investTransferApply(TransferApplicationDto transferApplicationDto) {

        InvestModel investModel = investMapper.findById(transferApplicationDto.getTransferInvestId());

        if (investModel.getStatus() != InvestStatus.SUCCESS || investModel.getAmount() < transferApplicationDto.getTransferAmount()) {
            return;
        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(investModel.getLoanId());

        if (loanRepayModel == null) {
            return;
        }


        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(investModel.getLoanId());
        int transferInterestDays = 0;
        if(transferApplicationDto.getTransferInterest()){
            transferInterestDays = InterestCalculator.calculateTransferInterestDays(loanModel,loanRepayModels);
        }

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, this.generateTransferApplyName(), loanRepayModel.getPeriod(), transferApplicationDto.getTransferAmount(),
                transferApplicationDto.getTransferInterest(), TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel), getDeadlineFromNow(),transferInterestDays);

        transferApplicationMapper.create(transferApplicationModel);

        investMapper.updateTransferStatus(investModel.getId(), TransferStatus.TRANSFERRING);

        investTransferApplyJob(transferApplicationModel);
    }

    @Override
    public Date getDeadlineFromNow() {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        DateTime dateTime = new DateTime();
        return dateTime.plusDays(transferRuleModel.getDaysLimit()).withTimeAtStartOfDay().toDate();
    }

    @Override
    public boolean cancelTransferApplication(long transferApplicationId) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (transferApplicationModel != null && transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            transferApplicationModel.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferApplicationModel);
            investMapper.updateTransferStatus(transferApplicationModel.getTransferInvestId(), TransferStatus.TRANSFERABLE);
            return true;
        } else {
            logger.debug("this transfer apply status is not allow cancel, id = " + transferApplicationId);
            return false;
        }
    }

    private void investTransferApplyJob(TransferApplicationModel transferApplicationModel) {
        if (!transferApplicationModel.getDeadline().after(new Date())) {
            logger.debug("investTransferApplyJob create failed, expect deadline is before now, id = " + transferApplicationModel.getId());
            return;
        }
        try {
            jobManager.newJob(JobType.TransferApplyAutoCancel, TransferApplicationAutoCancelJob.class)
                    .withIdentity(JobType.TransferApplyAutoCancel.name(), "Transfer-apply-" + transferApplicationModel.getId())
                    .replaceExistingJob(true)
                    .addJobData("Transfer-apply-id", transferApplicationModel.getId())
                    .runOnceAt(transferApplicationModel.getDeadline()).submit();
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    protected String generateTransferApplyName() {
        String date = new DateTime().toString("yyyyMMdd");
        String name = String.format("%03d", redisWrapperClient.incr(MessageFormat.format(redisTransferApplicationNumber, date)));
        return MessageFormat.format(TRANSFER_APPLY_NAME, date, name);
    }
    @Override
    public boolean isTransferable(long investId){

        InvestModel investModel = investMapper.findById(investId);
        if(investModel == null){
            logger.debug(MessageFormat.format("{0} is not exist",investId));
            return false;
        }
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        if(loanModel.getStatus() != LoanStatus.REPAYING){
            logger.debug(MessageFormat.format("{0} is not REPAYING",investModel.getLoanId()));
            return false;
        }
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investId,Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING));
        if (CollectionUtils.isNotEmpty(transferApplicationModels)) {
            logger.debug(MessageFormat.format("{0} is not REPAYING",investModel.getLoanId()));
            return false;
        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(investModel.getLoanId());
        if(loanRepayModel == null){
            logger.debug(MessageFormat.format("{0} is completed ",loanRepayModel.getLoanId()));
            return false;
        }

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investId);
        if(transferApplicationModel != null && transferApplicationModel.getPeriod() == loanRepayModel.getPeriod()){
            logger.debug(MessageFormat.format("{0} had been transfer ",investId));
            return false;
        }
        TransferRuleModel transferRuleModel =  transferRuleMapper.find();
        DateTime current = new DateTime().withTimeAtStartOfDay();
        int periodDuration = Days.daysBetween(current.withTimeAtStartOfDay(),new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay()).getDays();

        if(periodDuration > transferRuleModel.getDaysLimit()){
            logger.debug(MessageFormat.format("{0} right away repay ",investId));
            return false;
        }

        return true;


    }

    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findTransferApplicationPaginationList(Long transferApplicationId, Date startTime,
                                                                                    Date endTime, TransferStatus status,
                                                                                    String transferrerLoginName, String transfereeLoginName, Long loanId, Integer index, Integer pageSize) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        int count = transferApplicationMapper.findCountTransferApplicationPagination(transferApplicationId,startTime,endTime,status,transferrerLoginName,transfereeLoginName,loanId);
        List<TransferApplicationRecordDto> items = Lists.newArrayList();
        if(count > 0){
            int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findTransferApplicationPaginationList(transferApplicationId,startTime,endTime,status,transferrerLoginName,transfereeLoginName,loanId,(index - 1) * pageSize,pageSize);

        }
        List<TransferApplicationPaginationItemDataDto> records = Lists.transform(items, new Function<TransferApplicationRecordDto, TransferApplicationPaginationItemDataDto>() {
            @Override
            public TransferApplicationPaginationItemDataDto apply(TransferApplicationRecordDto input) {
                TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto(input);
                int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(input.getTransferInvestId(),input.getPeriod());
                transferApplicationPaginationItemDataDto.setLeftPeriod(String.valueOf(leftPeriod));
                return transferApplicationPaginationItemDataDto;
            }
        });

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> dto = new BasePaginationDataDto(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

}
