package com.tuotiansudai.api.dto;

public class FundManagementResponseDataDto extends BaseResponseDataDto {
    private String totalAssets;
    private String totalInvestment;
    private String expectedTotalInterest;
    private String receivedCorpus;
    private String receivedInterest;
    private String receivableCorpus;
    private String receivableInterest;
    private String receivableCorpusInterest;
    private String accountBalance;
    private String availableMoney;
    private String frozenMoney;
    private String paidRechargeMoney;
    private String successWithdrawMoney;
    private String usableUserCouponCount;
    private String point;

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public String getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(String totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public String getExpectedTotalInterest() {
        return expectedTotalInterest;
    }

    public void setExpectedTotalInterest(String expectedTotalInterest) { this.expectedTotalInterest = expectedTotalInterest; }

    public String getReceivedCorpus() {
        return receivedCorpus;
    }

    public void setReceivedCorpus(String receivedCorpus) {
        this.receivedCorpus = receivedCorpus;
    }

    public String getReceivedInterest() {
        return receivedInterest;
    }

    public void setReceivedInterest(String receivedInterest) {
        this.receivedInterest = receivedInterest;
    }

    public String getReceivableCorpus() {
        return receivableCorpus;
    }

    public void setReceivableCorpus(String receivableCorpus) {
        this.receivableCorpus = receivableCorpus;
    }

    public String getReceivableInterest() {
        return receivableInterest;
    }

    public void setReceivableInterest(String receivableInterest) {
        this.receivableInterest = receivableInterest;
    }

    public String getReceivableCorpusInterest() {
        return receivableCorpusInterest;
    }

    public void setReceivableCorpusInterest(String receivableCorpusInterest) { this.receivableCorpusInterest = receivableCorpusInterest; }

    public String getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(String accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(String availableMoney) {
        this.availableMoney = availableMoney;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getPaidRechargeMoney() {
        return paidRechargeMoney;
    }

    public void setPaidRechargeMoney(String paidRechargeMoney) {
        this.paidRechargeMoney = paidRechargeMoney;
    }

    public String getSuccessWithdrawMoney() {
        return successWithdrawMoney;
    }

    public void setSuccessWithdrawMoney(String successWithdrawMoney) { this.successWithdrawMoney = successWithdrawMoney; }

    public String getUsableUserCouponCount() { return usableUserCouponCount; }

    public void setUsableUserCouponCount(String usableUserCouponCount) { this.usableUserCouponCount = usableUserCouponCount; }

    public String getPoint() { return point; }

    public void setPoint(String point) { this.point = point; }
}
