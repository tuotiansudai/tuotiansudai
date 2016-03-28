package com.tuotiansudai.transfer.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.TransferApplyAutoCancelJob;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferRuleModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

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

    protected final static String TRANSFER_APPLY_NAME = "ZR{0}-{1}";

    @Override
    @Transactional
    public void investTransferApply(TransferApplicationDto transferApplicationDto) throws Exception{

        InvestModel investModel = investMapper.findById(transferApplicationDto.getTransferInvestId());

        if (investModel.getStatus() != InvestStatus.SUCCESS || investModel.getAmount() < transferApplicationDto.getTransferAmount()) {
            return;
        }

        LoanRepayModel loanRepayModel = loanRepayMapper.findEnabledLoanRepayByLoanId(investModel.getLoanId());

        if (loanRepayModel == null) {
            return;
        }
        TransferApplicationModel transferApplicationModel = new TransferApplicationModel(investModel, this.generateTransferApplyName(), loanRepayModel.getPeriod(), transferApplicationDto.getTransferAmount(),
                transferApplicationDto.getTransferInterest(), getTransferFee(investModel), getDeadlineFromNow());

        transferApplicationMapper.create(transferApplicationModel);

        investTransferApplyJob(transferApplicationModel);
    }

    public long getTransferFee(InvestModel investModel) {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        DateTime beginDate;
        DateTime endDate = new DateTime();
        if (Lists.newArrayList(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY, LoanType.INVEST_INTEREST_MONTHLY_REPAY).contains(loanModel.getType())){
            beginDate = new DateTime(investModel.getCreatedTime());
        } else {
            beginDate = new DateTime(loanModel.getRecheckTime());
        }
        int days = Days.daysBetween(beginDate, endDate).getDays();
        double fee;
        if (days <= transferRuleModel.getLevelOneUpper()) {
            fee = transferRuleModel.getLevelOneFee();
        } else if (days <= transferRuleModel.getLevelTwoUpper()) {
            fee = transferRuleModel.getLevelTwoFee();
        } else {
            fee = transferRuleModel.getLevelThreeFee();
        }
        return new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(fee)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
    }

    @Override
    public Date getDeadlineFromNow() {
        TransferRuleModel transferRuleModel = transferRuleMapper.find();
        DateTime dateTime = new DateTime();
        return dateTime.plusDays(transferRuleModel.getDaysLimit()).withTimeAtStartOfDay().toDate();
    }

    @Override
    public boolean investTransferApplyCancel(long id) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(id);
        if (transferApplicationModel != null && transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            transferApplicationModel.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferApplicationModel);
            return true;
        } else {
            logger.debug("this transfer apply status is not allow cancel, id = " + id);
            return false;
        }
    }

    private void investTransferApplyJob(TransferApplicationModel transferApplicationModel) {
        if (!transferApplicationModel.getDeadline().after(new Date())) {
            logger.debug("investTransferApplyJob create failed, expect deadline is before now, id = " + transferApplicationModel.getId());
            return;
        }
        try {
            jobManager.newJob(JobType.TransferApplyAutoCancel, TransferApplyAutoCancelJob.class)
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
        String name = transferApplicationMapper.findMaxNameInOneDay(MessageFormat.format(TRANSFER_APPLY_NAME, date, ""));
        if (StringUtils.isEmpty(name)) {
            name = "001";
        } else {
            name = String.format("%03d", Integer.parseInt(name) + 1);
        }
        return MessageFormat.format(TRANSFER_APPLY_NAME, date, name);
    }

}
