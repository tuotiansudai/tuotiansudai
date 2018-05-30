package com.tuotiansudai.fudian.message;

public class BankLoanRepayMessage extends BankBaseMessage {

    private long loanId;

    private long loanRepayId;

    private long capital;

    private long interest;

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanRepayMessage() {
    }

    public
    BankLoanRepayMessage(long loanId, long loanRepayId, long capital, long interest, String loginName, String mobile, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate) {
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.capital = capital;
        this.interest = interest;
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getCapital() {
        return capital;
    }

    public long getInterest() {
        return interest;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }
}
