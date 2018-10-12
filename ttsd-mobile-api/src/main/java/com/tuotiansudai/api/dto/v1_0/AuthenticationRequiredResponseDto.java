package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AuthenticationRequiredResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "出借是否需要验证安心签", example = "list")
    private boolean anxinSign;

    public AuthenticationRequiredResponseDto(boolean anxinSign) {
        this.anxinSign = anxinSign;
    }

    public boolean isAnxinSign() {
        return anxinSign;
    }
}
