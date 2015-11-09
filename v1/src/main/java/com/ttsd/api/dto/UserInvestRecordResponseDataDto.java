package com.ttsd.api.dto;

import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.repay.model.InvestRepay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserInvestRecordResponseDataDto extends BaseResponseDataDto {
    /**
     * 标的ID
     */
    private String loanId;
    /**
     * 标的名称
     */
    private String loanName;
    /**
     * 标的状态
     */
    private String loanStatus;
    /**
     * 标的状态描述
     */
    private String loanStatusDesc;
    /**
     * 投资ID
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
     * 投资状态
     */
    private String investStatus;
    /**
     * 投资状态描述
     */
    private String investStatusDesc;
    /**
     * 投资利率
     */
    private String investRate;
    /**
     * 投资总收益
     */
    private String investInterest;

    public UserInvestRecordResponseDataDto(){
    }

    public UserInvestRecordResponseDataDto(Invest invest){
        Loan loan = invest.getLoan();
        this.loanId = loan.getId();
        this.loanName = loan.getName();
        this.loanStatus = loan.getStatus();
        this.loanStatusDesc = LoanStatus.getMessageByCode(loan.getStatus());
        this.investId = invest.getId();
        this.investMoney = String.format("%.2f", invest.getInvestMoney());
        this.investTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invest.getTime()).toString();
        this.investStatus = invest.getStatus();
        this.investStatusDesc = InvestStatus.getMessageByCode(invest.getStatus());
        this.investRate = String.format("%.1f", invest.getRatePercent());
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

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatusDesc() {
        return loanStatusDesc;
    }

    public void setLoanStatusDesc(String loanStatusDesc) {
        this.loanStatusDesc = loanStatusDesc;
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

    public String getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(String investStatus) {
        this.investStatus = investStatus;
    }

    public String getInvestStatusDesc() {
        return investStatusDesc;
    }

    public void setInvestStatusDesc(String investStatusDesc) {
        this.investStatusDesc = investStatusDesc;
    }

    public String getInvestRate() {
        return investRate;
    }

    public void setInvestRate(String investRate) {
        this.investRate = investRate;
    }

    public String getInvestInterest() {
        return investInterest;
    }

    public void setInvestInterest(String investInterest) {
        this.investInterest = investInterest;
    }
}
