package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class OperationDataAgeResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "年龄阶段", example = "20岁以下")
    private String name;
    @ApiModelProperty(value = "所占比例", example = "39%")
    private String scale;

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
