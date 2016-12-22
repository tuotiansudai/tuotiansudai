package com.tuotiansudai.api.dto.v2_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.repository.model.UserFundView;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFundResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "可用余额", example = "1000")
    private long balance; //可用余额(分)

    @ApiModelProperty(value = "累计收益=已收投资收益+已收投资奖励(阶梯加息)+已收红包奖励+已收推荐奖励", example = "100")
    private long totalIncome; //累计收益=已收投资收益+已收投资奖励(阶梯加息)+已收红包奖励+已收推荐奖励

    @ApiModelProperty(value = "已收投资收益(分)", example = "100")
    private long actualTotalInterest; //已收投资收益(分)

    @ApiModelProperty(value = "已收投资奖励(阶梯加息)(分)", example = "10")
    private long actualTotalExtraInterest; //已收投资奖励(阶梯加息)(分)

    @ApiModelProperty(value = "已收推荐奖励(分)", example = "10")
    private long referRewardAmount; //已收推荐奖励(分)

    @ApiModelProperty(value = "已收红包奖励(分)", example = "10")
    private long redEnvelopeAmount; //已收红包奖励(分)

    @ApiModelProperty(value = "待收回款=待收投资本金+待收投资收益+待收投资奖励(阶梯加息)", example = "1")
    private long expectedTotalCorpusInterest; //待收回款=待收投资本金+待收投资收益+待收投资奖励(阶梯加息)

    @ApiModelProperty(value = "待收投资本金(分)", example = "1")
    private long expectedTotalCorpus; //待收投资本金(分)

    @ApiModelProperty(value = "待收投资收益(分)", example = "13")
    private long expectedTotalInterest; //待收投资收益(分)

    @ApiModelProperty(value = "待收投资奖励(阶梯加息)(分)", example = "0")
    private long expectedTotalExtraInterest; //待收投资奖励(阶梯加息)(分)

    @ApiModelProperty(value = "投资冻结资金(分)", example = "0")
    private long investFrozeAmount; //投资冻结资金(分)

    @ApiModelProperty(value = "提现冻结资金(分)", example = "0")
    private long withdrawFrozeAmount; //提现冻结资金(分)

    @ApiModelProperty(value = "本月回款笔数", example = "1")
    private long currentMonthInvestRepayCount; //本月回款笔数

    @ApiModelProperty(value = "可用优惠券个数", example = "0")
    private long usableUserCouponCount; //可用优惠券个数

    @ApiModelProperty(value = "会员等级", example = "0")
    private int membershipLevel; //会员等级

    @ApiModelProperty(value = "积分", example = "100")
    private long point; //积分

    @ApiModelProperty(value = "会员成长值", example = "10")
    private long membershipPoint; //会员成长值

    @ApiModelProperty(value = "用户会员过期日yyyy-MM-dd(空表示不过期)", example = "有效期至:2016-11-25")
    private String membershipExpiredDate; //用户会员过期日yyyy-MM-dd(空表示不过期)


    public UserFundResponseDataDto(UserFundView userFundView, long balance, long point, int membershipLevel, long membershipPoint, int usableUserCouponCount, Date membershipExpiredDate) {
        this.balance = balance;
        this.actualTotalInterest = userFundView.getActualTotalInterest();
        this.actualTotalExtraInterest = userFundView.getActualTotalExtraInterest();
        this.referRewardAmount = userFundView.getReferRewardAmount();
        this.redEnvelopeAmount = userFundView.getRedEnvelopeAmount();
        this.totalIncome = this.actualTotalInterest + this.actualTotalExtraInterest + this.referRewardAmount + this.redEnvelopeAmount;

        this.expectedTotalCorpus = userFundView.getExpectedTotalCorpus();
        this.expectedTotalInterest = userFundView.getExpectedTotalInterest();
        this.expectedTotalExtraInterest = userFundView.getExpectedTotalExtraInterest();
        this.expectedTotalCorpusInterest = this.expectedTotalCorpus + this.expectedTotalInterest + this.expectedTotalExtraInterest;

        this.investFrozeAmount = userFundView.getInvestFrozeAmount();
        this.withdrawFrozeAmount = userFundView.getWithdrawFrozeAmount();

        this.currentMonthInvestRepayCount = userFundView.getCurrentMonthInvestRepayCount();

        this.point = point;
        this.membershipLevel = membershipLevel;
        this.membershipPoint = membershipPoint;
        this.usableUserCouponCount = usableUserCouponCount;
        this.membershipExpiredDate = membershipExpiredDate != null ? "有效期至:" + new SimpleDateFormat("yyyy-mm-dd").format(membershipExpiredDate) : null;
    }

    public long getBalance() {
        return balance;
    }

    public long getTotalIncome() {
        return totalIncome;
    }

    public long getActualTotalInterest() {
        return actualTotalInterest;
    }

    public long getActualTotalExtraInterest() {
        return actualTotalExtraInterest;
    }

    public long getReferRewardAmount() {
        return referRewardAmount;
    }

    public long getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public long getExpectedTotalCorpusInterest() {
        return expectedTotalCorpusInterest;
    }

    public long getExpectedTotalCorpus() {
        return expectedTotalCorpus;
    }

    public long getExpectedTotalInterest() {
        return expectedTotalInterest;
    }

    public long getExpectedTotalExtraInterest() {
        return expectedTotalExtraInterest;
    }

    public long getInvestFrozeAmount() {
        return investFrozeAmount;
    }

    public long getWithdrawFrozeAmount() {
        return withdrawFrozeAmount;
    }

    public long getCurrentMonthInvestRepayCount() {
        return currentMonthInvestRepayCount;
    }

    public long getUsableUserCouponCount() {
        return usableUserCouponCount;
    }

    public int getMembershipLevel() {
        return membershipLevel;
    }

    public long getPoint() {
        return point;
    }

    public long getMembershipPoint() {
        return membershipPoint;
    }

    public Date getMembershipExpiredDate() {
        return membershipExpiredDate;
    }
}
