package com.tuotiansudai.mq.config.setting;

import org.springframework.beans.factory.annotation.Value;

public class MessageConsumerSetting {

    @Value("${mq.message.pop.period.seconds}")
    private int messagePopPeriodSeconds;

    @Value("${mq.error.count.threshold}")
    private int errorCountThreshold;

    @Value("${mq.error.sleep.seconds}")
    private int errorSleepSeconds;

    public int getMessagePopPeriodSeconds() {
        return messagePopPeriodSeconds;
    }

    public void setMessagePopPeriodSeconds(int messagePopPeriodSeconds) {
        this.messagePopPeriodSeconds = messagePopPeriodSeconds;
    }

    public int getErrorCountThreshold() {
        return errorCountThreshold;
    }

    public void setErrorCountThreshold(int errorCountThreshold) {
        this.errorCountThreshold = errorCountThreshold;
    }

    public int getErrorSleepSeconds() {
        return errorSleepSeconds;
    }

    public void setErrorSleepSeconds(int errorSleepSeconds) {
        this.errorSleepSeconds = errorSleepSeconds;
    }
}
