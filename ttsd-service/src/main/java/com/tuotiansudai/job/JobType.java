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
    OverInvestPayBack("超投还款");

    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
