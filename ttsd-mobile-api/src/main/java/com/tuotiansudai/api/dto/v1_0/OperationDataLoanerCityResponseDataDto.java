package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.OperationDataLoanerCityDataDto;
import io.swagger.annotations.ApiModelProperty;

public class OperationDataLoanerCityResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "城市名称", example = "北京")
    private String city;
    @ApiModelProperty(value = "所占比例", example = "60%")
    private String scale;

    public OperationDataLoanerCityResponseDataDto() {
    }

    public OperationDataLoanerCityResponseDataDto(OperationDataLoanerCityDataDto operationDataLoanerCityDataDto) {
        this.city = operationDataLoanerCityDataDto.getCity();
        this.scale = operationDataLoanerCityDataDto.getScale();
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
