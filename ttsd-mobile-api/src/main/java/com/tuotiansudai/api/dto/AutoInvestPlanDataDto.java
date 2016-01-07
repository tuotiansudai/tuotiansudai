package com.tuotiansudai.api.dto;

public class AutoInvestPlanDataDto extends BaseResponseDataDto{
    private String pid;
    private String loginName;
    private String minInvestAmount;
    private String maxInvestAmount;
    private AutoInvestPeriodDto[] autoInvestPeriods;
    private boolean enabled ;
    private String createdTime;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public AutoInvestPeriodDto[] getAutoInvestPeriods() {
        return autoInvestPeriods;
    }

    public void setAutoInvestPeriods(AutoInvestPeriodDto[] autoInvestPeriods) {
        this.autoInvestPeriods = autoInvestPeriods;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
