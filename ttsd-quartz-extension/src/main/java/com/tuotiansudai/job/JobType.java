package com.tuotiansudai.job;

/**
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 * TODO: 所有人请注意
 *
 * 在此枚举中添加字段时，请同步添加到 ttsd-job-worker/src/main/resources/job-worker.properties.  deploy/job-worker-all.properties 里
 *
 *
 */
public enum JobType {
    Default("默认类别"),
    DelayMessageDelivery("延迟消息投递"),

    //unused job
    AutoJPushNoInvestAlert(""),
    AutoJPushAlertBirthDay(""),
    OverInvestPayBack(""),
    NormalRepayCallBack("正常还款回调处理"),
    AdvanceRepayCallBack("提前还款回调处理");


    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
