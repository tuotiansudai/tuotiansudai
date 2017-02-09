package com.tuotiansudai.dto;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class InvestorDto implements Serializable {

    private long balance;

    private boolean autoInvest;

    private boolean noPasswordInvest;

    private boolean remindNoPassword;

    private String maxAvailableInvestAmount = "0";

    private boolean authenticationRequired;

    private boolean anxinUser;

    public InvestorDto() {
    }

    public InvestorDto(long balance, boolean autoInvest, boolean noPasswordInvest, boolean remindNoPassword, long maxAvailableInvestAmount, boolean authenticationRequired, boolean isAnxinUser) {
        this.balance = balance;
        this.autoInvest = autoInvest;
        this.noPasswordInvest = noPasswordInvest;
        this.remindNoPassword = remindNoPassword;
        this.maxAvailableInvestAmount = AmountConverter.convertCentToString(maxAvailableInvestAmount);
        this.authenticationRequired = authenticationRequired;
        this.anxinUser = isAnxinUser;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public boolean isRemindNoPassword() {
        return remindNoPassword;
    }

    public String getMaxAvailableInvestAmount() {
        return maxAvailableInvestAmount;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public boolean isAnxinUser() {
        return anxinUser;
    }
}
