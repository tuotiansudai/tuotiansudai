package com.tuotiansudai.repository.model;

public class InvestNotifyInfo {
    /**
     * 标的ID
     */
    private String loanName;
    /**
     * 投资金额
     */
    private long amount;
    /**
     * 投资人手机号
     */
    private String mobile;
    /**
     * 投资人邮箱
     */
    private String email;

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
