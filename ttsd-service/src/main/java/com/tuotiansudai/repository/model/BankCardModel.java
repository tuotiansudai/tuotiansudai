package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.BindBankCardDto;

import java.util.Date;

public class BankCardModel {

    private long id;

    private String bankNumber;

    private String cardNumber;

    private Date createdTime = new Date();

    private BankCardStatus status;

    private String loginName;

    private boolean isOpenFastPayment;


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

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
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

    public boolean isOpenFastPayment() {
        return isOpenFastPayment;
    }

    public void setIsOpenFastPayment(boolean isOpenFastPayment) {
        this.isOpenFastPayment = isOpenFastPayment;
    }
}
