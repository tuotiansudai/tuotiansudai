package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;
import java.util.List;

public class AutoInvestPlanDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "唯一标识该自动投标计划", example = "12345")
    private String autoPlanId;

    @ApiModelProperty(value = "用户", example = "admin")
    private String loginName;

    @ApiModelProperty(value = "最小出借金额", example = "100.00")
    private String minInvestAmount;

    @ApiModelProperty(value = "最大出借金额", example = "10000.00")
    private String maxInvestAmount;

    @ApiModelProperty(value = "保留金额", example = "200.00")
    private String retentionAmount;

    @ApiModelProperty(value = "选中项目期限", example = "")
    private List<AutoInvestPeriodDto> autoInvestPeriods;

    @ApiModelProperty(value = "自动投标状态", example = "true")
    private boolean enabled ;

    @ApiModelProperty(value = "计划创建时间", example = "2015-11-17 20:06:45")
    private String createdTime;

    public String getAutoPlanId() {
        return autoPlanId;
    }

    public void setAutoPlanId(String autoPlanId) {
        this.autoPlanId = autoPlanId;
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
        this.autoPlanId = "" + autoInvestPlanModel.getId();
        this.loginName = autoInvestPlanModel.getLoginName();
        this.minInvestAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getMaxInvestAmount());
        this.retentionAmount = AmountConverter.convertCentToString(autoInvestPlanModel.getRetentionAmount());
        this.enabled = autoInvestPlanModel.isEnabled();
        this.createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(autoInvestPlanModel.getCreatedTime());
        this.autoInvestPeriods = AutoInvestPeriodDto.generateSelectedAutoInvestPeriodDto(autoInvestPlanModel.getAutoInvestPeriods());
    }

}
