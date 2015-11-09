package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.InvestPaginationItemView;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.utils.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class InvestPaginationItemDataDto implements Serializable {

    private long investId;

    private long loanId;

    private String loanName;

    private String loanType;

    private String amount;

    private String investorLoginName;

    private String referrerLoginName;

    private String source;

    private boolean isAutoInvest;

    private String status;

    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextRepayDate;

    private String nextRepayAmount;

    private boolean hasInvestRepay;

    public InvestPaginationItemDataDto(InvestPaginationItemView view) {
        this.investId = view.getId();
        this.loanId = view.getLoanId();
        this.amount = AmountConverter.convertCentToString(view.getAmount());
        this.loanName = view.getLoanName();
        this.investorLoginName = view.getLoginName();
        this.referrerLoginName = view.getReferrerLoginName();
        this.source = view.getSource().name();
        this.isAutoInvest = view.isAutoInvest();
        this.loanType = view.getLoanType().getName();
        this.createdTime = view.getCreatedTime();
        this.status = view.getStatus().getDescription();
        this.nextRepayDate = view.getNextRepayDate();
        this.nextRepayAmount = AmountConverter.convertCentToString(view.getNextRepayAmount());;
        this.hasInvestRepay = Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.COMPLETE).contains(view.getLoanStatus());
    }

    public long getInvestId() {
        return investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public String getLoanType() {
        return loanType;
    }

    public String getAmount() {
        return amount;
    }

    public String getInvestorLoginName() {
        return investorLoginName;
    }

    public String getReferrerLoginName() {
        return referrerLoginName;
    }

    public String getSource() {
        return source;
    }

    public boolean isAutoInvest() {
        return isAutoInvest;
    }

    public String getStatus() {
        return status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public String getNextRepayAmount() {
        return nextRepayAmount;
    }

    public boolean isHasInvestRepay() {
        return hasInvestRepay;
    }
}
