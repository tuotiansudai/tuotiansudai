package com.tuotiansudai.transfer.service.impl;

import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.TransferApplyAutoCancelJob;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class InvestTransferServiceImpl implements InvestTransferService{

    static Logger logger = Logger.getLogger(InvestTransferServiceImpl.class);

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private JobManager jobManager;

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
                transferApplicationDto.getTransferInterest(), getTransferFee(investModel), getDeadline());

        transferApplicationMapper.create(transferApplicationModel);

        investTransferApplyJob(transferApplicationModel);
    }

    private long getTransferFee(InvestModel investModel) {

    }

    private Date getDeadline() {
        
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
