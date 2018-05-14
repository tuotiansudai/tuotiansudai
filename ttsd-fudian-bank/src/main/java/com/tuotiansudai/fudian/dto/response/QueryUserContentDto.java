package com.tuotiansudai.fudian.dto.response;

public class QueryUserContentDto extends BaseContentDto {

    private String authorization;

    private String balance;

    private String withdrawBalance;

    private String freezeBalance;

    private String identityCode;

    private String status; // 1 代表账户状态正常，2 代表账户冻结，3 代表账户挂失，4 账户销户

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWithdrawBalance() {
        return withdrawBalance;
    }

    public void setWithdrawBalance(String withdrawBalance) {
        this.withdrawBalance = withdrawBalance;
    }

    public String getFreezeBalance() {
        return freezeBalance;
    }

    public void setFreezeBalance(String freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
