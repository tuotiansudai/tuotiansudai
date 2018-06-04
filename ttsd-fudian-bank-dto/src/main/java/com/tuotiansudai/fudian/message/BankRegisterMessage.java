package com.tuotiansudai.fudian.message;


public class BankRegisterMessage {

    private String loginName;

    private String mobile;

    private String identityCode;

    private String realName;

    private String BankAccountNo;

    private String BankUserName;

    private String bankOrderDate;

    private String bankOrderNo;

    public BankRegisterMessage() {
    }

    public BankRegisterMessage(String loginName, String mobile, String identityCode, String realName, String bankAccountNo, String bankUserName, String bankOrderDate, String bankOrderNo) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.identityCode = identityCode;
        this.realName = realName;
        BankAccountNo = bankAccountNo;
        BankUserName = bankUserName;
        this.bankOrderDate = bankOrderDate;
        this.bankOrderNo = bankOrderNo;
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

    public String getBankAccountNo() {
        return BankAccountNo;
    }

    public String getBankUserName() {
        return BankUserName;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }
}
