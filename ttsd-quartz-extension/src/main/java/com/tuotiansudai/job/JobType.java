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
    DragonBoatSendPKPrize("端午节活动结束后发PK体验金"),
    DelayMessageDelivery("延迟消息投递");

    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
