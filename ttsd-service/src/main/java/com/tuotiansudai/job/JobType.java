package com.tuotiansudai.job;

/**
 *
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 *
 *
 *
 *
 * 在此枚举中添加字段时，请同步添加到 ttsd-job-worker/src/main/resources/job-worker.properties.deploy/job-worker-all.properties 里
 *
 * 请不要将 OverInvestPayBack 添加到上述文件中
 *
 *
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 */
public enum JobType {
    Default("默认类别"),
    LoanStatusToRaising("标的状态从预热转为可投资"),
    LoanOut("放款后续处理"),
    AutoInvest("自动投资"),
    OverInvestPayBack("超投还款"),
    LoanStatusToRecheck("标的状态从筹款变为等待复核"),
    CalculateDefaultInterest("计算逾期罚息"),
    AutoReFreshAreaByMobile("获取手机归属地"),
    AutoJPushAlertBirthMonth("每月1日推送当月生日用户"),
    AutoJPushAlertBirthDay("用户生日当天"),
    AutoJPushNoInvestAlert("持续30天未投资，提醒一次"),
    AutoJPushAlertLoanOut("用户所投标的放款"),
    AutoJPushLotteryObtainCashAlert("抽奖获取现金推送"),
    NormalRepay("正常还款"),
    LoanRepayNotify("还款提醒"),
    AdvanceRepay("提前还款"),
    AutoLoanOut("满标自动放款"),
    SendRedEnvelope("放款发红包"),
    CouponNotify("优惠券通知"),
    GetPushReport("获取推送统计数据"),
    ManualJPushAlert("手动推送"),
    BirthdayNotify("生日月用户短信提醒"),
    LotteryTransferCash("抽奖现金发放"),
    ImitateLottery("模拟抽奖"),
    AutoJPushRepayAlert("用户资金变动推送-还款"),
    AutoJPushRechargeAlert("用户资金变动推送-充值"),
    AutoJPushWithDrawApplyAlert("用户资金变动推送-申请提现成功"),
    AutoJPushWithDrawAlert("用户资金变动推送-提现成功"),
    CheckUserBalanceMonthly("用户余额对账"),
    AutoJPushReferrerRewardAlert("用户资金变动推送-推荐人奖励");


    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
