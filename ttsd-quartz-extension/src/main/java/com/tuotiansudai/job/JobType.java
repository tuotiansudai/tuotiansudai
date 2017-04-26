package com.tuotiansudai.job;

/**
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 *
 * 在此枚举中添加字段时，请同步添加到 ttsd-job-worker/src/main/resources/job-worker.properties.  deploy/job-worker-all.properties 里
 *
 * 请不要将 CouponRepayCallBack, ExtraRateRepayCallBack添加到上述文件中
 *
 */
public enum JobType {
    Default("默认类别"),
    LoanOut("放款后续处理"),
    AutoInvest("自动投资"),
    CouponRepayCallBack("还款时优惠券收益回调处理"),
    ExtraRateRepayCallBack("还款时阶梯加息收益回调处理"),
    CalculateDefaultInterest("计算逾期罚息"),
    AutoReFreshAreaByMobile("获取手机归属地"),
    NormalRepay("正常还款"),
    LoanRepayNotify("还款提醒"),
    AdvanceRepay("提前还款"),
    SendRedEnvelope("放款发红包"),
    BirthdayNotify("生日月用户短信提醒"),
    CheckUserBalanceMonthly("用户余额对账"),
    ExperienceRepay("新手体验标还款"),
    PlatformBalanceLowNotify("平台账户余额不足提醒"),
    EventMessage("每日事件触发消息"),
    CreateAnXinContract("创建安心签合同"),
    SendFirstRedEnvelopSplit("红包分裂第一期活动发红包"),
    SendSecondRedEnvelopSplit("红包分裂第二期活动发红包"),
    DelayMessageDelivery("延迟消息投递"),
    AssignMothersDayExperienceJob("母亲节奖励发放体验金");

    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
