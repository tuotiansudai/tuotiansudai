package com.tuotiansudai.api.dto;


import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.util.List;

public class AutoInvestPlanRequestDto extends BaseParamDto {
    @Pattern(regexp = "^\\d+$",message = "0023")
    private String autoPlanId;
    @NotEmpty(message = "0063")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String minInvestAmount;
    @NotEmpty(message = "0064")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String maxInvestAmount;
    @NotEmpty(message = "0065")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$",message = "0023")
    private String retentionAmount;
    @NotEmpty(message = "0066")
    private List<AutoInvestPeriodDto> autoInvestPeriods;

    private boolean enabled;

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
                autoInvestPeriods += Integer.parseInt(autoInvestPeriodDto.getPid());
            }
            autoInvestPlanModel.setAutoInvestPeriods(autoInvestPeriods);
        }
        return autoInvestPlanModel;

    }




}
