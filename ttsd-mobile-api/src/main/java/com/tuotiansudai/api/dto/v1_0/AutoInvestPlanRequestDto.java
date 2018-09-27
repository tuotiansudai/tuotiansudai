package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;

public class AutoInvestPlanRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "唯一标识该自动投标计划", example = "12345")
    @Pattern(regexp = "^\\d+$",message = "0023")
    private String autoPlanId;

    @ApiModelProperty(value = "最小出借金额", example = "100.00")
    @NotEmpty(message = "0063")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String minInvestAmount;

    @ApiModelProperty(value = "最大出借金额", example = "10000.00")
    @NotEmpty(message = "0064")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String maxInvestAmount;

    @ApiModelProperty(value = "保留金额", example = "200.00")
    @NotEmpty(message = "0065")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String retentionAmount;

    @ApiModelProperty(value = "选中项目期限", example = "")
    @NotEmpty(message = "0066")
    private List<AutoInvestPeriodDto> autoInvestPeriods;

    @ApiModelProperty(value = "自动投标状态", example = "true")
    private boolean enabled;

    @ApiModelProperty(value = "ip", example = "127.0.0.1")
    private String ip;

    public String getAutoPlanId() {
        return autoPlanId;
    }

    public void setAutoPlanId(String autoPlanId) {
        this.autoPlanId = autoPlanId;
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

    public String getRetentionAmount() {
        return retentionAmount;
    }

    public void setRetentionAmount(String retentionAmount) {
        this.retentionAmount = retentionAmount;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public AutoInvestPlanModel convertDtoToModel(){
        AutoInvestPlanModel autoInvestPlanModel = new AutoInvestPlanModel();
        if(StringUtils.isNotEmpty(this.getAutoPlanId())){
            autoInvestPlanModel.setId(Long.parseLong(this.getAutoPlanId()));
        }
        autoInvestPlanModel.setLoginName(this.getBaseParam().getUserId());
        autoInvestPlanModel.setMinInvestAmount(AmountConverter.convertStringToCent(this.getMinInvestAmount()));
        autoInvestPlanModel.setMaxInvestAmount(AmountConverter.convertStringToCent(this.getMaxInvestAmount()));
        autoInvestPlanModel.setRetentionAmount(AmountConverter.convertStringToCent(this.getRetentionAmount()));
        autoInvestPlanModel.setEnabled(this.isEnabled());
        if(CollectionUtils.isNotEmpty(this.getAutoInvestPeriods())){
            int autoInvestPeriods = 0 ;
            for(AutoInvestPeriodDto autoInvestPeriodDto:this.getAutoInvestPeriods()){
                if(autoInvestPeriodDto.isSelected()){
                    autoInvestPeriods += Integer.parseInt(autoInvestPeriodDto.getPid());
                }
            }
            autoInvestPlanModel.setAutoInvestPeriods(autoInvestPeriods);
        }
        autoInvestPlanModel.setIp(this.getIp());
        return autoInvestPlanModel;

    }




}
