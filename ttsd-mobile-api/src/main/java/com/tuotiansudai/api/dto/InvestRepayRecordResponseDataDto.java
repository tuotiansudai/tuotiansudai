package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestRepayModel;
import org.apache.commons.lang3.NotImplementedException;

public class InvestRepayRecordResponseDataDto {
    /**
     * 标的id
     */
    private String loanId;
    /**
     * 标的名称
     */
    private String loanName;
    /**
     * 投资id
     */
    private String investId;
    /**
     * 投资金额
     */
    private String investMoney;
    /**
     * 投资时间
     */
    private String investTime;
    /**
     * 还款日
     */
    private String repayDay;
    /**
     * 还款状态
     */
    private String status;
    /**
     * 还款状态描述
     */
    private String statusDesc;
    /**
     * 还款时间
     */
    private String time;
    /**
     * 本金
     */
    private String corpus;
    /**
     * 利息
     */
    private String interest;

    public InvestRepayRecordResponseDataDto() {
    }

    public InvestRepayRecordResponseDataDto(InvestRepayModel investRepay) {
        throw new NotImplementedException(getClass().getName());
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCorpus() {
        return corpus;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
