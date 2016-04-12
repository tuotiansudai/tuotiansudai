package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class InvestDetailModel extends InvestModel implements Serializable {
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
     * 下个还款日
     */
    private Date nextRepayDate;
    /**
     * 下次还款金额
     */
    private long nextRepayAmount;

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
