package com.tuotiansudai.dto;

public class ContractInvestDto {
    private String loginName;
    private String userName;
    private String identityNumber;
    private String investAmount;
    private String period;
    private String investTime;

    public ContractInvestDto(String loginName, String userName, String identityNumber, String investAmount, String period, String investTime) {
        this.loginName = loginName;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.investAmount = investAmount;
        this.period = period;
        this.investTime = investTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }
}
