package com.tuotiansudai.diagnosis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "diagnosis")
public class DiagnosisConfig {
    private ScheduleConfig schedule;
    private ReportConfig report;

    public ScheduleConfig getSchedule() {
        return schedule;
    }

    public void setSchedule(ScheduleConfig schedule) {
        this.schedule = schedule;
    }

    public ReportConfig getReport() {
        return report;
    }

    public void setReport(ReportConfig report) {
        this.report = report;
    }


    public static class ScheduleConfig {
        private int hour;
        private int minute;

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }
    }

    public static class ReportConfig {
        private String notifyMailFrom;
        private String[] notifyMailAddress;
        private String[] notifyMailAddressQa;

        public String getNotifyMailFrom() {
            return notifyMailFrom;
        }

        public void setNotifyMailFrom(String notifyMailFrom) {
            this.notifyMailFrom = notifyMailFrom;
        }

        public String[] getNotifyMailAddress() {
            return notifyMailAddress;
        }

        public void setNotifyMailAddress(String[] notifyMailAddress) {
            this.notifyMailAddress = notifyMailAddress;
        }

        public String[] getNotifyMailAddressQa() {
            return notifyMailAddressQa;
        }

        public void setNotifyMailAddressQa(String[] notifyMailAddressQa) {
            this.notifyMailAddressQa = notifyMailAddressQa;
        }
    }
}
