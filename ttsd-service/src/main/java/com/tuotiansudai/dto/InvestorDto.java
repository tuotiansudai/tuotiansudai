package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;

public class InvestorDto implements Serializable {

    private long balance;

    private boolean autoInvest;

    private boolean noPasswordInvest;

    private boolean remindNoPassword;

    private String maxAvailableInvestAmount = "0";

    private boolean skipAuth;

    private boolean anxinUser;

    public InvestorDto(AccountModel accountModel, boolean remindNoPassword, long maxAvailableInvestAmount, boolean skipAuth, boolean isAnxinUser) {
        if (accountModel != null) {
            this.balance = accountModel.getBalance();
            this.autoInvest = accountModel.isAutoInvest();
            this.noPasswordInvest = accountModel.isNoPasswordInvest();
            this.remindNoPassword = remindNoPassword;
            this.maxAvailableInvestAmount = AmountConverter.convertCentToString(maxAvailableInvestAmount);
            this.skipAuth = skipAuth;
            this.anxinUser = isAnxinUser;
        }
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

    public boolean isSkipAuth() {
        return skipAuth;
    }

    public void setSkipAuth(boolean skipAuth) {
        this.skipAuth = skipAuth;
    }

    public boolean isAnxinUser() {
        return anxinUser;
    }

    public void setAnxinUser(boolean anxinUser) {
        this.anxinUser = anxinUser;
    }
}
