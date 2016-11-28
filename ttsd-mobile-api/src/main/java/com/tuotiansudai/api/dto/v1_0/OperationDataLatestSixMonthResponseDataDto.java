package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class OperationDataLatestSixMonthResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "月份", example = "1月")
    private String name;
    @ApiModelProperty(value = "金额,单位为分", example = "293939393")
    private String amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
