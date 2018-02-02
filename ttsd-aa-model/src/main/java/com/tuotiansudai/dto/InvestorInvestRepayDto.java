package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;

import java.io.Serializable;
import java.util.Date;

public class InvestorInvestRepayDto implements Serializable {
    private Date repayDate;
    private long amount;
    private RepayStatus status;
    private String statusDesc;
    private boolean isTransferred;

    public InvestorInvestRepayDto(InvestRepayModel investRepayModel, CouponRepayModel couponRepayModel) {
        if (null != investRepayModel.getActualRepayDate()) {
            this.repayDate = investRepayModel.getActualRepayDate();
        } else {
            this.repayDate = investRepayModel.getRepayDate();
        }

        long expectedInterest = investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee();
        if (couponRepayModel != null) {
            expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
        }

        this.amount = expectedInterest + investRepayModel.getCorpus();
        this.status = investRepayModel.getStatus();
        this.isTransferred = investRepayModel.isTransferred();
        this.statusDesc = convertStatusDesc(investRepayModel.getStatus());
    }

    private String convertStatusDesc(RepayStatus status) {
        if (RepayStatus.REPAYING.equals(status)) {
            return "待还款";
        }
        if (RepayStatus.WAIT_PAY.equals(status)) {
            return "待支付";
        }
        if (RepayStatus.COMPLETE.equals(status)) {
            return "已还款";
        }
        if (RepayStatus.OVERDUE.equals(status)) {
            return "已逾期";
        }
        return "";
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public long getAmount() {
        return amount;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public boolean isTransferred() {
        return isTransferred;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
}
