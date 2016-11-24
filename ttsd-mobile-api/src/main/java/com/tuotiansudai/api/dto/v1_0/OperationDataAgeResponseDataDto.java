package com.tuotiansudai.api.dto.v1_0;

public class OperationDataAgeResponseDataDto extends BaseResponseDataDto {
    private String name;
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
