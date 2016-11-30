package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class PointResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "积分", example = "10")
    long point;

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }
}
