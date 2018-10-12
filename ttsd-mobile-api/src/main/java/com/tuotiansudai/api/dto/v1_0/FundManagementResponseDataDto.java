package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class FundManagementResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "资产总额", dataType = "10000")
    private String totalAssets;

    @ApiModelProperty(value = "累计出借额", dataType = "10000")
    private String totalInvestment;

    @ApiModelProperty(value = "累计收益", dataType = "1000")
    private String expectedTotalInterest;

    @ApiModelProperty(value = "已收本金", dataType = "300")
    private String receivedCorpus;

    @ApiModelProperty(value = "已收利息", dataType = "1000")
    private String receivedInterest;

    @ApiModelProperty(value = "待收本金", dataType = "9300")
    private String receivableCorpus;

    @ApiModelProperty(value = "待收利息", dataType = "700")
    private String receivableInterest;

    @ApiModelProperty(value = "待收本息", dataType = "0")
    private String receivableCorpusInterest;

    @ApiModelProperty(value = "账户余额", dataType = "10000")
    private String accountBalance;

    @ApiModelProperty(value = "可用金额", dataType = "300")
    private String availableMoney;

    @ApiModelProperty(value = "冻结金额", dataType = "0")
    private String frozenMoney;

    @ApiModelProperty(value = "累计充值", dataType = "9000")
    private String paidRechargeMoney;

    @ApiModelProperty(value = "累计提现", dataType = "0")
    private String successWithdrawMoney;

    @ApiModelProperty(value = "优惠券个数", dataType = "10")
    private String usableUserCouponCount;

    @ApiModelProperty(value = "我的财豆数量", dataType = "10000")
    private String point;

    @ApiModelProperty(value = "推荐奖励", dataType = "800")
    private String rewardAmount;

    @ApiModelProperty(value = "用户会员等级", dataType = "1")
    private String membershipLevel;

    @ApiModelProperty(value = "用户会员过期日", dataType = "2016-11-23")
    private String membershipExpiredDate;

    @ApiModelProperty(value = "户会员成长值", dataType = "1000")
    private String membershipPoint;

    @ApiModelProperty(value = "可用金额", dataType = "1999")
    private String availableMoneyCent;

    @ApiModelProperty(value = "是否显示摇钱树", dataType = "1")
    private String showMoneyTree;


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

    public String getRewardAmount() { return rewardAmount; }

    public void setRewardAmount(String rewardAmount) { this.rewardAmount = rewardAmount; }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public String getMembershipPoint() {
        return membershipPoint;
    }

    public void setMembershipPoint(String membershipPoint) {
        this.membershipPoint = membershipPoint;
    }

    public String getMembershipExpiredDate() {
        return membershipExpiredDate;
    }

    public void setMembershipExpiredDate(String membershipExpiredDate) {
        this.membershipExpiredDate = membershipExpiredDate;
    }

    public String getAvailableMoneyCent() {
        return availableMoneyCent;
    }

    public void setAvailableMoneyCent(String availableMoneyCent) {
        this.availableMoneyCent = availableMoneyCent;
    }

    public String getShowMoneyTree() {
        return showMoneyTree;
    }

    public void setShowMoneyTree(String showMoneyTree) {
        this.showMoneyTree = showMoneyTree;
    }
}
