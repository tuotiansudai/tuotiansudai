package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

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

    private String loanType;

    public UserInvestRecordResponseDataDto() {
    }

    public UserInvestRecordResponseDataDto(InvestModel invest, LoanModel loan) {
        InvestStatus investStatus = InvestStatus.convertInvestStatus(invest.getStatus());
        LoanStatus loanStatus = LoanStatus.convertLoanStatus(loan.getStatus());
        this.loanId = String.valueOf(invest.getLoanId());
        this.loanName = loan.getName();
        this.loanStatus = loanStatus.getCode();
        this.loanStatusDesc = loanStatus.getMessage();
        this.investId = String.valueOf(invest.getId());
        this.investMoney = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime());
        this.investStatus = investStatus.getCode();
        this.investStatusDesc = investStatus.getMessage();
        this.investRate = String.format("%.1f", loan.getActivityRate() + loan.getBaseRate());
        this.loanType = loan.getProductType() != null ? loan.getProductType().name() : "";
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

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
