package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.InvestModel;
import org.apache.commons.lang3.NotImplementedException;

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

    public UserInvestRecordResponseDataDto() {
    }

    public UserInvestRecordResponseDataDto(InvestModel invest) {
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
