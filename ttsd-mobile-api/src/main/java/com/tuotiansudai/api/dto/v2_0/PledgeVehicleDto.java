package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.repository.model.PledgeVehicleModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class PledgeVehicleDto implements Serializable {

    @ApiModelProperty(value = "抵押车辆品牌", example = "BMW")
    private String brand;

    @ApiModelProperty(value = "车辆型号", example = "Q7")
    private String model;

    @ApiModelProperty(value = "车辆估值", example = "1000")
    private String estimateAmount;

    @ApiModelProperty(value = "借款公正金额", example = "1000")
    private String loanAmount;

    public PledgeVehicleDto(PledgeVehicleModel pledgeVehicleModel) {
        this.brand = pledgeVehicleModel.getBrand();
        this.model = pledgeVehicleModel.getModel();
        this.estimateAmount = pledgeVehicleModel.getEstimateAmount();
        this.loanAmount = pledgeVehicleModel.getLoanAmount();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }
}
