package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class AnxinSignSwitchSkipAuthRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "开通免验", example = "true：是")
    boolean open;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}