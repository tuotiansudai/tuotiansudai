package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class UserBillPaginationItemDataDto {

    private long id;

    private String balance = "0.00";

    private String freeze = "0.00";

    private String income = "0.00";

    private String cost = "0.00";

    private String businessType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdTime;

    public UserBillPaginationItemDataDto() {
    }

    public UserBillPaginationItemDataDto(UserBillModel userBillModel) {
        this.id = userBillModel.getId();
        this.freeze = AmountConverter.convertCentToString(userBillModel.getFreeze());
        this.balance = AmountConverter.convertCentToString(userBillModel.getBalance());
        if (UserBillOperationType.TI_BALANCE == userBillModel.getOperationType()) {
            this.income = AmountConverter.convertCentToString(userBillModel.getAmount());
        }
        if (UserBillOperationType.TO_BALANCE == userBillModel.getOperationType() || UserBillOperationType.TO_FREEZE == userBillModel.getOperationType()) {
            this.cost = AmountConverter.convertCentToString(userBillModel.getAmount());
        }
        this.businessType = userBillModel.getBusinessType().getDescription();
        this.createdTime = userBillModel.getCreatedTime();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
