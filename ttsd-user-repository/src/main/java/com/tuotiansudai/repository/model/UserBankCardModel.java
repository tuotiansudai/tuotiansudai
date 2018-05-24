package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserBankCardModel implements Serializable {

    private long id;

    private String loginName;

    private String bank;

    private String bankCode;

    private String cardNumber;

    private String bankOrderNo;

    private String bankOrderDate;

    private UserBankCardStatus status;

    private Date createdTime;

    private Date updatedTime;

    public UserBankCardModel() {
    }

    public UserBankCardModel(String loginName, String bank, String bankCode, String cardNumber, String bankOrderNo, String bankOrderDate, UserBankCardStatus status) {
        this.loginName = loginName;
        this.bank = bank;
        this.bankCode = bankCode;
        this.cardNumber = cardNumber;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public void setBankOrderDate(String bankOrderDate) {
        this.bankOrderDate = bankOrderDate;
    }

    public UserBankCardStatus getStatus() {
        return status;
    }

    public void setStatus(UserBankCardStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}
