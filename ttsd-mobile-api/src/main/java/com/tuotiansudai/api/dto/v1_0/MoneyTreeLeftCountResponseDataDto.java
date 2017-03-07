package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class MoneyTreeLeftCountResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "剩余摇奖次数", example = "1")
    private int leftCount;

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }
}
