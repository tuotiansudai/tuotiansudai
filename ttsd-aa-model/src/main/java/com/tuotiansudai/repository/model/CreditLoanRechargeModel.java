package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class CreditLoanRechargeModel implements Serializable {

    private long id;

    private String operatorName;

    private String accountName;

    private Date createdtime;

    private long amount;

    private Date updatedTime;

    private RechargeStatus status;

    public CreditLoanRechargeModel(CreditLoanRechargeDto dto, String accountName){
        this.operatorName = dto.getOperatorLoginName();
        this.accountName = accountName;
        this.createdtime = new Date();
        this.updatedTime = new Date();
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.status = RechargeStatus.WAIT_PAY;
    }
    public CreditLoanRechargeModel(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public RechargeStatus getStatus() {
        return status;
    }

    public void setStatus(RechargeStatus status) {
        this.status = status;
    }

    public Date getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Date createdtime) {
        this.createdtime = createdtime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
