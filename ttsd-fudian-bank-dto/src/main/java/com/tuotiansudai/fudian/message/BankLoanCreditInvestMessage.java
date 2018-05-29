package com.tuotiansudai.fudian.message;

import java.io.Serializable;

public class BankLoanCreditInvestMessage implements Serializable {

    private long loanId;

    private String loanName;

    private long investId;

    private long amount;

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    private String bankOrderNo;

    private String bankOrderDate;

    public BankLoanCreditInvestMessage() {
    }

    public BankLoanCreditInvestMessage(long loanId, String loanName, long investId, long amount, String loginName, String mobile, String bankUserName, String bankAccountNo, String bankOrderNo, String bankOrderDate) {
        this.loanId = loanId;
        this.loanName = loanName;
        this.investId = investId;
        this.amount = amount;
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

    public String getLoanName() {
        return loanName;
    }

    public long getInvestId() {
        return investId;
    }

    public long getAmount() {
        return amount;
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
