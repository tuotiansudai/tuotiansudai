package com.tuotiansudai.fudian.message;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BankQueryUserMessage extends BankBaseMessage {

    private String bankUserName;

    private String bankAccountName;

    private boolean loanInvestAuthorization;

    private boolean loanRepayAuthorization;

    private long balance;

    private long withdrawBalance;

    private long freezeBalance;

    private String identityCode;

    private String userStatus;

    public BankQueryUserMessage() {
    }

    public BankQueryUserMessage(boolean status, String message) {
        super(status, message);
    }

    public BankQueryUserMessage(String bankUserName, String bankAccountName, String authorization, long balance, long withdrawBalance, long freezeBalance, String identityCode, String userStatus) {
        super(true, null);
        this.bankUserName = bankUserName;
        this.bankAccountName =bankAccountName;
        if (!Strings.isNullOrEmpty(authorization)) {
            JsonObject jsonObject = new JsonParser().parse(authorization).getAsJsonObject();
            this.loanInvestAuthorization = "0000".equals(jsonObject.get("loanInvest").getAsString());
            this.loanRepayAuthorization = "0000".equals(jsonObject.get("loanRepay").getAsString());
        }
        this.balance = balance;
        this.withdrawBalance = withdrawBalance;
        this.freezeBalance = freezeBalance;
        this.identityCode = identityCode;
        this.userStatus = Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("1", "正常")
                .put("2", "冻结")
                .put("3", "挂失")
                .put("4", "销户")
                .build()).get(userStatus);
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public boolean isLoanInvestAuthorization() {
        return loanInvestAuthorization;
    }

    public boolean isLoanRepayAuthorization() {
        return loanRepayAuthorization;
    }

    public long getBalance() {
        return balance;
    }

    public long getWithdrawBalance() {
        return withdrawBalance;
    }

    public long getFreezeBalance() {
        return freezeBalance;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public String getUserStatus() {
        return userStatus;
    }
}