package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
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
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.transfer.util.TransferRuleUtil;
import com.tuotiansudai.util.JobManager;
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
    public BaseDataDto isAllowTransfer(long transferApplicationId) {
        BaseDataDto baseDataDto = new BaseDataDto();
        DateTime dateTime = new DateTime();
        InvestModel investModel = investMapper.findById(transferApplicationId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(investModel.getLoanId());
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        if (Days.daysBetween(dateTime, new DateTime(loanRepayModel.getRepayDate())).getDays() < transferRuleModel.getDaysLimit()) {
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("该项目即将在"+transferRuleModel.getDaysLimit()+"日内回款，暂不可转让，请选择其他项目。");
        } else if (loanModel.getStatus() != LoanStatus.REPAYING){
            baseDataDto.setStatus(false);
            baseDataDto.setMessage("该项目已提前回款，不可进行转让。");
        } else {
            baseDataDto.setStatus(true);
        }
        return baseDataDto;
    }

    @Override
    @Transactional
    public boolean investTransferApply(TransferApplicationDto transferApplicationDto) {

        InvestModel investModel = investMapper.findById(transferApplicationDto.getTransferInvestId());

        if (investModel.getStatus() != InvestStatus.SUCCESS || investModel.getAmount() < transferApplicationDto.getTransferAmount()) {
            return false;
        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(investModel.getLoanId());

        if (loanRepayModel == null) {
            return false;
        }


        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdOrderByPeriodAsc(investModel.getLoanId());
        int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(transferApplicationDto.getTransferInvestId(),loanRepayModel.getPeriod());

        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, this.generateTransferApplyName(), loanRepayModel.getPeriod(), transferApplicationDto.getTransferAmount(),
                TransferRuleUtil.getTransferFee(investModel, transferRuleModel, loanModel), getDeadlineFromNow(),leftPeriod);

        transferApplicationMapper.create(transferApplicationModel);

        investMapper.updateTransferStatus(investModel.getId(), TransferStatus.TRANSFERRING);

        investTransferApplyJob(transferApplicationModel);

        return true;
    }

    @Override
    public Date getDeadlineFromNow() {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        DateTime dateTime = new DateTime();
        return dateTime.plusDays(transferRuleModel.getDaysLimit() + 1).withTimeAtStartOfDay().toDate();
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
        DateTime current = new DateTime().withTimeAtStartOfDay();
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
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investId, Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING, TransferStatus.CANCEL));
        for(TransferApplicationModel transferApplicationModelTemp:transferApplicationModels){
            if(transferApplicationModelTemp.getStatus() != TransferStatus.CANCEL) {
                logger.debug(MessageFormat.format("{0} is transferred",investModel.getLoanId()));
                return false;
            }
            DateTime transferTime = new DateTime(transferApplicationModelTemp.getTransferTime()).withTimeAtStartOfDay();
            return transferApplicationModelTemp.getStatus() == TransferStatus.CANCEL && current.compareTo(transferTime) != 0;

        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(investModel.getLoanId());
        if(loanRepayModel == null){
            logger.debug(MessageFormat.format("{0} is completed ",investModel.getLoanId()));
            return false;
        }
        TransferRuleModel transferRuleModel =  transferRuleMapper.find();
        if(!transferRuleModel.isMultipleTransferEnabled()){
            TransferApplicationModel transfereeApplicationModel = transferApplicationMapper.findByInvestId(investId);
            if( transfereeApplicationModel != null){
                logger.debug(MessageFormat.format("{0} MultipleTransferEnabled is false ",investId));
                return false;
            }

        }

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
                return transferApplicationPaginationItemDataDto;
            }
        });

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> dto = new BasePaginationDataDto(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }
    
    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName,List<TransferStatus> statusList ,Integer index, Integer pageSize) {

        int count = transferApplicationMapper.findCountTransferApplicationPaginationByLoginName(transferrerLoginName, statusList);
        List<TransferApplicationRecordDto> items = Lists.newArrayList();
        if (count > 0) {
            int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findTransferApplicationPaginationByLoginName(transferrerLoginName, statusList, (index - 1) * pageSize, pageSize);

        }
        List<TransferApplicationPaginationItemDataDto> records = Lists.transform(items, new Function<TransferApplicationRecordDto, TransferApplicationPaginationItemDataDto>() {
            @Override
            public TransferApplicationPaginationItemDataDto apply(TransferApplicationRecordDto input) {
                TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto(input);
                if (input.getTransferStatus() == TransferStatus.TRANSFERABLE) {
                    transferApplicationPaginationItemDataDto.setTransferStatus(isTransferable(input.getTransferApplicationId()) ? input.getTransferStatus().getDescription() : "--");
                } else if (input.getTransferStatus() == TransferStatus.NONTRANSFERABLE) {
                    transferApplicationPaginationItemDataDto.setTransferStatus("--");
                } else {
                    transferApplicationPaginationItemDataDto.setTransferStatus(input.getTransferStatus().getDescription());
                }
                return transferApplicationPaginationItemDataDto;
            }
        });

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> dto = new BasePaginationDataDto(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    public BasePaginationDataDto<TransferInvestDetailDto> getInvestTransferList(String investorLoginName,
                                                               int index,
                                                               int pageSize,
                                                               Date startTime,
                                                               Date endTime,
                                                               LoanStatus loanStatus) {
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

            List<TransferInvestDetailDto> items = Lists.newArrayList();
            long count = transferApplicationMapper.findCountInvestTransferPagination(investorLoginName, startTime, endTime, loanStatus);

            if (count > 0) {
                int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
                index = index > totalPages ? totalPages : index;
                items = transferApplicationMapper.findTransferInvestList(investorLoginName, (index - 1) * pageSize, pageSize, startTime, endTime, loanStatus);
            }
            BasePaginationDataDto dto = new BasePaginationDataDto(index, pageSize, count, items);
            dto.setStatus(true);
            return dto;
    }

}
