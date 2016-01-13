package com.tuotiansudai.api.dto;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import java.text.SimpleDateFormat;
import java.util.List;

public class AutoInvestPlanDataDto extends BaseResponseDataDto{
    private String id;
    private String loginName;
    private String minInvestAmount;
    private String maxInvestAmount;
    private String retentionAmount;
    private List<AutoInvestPeriodDto> autoInvestPeriods;
    private boolean enabled ;
    private String createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<AutoInvestPeriodDto> getAutoInvestPeriods() {
        return autoInvestPeriods;
    }

    public void setAutoInvestPeriods(List<AutoInvestPeriodDto> autoInvestPeriods) {
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

    public String getRetentionAmount() {
        return retentionAmount;
    }

    public void setRetentionAmount(String retentionAmount) {
        this.retentionAmount = retentionAmount;
    }

    public AutoInvestPlanDataDto(){

    }

    public AutoInvestPlanDataDto(AutoInvestPlanModel autoInvestPlanModel){
        this.id = "" + autoInvestPlanModel.getId();
        this.loginName = autoInvestPlanModel.getLoginName();
        this.minInvestAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getMaxInvestAmount());
        this.retentionAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getRetentionAmount());
        this.enabled = autoInvestPlanModel.isEnabled();
        this.createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(autoInvestPlanModel.getCreatedTime());
        this.autoInvestPeriods = AutoInvestPeriodDto.generateSelectedAutoInvestPeriodDto(autoInvestPlanModel.getAutoInvestPeriods());
    }

}
