package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.BindBankCardDto;

import java.io.Serializable;
import java.util.Date;

public class BankCardModel implements Serializable {

    private long id;

    private String bankCode;

    private String cardNumber;

    private Date createdTime = new Date();

    private BankCardStatus status;

    private String loginName;

    private boolean isFastPayOn;

    public BankCardModel() {
    }

    public BankCardModel(BindBankCardDto bindBankCardDto) {
        this.loginName = bindBankCardDto.getLoginName();
        this.cardNumber = bindBankCardDto.getCardNumber();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public BankCardStatus getStatus() {
        return status;
    }

    public void setStatus(BankCardStatus status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isFastPayOn() {
        return isFastPayOn;
    }

    public void setIsFastPayOn(boolean isFastPayOn) {
        this.isFastPayOn = isFastPayOn;
    }
}
