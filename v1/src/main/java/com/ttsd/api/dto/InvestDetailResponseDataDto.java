package com.ttsd.api.dto;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.RepayRoadmap;

import java.text.SimpleDateFormat;
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

    public InvestDetailResponseDataDto(){

    }

    public InvestDetailResponseDataDto(Invest invest){
        Loan loan = invest.getLoan();
        this.loanId = loan.getId();
        this.loanName = loan.getName();
        this.investId = invest.getId();
        this.investMoney = String.format("%.2f", invest.getInvestMoney());
        this.investStatus = invest.getStatus();
        this.investRate = String.format("%.1f", invest.getRatePercent());

        RepayRoadmap rrmap = invest.getRepayRoadmap();
        this.paidCorpus = String.format("%.2f", rrmap.getPaidCorpus());
        this.paidInterest = String.format("%.2f", rrmap.getPaidInterest());
        if(rrmap.getNextRepayDate()!=null) {
            this.nextRepayDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rrmap.getNextRepayDate());
        }else {
            this.nextRepayDate = "";
        }
        if(rrmap.getNextRepayMoney()!=null) {
            this.nextRepayMoney = String.format("%.2f", rrmap.getNextRepayMoney());
        }else{
            this.nextRepayMoney = "";
        }
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
