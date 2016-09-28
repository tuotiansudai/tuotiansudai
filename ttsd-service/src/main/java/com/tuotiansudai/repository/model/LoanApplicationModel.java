package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanApplicationDto;

import java.io.Serializable;
import java.util.Date;

public class LoanApplicationModel implements Serializable {
    private long id;
    private String loginName;
    private LoanApplicationRegion region;
    private int amount;
    private int period;
    private PledgeType pledgeType;
    private String pledgeInfo;
    private String comment;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;

    public LoanApplicationModel() {
    }

    public LoanApplicationModel(LoanApplicationDto loanApplicationDto) {
//        id
        this.loginName = loanApplicationDto.getLoginName();
        this.region = loanApplicationDto.getRegion();
        this.amount = loanApplicationDto.getAmount();
        this.period = loanApplicationDto.getPeriod();
        this.pledgeType = loanApplicationDto.getPledgeType();
        this.pledgeInfo = loanApplicationDto.getPledgeInfo();
//        comment
        this.createdTime = new Date();
        this.updatedBy = loanApplicationDto.getLoginName();
        this.updatedTime = new Date();
    }

    public void updateByView(LoanApplicationView loanApplicationView) {
//        loginName
        this.region = loanApplicationView.getRegion();
        this.amount = loanApplicationView.getAmount();
        this.period = loanApplicationView.getPeriod();
        this.pledgeType = loanApplicationView.getPledgeType();
        this.pledgeInfo = loanApplicationView.getPledgeInfo();
        this.comment = loanApplicationView.getComment();
//        createdTime;
        this.updatedBy = loanApplicationView.getUpdatedBy();
        this.updatedTime = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public LoanApplicationRegion getRegion() {
        return region;
    }

    public void setRegion(LoanApplicationRegion region) {
        this.region = region;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getPledgeInfo() {
        return pledgeInfo;
    }

    public void setPledgeInfo(String pledgeInfo) {
        this.pledgeInfo = pledgeInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
