package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestModel;
import org.apache.commons.lang3.NotImplementedException;

public class InvestDetailResponseDataDto extends BaseResponseDataDto {
    /**
     * 标的ID
     */
    private String loanId;
    /**
     * 标的名称
     */
    private String loanName;
    /**
     * 投资ID
     */
    private String investId;
    /**
     * 投资金额
     */
    private String investMoney;
    /**
     * 投资状态
     */
    private String investStatus;
    /**
     * 投资利率
     */
    private String investRate;
    /**
     * 已还本金
     */
    private String paidCorpus;
    /**
     * 已还利息
     */
    private String paidInterest;
    /**
     * 下个还款日
     */
    private String nextRepayDate;
    /**
     * 下个还款总金额
     */
    private String nextRepayMoney;

    public InvestDetailResponseDataDto() {

    }

    public InvestDetailResponseDataDto(InvestModel invest) {
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

    public String getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(String investStatus) {
        this.investStatus = investStatus;
    }

    public String getInvestRate() {
        return investRate;
    }

    public void setInvestRate(String investRate) {
        this.investRate = investRate;
    }

    public String getPaidCorpus() {
        return paidCorpus;
    }

    public void setPaidCorpus(String paidCorpus) {
        this.paidCorpus = paidCorpus;
    }

    public String getPaidInterest() {
        return paidInterest;
    }

    public void setPaidInterest(String paidInterest) {
        this.paidInterest = paidInterest;
    }

    public String getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(String nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public String getNextRepayMoney() {
        return nextRepayMoney;
    }

    public void setNextRepayMoney(String nextRepayMoney) {
        this.nextRepayMoney = nextRepayMoney;
    }
}
