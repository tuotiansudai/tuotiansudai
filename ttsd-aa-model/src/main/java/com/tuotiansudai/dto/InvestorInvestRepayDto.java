package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;

import java.io.Serializable;
import java.util.Date;

public class InvestorInvestRepayDto implements Serializable {
    private Date repayDay;
    private long amount;
    private RepayStatus status;

    public InvestorInvestRepayDto(InvestRepayModel investRepayModel) {
        this.repayDay = investRepayModel.getRepayDate();
        this.status = investRepayModel.getStatus();
        if (RepayStatus.COMPLETE.equals(this.status)) {
            this.amount = investRepayModel.getRepayAmount();
        } else {
            this.amount = investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
        }
    }

    public Date getRepayDay() {
        return repayDay;
    }

    public long getAmount() {
        return amount;
    }

    public RepayStatus getStatus() {
        return status;
    }
}
