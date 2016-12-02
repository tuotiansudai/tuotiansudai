package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.PledgeEnterpriseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class PledgeEnterpriseDto implements Serializable {

    @ApiModelProperty(value = "地址", example = "beijing")
    private String pledgeLocation;

    @ApiModelProperty(value = "估价", example = "10000")
    private String estimateAmount;

    @ApiModelProperty(value = "担保人", example = "王拓天")
    private String guarantee;

    public PledgeEnterpriseDto(){}

    public PledgeEnterpriseDto(PledgeEnterpriseModel pledgeEnterpriseModel){
        this.pledgeLocation = pledgeEnterpriseModel.getPledgeLocation();
        this.estimateAmount = pledgeEnterpriseModel.getEstimateAmount();
        this.guarantee = pledgeEnterpriseModel.getGuarantee();
    }

    public String getPledgeLocation() {
        return pledgeLocation;
    }

    public void setPledgeLocation(String pledgeLocation) {
        this.pledgeLocation = pledgeLocation;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }
}
