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
    NormalRepay("正常还款"),
    LoanRepayNotify("还款提醒"),
    AdvanceRepay("提前还款"),
    AutoLoanOut("满标自动放款"),
    SendRedEnvelope("放款发红包"),
    CouponNotify("优惠券通知");


    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
