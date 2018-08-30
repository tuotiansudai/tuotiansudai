package com.tuotiansudai.fudian.message;

public class BankChangeMobileMessage extends BankBaseMessage {

    private String loginName;

    private String bankAccountNo;

    private String bankUserName;

    private String newPhone;

    public BankChangeMobileMessage() {
    }

    public BankChangeMobileMessage(boolean status, String message) {
        super(status, message);
    }

    public BankChangeMobileMessage(String loginName, String bankAccountNo, String bankUserName,String newPhone) {
        super(true, null);
        this.bankAccountNo = bankAccountNo;
        this.bankUserName = bankUserName;
        this.loginName = loginName;
        this.newPhone=newPhone;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }
}
