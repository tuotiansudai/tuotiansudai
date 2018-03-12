package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.OperationDataAgeDataDto;
import com.tuotiansudai.dto.OperationDataLoanerAgeDataDto;
import io.swagger.annotations.ApiModelProperty;

public class OperationDataLoanerAgeResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "年龄阶段", example = "20岁以下")
    private String name;
    @ApiModelProperty(value = "所占比例", example = "39%")
    private String scale;

    public OperationDataLoanerAgeResponseDataDto() {
    }

    public OperationDataLoanerAgeResponseDataDto(OperationDataLoanerAgeDataDto operationDataLoanerAgeDataDto) {
        this.name = operationDataLoanerAgeDataDto.getName();
        this.scale = operationDataLoanerAgeDataDto.getScale();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
