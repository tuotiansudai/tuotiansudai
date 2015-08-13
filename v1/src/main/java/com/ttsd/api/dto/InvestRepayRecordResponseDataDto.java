package com.ttsd.api.dto;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.InvestRepay;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InvestRepayRecordResponseDataDto{
    /**
     * 标的id
     */
    private  String loanId;
    /**
     * 标的名称
     */
    private  String loanName;
    /**
     * 投资id
     */
    private String investId;
    /**
     * 投资金额
     */
    private Double investMoney;
    /**
     * 投资时间
     */
    private String investTime;
    /**
     * 还款日
     */
    private String repayDay;
    /**
     * 还款时间
     */
    private String time;
    /**
     * 本金
     */
    private Double corpus;
    /**
     * 利息
     */
    private Double interest;

    public InvestRepayRecordResponseDataDto(){
    }

    public InvestRepayRecordResponseDataDto(InvestRepay investRepay){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Invest invest = investRepay.getInvest();
        Loan loan = invest.getLoan();
        this.loanId = loan.getId();
        this.loanName = loan.getName();
        this.investId = invest.getId();
        this.investMoney = invest.getInvestMoney();
        this.investTime = sdf.format(invest.getTime()).toString();
        this.repayDay = sdf.format(investRepay.getRepayDay()).toString();
        if(investRepay.getTime()!=null) {
            this.time = sdf.format(investRepay.getTime()).toString();
        }
        this.corpus = investRepay.getCorpus();
        this.interest = investRepay.getInterest();
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getCorpus() {
        return corpus;
    }

    public void setCorpus(Double corpus) {
        this.corpus = corpus;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }
}
