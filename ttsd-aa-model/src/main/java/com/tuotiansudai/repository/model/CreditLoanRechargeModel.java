package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class CreditLoanRechargeModel implements Serializable {

    private long id;

    private String operatorName;

    private String accountName;

    private Date createdTime;

    private long amount;

    private Date updatedTime;

    private BankRechargeStatus status;

    public CreditLoanRechargeModel(CreditLoanRechargeDto dto, String accountName){
        this.operatorName = dto.getOperatorLoginName();
        this.accountName = accountName;
        this.createdTime = new Date();
        this.updatedTime = new Date();
        this.amount = AmountConverter.convertStringToCent(dto.getAmount());
        this.status = BankRechargeStatus.WAIT_PAY;
    }
    public CreditLoanRechargeModel(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BankRechargeStatus getStatus() {
        return status;
    }

    public void setStatus(BankRechargeStatus status) {
        this.status = status;
    }
}
