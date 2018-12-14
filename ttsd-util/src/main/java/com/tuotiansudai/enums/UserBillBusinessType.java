package com.tuotiansudai.enums;

public enum UserBillBusinessType {
    RECHARGE_SUCCESS("充值成功"),
    WITHDRAW_SUCCESS("提现成功"),
    WITHDRAW_FAIL("提现失败"),
    INVEST_SUCCESS("出借成功"),
    INVEST_TRANSFER_OUT("债权转让"),
    INVEST_TRANSFER_IN("债权购买"),
    LOAN_SUCCESS("放款成功"),
    NORMAL_REPAY("正常还款"),
    ADVANCE_REPAY("提前还款"),
    OVERDUE_REPAY("逾期还款"),
    OVERDUE_INTEREST("逾期罚息"),
    INVEST_FEE("利息管理费"),
    OVERDUE_INTEREST_FEE("逾期罚息管理费"),
    TRANSFER_FEE("债权转让管理费"),
    ACTIVITY_REWARD("活动奖励"),
    REFERRER_REWARD("推荐奖励"),
    APPLY_WITHDRAW("申请提现"),
    OVER_INVEST_PAYBACK("超投返款"),
    CANCEL_INVEST_PAYBACK("流标返款"),
    ADMIN_INTERVENTION("管理员干预"),
    NEWBIE_COUPON("新手体验券"),
    INVEST_COUPON("出借体验券"),
    INTEREST_COUPON("加息优惠券"),
    BIRTHDAY_COUPON("生日福利"),
    RED_ENVELOPE("出借红包"),
    SYSTEM_RECHARGE("平台充值"),
    LOTTERY_CASH("抽奖现金"),
    EXTRA_RATE("出借奖励"),
    EXPERIENCE_INTEREST("体验金收益"),
    INVEST_CASH_BACK("现金补贴"),
    NATIONAL_DAY_INVEST("国庆节逢万返百奖励"),
    MEMBERSHIP_PRIVILEGE_PURCHASE("增值特权购买"),
    HUI_ZU_REPAY_IN("慧租租金还款"),
    CREDIT_LOAN_ACTIVATE_ACCOUNT("慧租一分钱激活账户"),
    CREDIT_LOAN_TRANSFER_AGENT("发放代理人账户"),
    CREDIT_LOAN_RECHARGE("信用贷账户充值"),
    CREDIT_LOAN_OUT("信用贷放款"),
    CREDIT_LOAN_REPAY("信用贷还款"),
    LUXURY_STAGE_PURCHASE("分期首付+第一期月供"),
    LUXURY_STAGE_DOWN_PAYMENT("分期首付"),
    LUXURY_STAGE_REPAY("分期还款"),
    PAYROLL("代发工资"),
    ;

    private final String description;

    UserBillBusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
