package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.AmountUtil;

import java.io.Serializable;
import java.util.Date;

public class InvestDetailDto implements Serializable{
    /**
     * 投资ID
     */
    private long id;
    /**
     * 投资人ID
     */
    private String loginName;
    /**
     * 标的ID
     */
    private long loanId;
    /**
     * 投资金额
     */
    private String amount;
    /**
     * 投资状态
     */
    private InvestStatus status;
    /**
     * 投资来源渠道
     */
    private Source source;
    /**
     * 是否为自动投资
     */
    private boolean isAutoInvest;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 标的名称
     */
    private String loanName;
    /**
     * 标的类型
     */
    private LoanType loanType;
    /**
     * 标的状态
     */
    private LoanStatus loanStatus;
    /**
     * 投资人的推荐人
     */
    private String userReferrer;
    /**
     * 下次还款时间
     */
    private Date nextRepayDate;
    /**
     * 下次还款金额
     */
    private long nextRepayAmount;

    public InvestDetailDto(){

    }

    public InvestDetailDto(InvestDetailModel model){
        this.amount = AmountUtil.convertCentToString(model.getAmount());
        this.id = model.getId();
        this.isAutoInvest = model.isAutoInvest();
        this.loanId = model.getLoanId();
        this.loanName = model.getLoanName();
        this.loanStatus = model.getLoanStatus();
        this.loanType = model.getLoanType();
        this.loginName = model.getLoginName();
        this.source = model.getSource();
        this.status = model.getStatus();
        this.userReferrer = model.getUserReferrer();
        this.createdTime = model.getCreatedTime();
        this.nextRepayAmount = model.getNextRepayAmount();
        this.nextRepayDate = model.getNextRepayDate();
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

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public InvestStatus getStatus() {
        return status;
    }

    public void setStatus(InvestStatus status) {
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isAutoInvest() {
        return isAutoInvest;
    }

    public void setIsAutoInvest(boolean isAutoInvest) {
        this.isAutoInvest = isAutoInvest;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getUserReferrer() {
        return userReferrer;
    }

    public void setUserReferrer(String userReferrer) {
        this.userReferrer = userReferrer;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public long getNextRepayAmount() {
        return nextRepayAmount;
    }

    public void setNextRepayAmount(long nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }
}

