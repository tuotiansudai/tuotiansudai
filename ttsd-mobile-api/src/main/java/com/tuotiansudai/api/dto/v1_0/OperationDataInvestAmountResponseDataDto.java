package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.OperationDataInvestAmountDataDto;
import io.swagger.annotations.ApiModelProperty;

public class OperationDataInvestAmountResponseDataDto extends BaseResponseDataDto {
    @ApiModelProperty(value = "城市名称", example = "北京")
    private String city;
    @ApiModelProperty(value = "所占比例", example = "70%")
    private String scale;

    public OperationDataInvestAmountResponseDataDto() {
    }

    public OperationDataInvestAmountResponseDataDto(OperationDataInvestAmountDataDto operationDataInvestAmountDataDto) {
        this.city = operationDataInvestAmountDataDto.getCity();
        this.scale = operationDataInvestAmountDataDto.getScale();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
