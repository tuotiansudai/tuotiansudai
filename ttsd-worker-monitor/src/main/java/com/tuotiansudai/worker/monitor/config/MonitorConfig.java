package com.tuotiansudai.worker.monitor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "monitor")
public class MonitorConfig {
    private String name;
    private int healthCheckIntervalSeconds;
    private int maxSilenceSeconds;
    private boolean smsNotifyEnabled;
    private boolean emailNotifyEnabled;
    private String emailNotifySender;
    private String[] emailNotifyRecipients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthCheckIntervalSeconds() {
        return healthCheckIntervalSeconds;
    }

    public void setHealthCheckIntervalSeconds(int healthCheckIntervalSeconds) {
        this.healthCheckIntervalSeconds = healthCheckIntervalSeconds;
    }

    public int getMaxSilenceSeconds() {
        return maxSilenceSeconds;
    }

    public void setMaxSilenceSeconds(int maxSilenceSeconds) {
        this.maxSilenceSeconds = maxSilenceSeconds;
    }


    public boolean isSmsNotifyEnabled() {
        return smsNotifyEnabled;
    }

    public void setSmsNotifyEnabled(boolean smsNotifyEnabled) {
        this.smsNotifyEnabled = smsNotifyEnabled;
    }

    public boolean isEmailNotifyEnabled() {
        return emailNotifyEnabled;
    }

    public void setEmailNotifyEnabled(boolean emailNotifyEnabled) {
        this.emailNotifyEnabled = emailNotifyEnabled;
    }

    public String getEmailNotifySender() {
        return emailNotifySender;
    }

    public void setEmailNotifySender(String emailNotifySender) {
        this.emailNotifySender = emailNotifySender;
    }

    public String[] getEmailNotifyRecipients() {
        return emailNotifyRecipients;
    }

    public void setEmailNotifyRecipients(String[] emailNotifyRecipients) {
        this.emailNotifyRecipients = emailNotifyRecipients;
    }
}
