package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class InvestRepayListRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "查询状态", example = "paid 待收款明细、unpaid 已收款记录")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
