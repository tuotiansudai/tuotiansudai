package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class JpushRequestDto extends BaseParamDto {

    @NotEmpty(message = "0023")
    @ApiModelProperty(value = "jpushId", example = "123456789")
    private String jpushId;

    public String getJpushId() {
        return jpushId;
    }

    public void setJpushId(String jpushId) {
        this.jpushId = jpushId;
    }
}
