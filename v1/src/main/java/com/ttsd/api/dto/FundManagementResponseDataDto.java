package com.ttsd.api.dto;

public class FundManagementResponseDataDto extends BaseResponseDataDto {
    private double totalAssets;
    private double totalInvestment;
    private double expectedTotalInterest;
    private double receivedCorpus;
    private double receivedInterest;
    private double receivableCorpus;
    private double receivableInterest;
    private double receivableCorpusInterest;
    private double accountBalance;
    private double availableMoney;
    private double frozenMoney;
    private double paidRechargeMoney;
    private double successWithdrawMoney;

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public double getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(double frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public double getPaidRechargeMoney() {
        return paidRechargeMoney;
    }

    public void setPaidRechargeMoney(double paidRechargeMoney) {
        this.paidRechargeMoney = paidRechargeMoney;
    }

    public double getSuccessWithdrawMoney() {
        return successWithdrawMoney;
    }

    public void setSuccessWithdrawMoney(double successWithdrawMoney) {
        this.successWithdrawMoney = successWithdrawMoney;
    }

    public double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public double getExpectedTotalInterest() {
        return expectedTotalInterest;
    }

    public void setExpectedTotalInterest(double expectedTotalInterest) {
        this.expectedTotalInterest = expectedTotalInterest;
    }

    public double getReceivedCorpus() {
        return receivedCorpus;
    }

    public void setReceivedCorpus(double receivedCorpus) {
        this.receivedCorpus = receivedCorpus;
    }

    public double getReceivedInterest() {
        return receivedInterest;
    }

    public void setReceivedInterest(double receivedInterest) {
        this.receivedInterest = receivedInterest;
    }

    public double getReceivableCorpus() {
        return receivableCorpus;
    }

    public void setReceivableCorpus(double receivableCorpus) {
        this.receivableCorpus = receivableCorpus;
    }

    public double getReceivableInterest() {
        return receivableInterest;
    }

    public void setReceivableInterest(double receivableInterest) {
        this.receivableInterest = receivableInterest;
    }

    public double getReceivableCorpusInterest() {
        return receivableCorpusInterest;
    }

    public void setReceivableCorpusInterest(double receivableCorpusInterest) {
        this.receivableCorpusInterest = receivableCorpusInterest;
    }
}
