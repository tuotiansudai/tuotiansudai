package com.ttsd.api.dto;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.RepayRoadmap;

import java.util.Date;

public class InvestDetailResponseDataDto extends BaseResponseDataDto{
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
    private Double investMoney;
    /**
     * 投资状态
     */
    private String investStatus;
    /**
     * 投资利率
     */
    private Double investRate;
    /**
     * 已还本金
     */
    private Double paidCorpus;
    /**
     * 已还利息
     */
    private Double paidInterest;
    /**
     * 下个还款日
     */
    private Date nextRepayDate;
    /**
     * 下个还款总金额
     */
    private Double nextRepayMoney;

    public InvestDetailResponseDataDto(){

    }

    public InvestDetailResponseDataDto(Invest invest){
        Loan loan = invest.getLoan();
        this.loanId = loan.getId();
        this.loanName = loan.getName();
        this.investId = invest.getId();
        this.investMoney = invest.getInvestMoney();
        this.investStatus = invest.getStatus();
        this.investRate = invest.getRate();

        RepayRoadmap rrmap = invest.getRepayRoadmap();
        this.paidCorpus = rrmap.getPaidCorpus();
        this.paidInterest = rrmap.getPaidInterest();
        this.nextRepayDate = rrmap.getNextRepayDate();
        this.nextRepayMoney = rrmap.getNextRepayMoney();
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

    public Double getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(Double investMoney) {
        this.investMoney = investMoney;
    }

    public String getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(String investStatus) {
        this.investStatus = investStatus;
    }

    public Double getInvestRate() {
        return investRate;
    }

    public void setInvestRate(Double investRate) {
        this.investRate = investRate;
    }

    public Double getPaidCorpus() {
        return paidCorpus;
    }

    public void setPaidCorpus(Double paidCorpus) {
        this.paidCorpus = paidCorpus;
    }

    public Double getPaidInterest() {
        return paidInterest;
    }

    public void setPaidInterest(Double paidInterest) {
        this.paidInterest = paidInterest;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public Double getNextRepayMoney() {
        return nextRepayMoney;
    }

    public void setNextRepayMoney(Double nextRepayMoney) {
        this.nextRepayMoney = nextRepayMoney;
    }
}
