package com.ttsd.api.dto;


public class InvestRequestDto extends BaseParamDto{
    private String userId;
    private double investMoney;
    private String loanId;
    private String password;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(double investMoney) {
        this.investMoney = investMoney;
    }
}
