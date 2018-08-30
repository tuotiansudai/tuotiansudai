package com.tuotiansudai.fudian.message;


public class BankRegisterMessage {

    private String loginName;

    private String mobile;

    private String identityCode;

    private String realName;

    private String token;

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderNo;

    private String bankOrderDate;

    private String bank;

    private String bankCardNo;

    private String bankCode;

    private boolean isInvestor;

    private String bankMobile;

    public BankRegisterMessage() {
    }

    public BankRegisterMessage(String loginName, String mobile, String identityCode, String realName, String token, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate, boolean isInvestor) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.identityCode = identityCode;
        this.realName = realName;
        this.token = token;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.isInvestor = isInvestor;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public String getRealName() {
        return realName;
    }

    public String getToken() {
        return token;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public boolean isInvestor() {
        return isInvestor;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }
}
