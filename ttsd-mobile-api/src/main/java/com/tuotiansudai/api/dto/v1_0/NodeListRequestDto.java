package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class NodeListRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "标示", example = "announcement")
    private String termId;

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }
}
