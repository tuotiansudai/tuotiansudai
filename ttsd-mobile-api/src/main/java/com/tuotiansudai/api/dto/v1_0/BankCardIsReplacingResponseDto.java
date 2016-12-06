package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class BankCardIsReplacingResponseDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "已经申请更换银行卡", example = "true")
    private boolean isReplacing;

    @ApiModelProperty(value = "账户余额和待收回款为0", example = "false")
    private boolean isManual;

    public BankCardIsReplacingResponseDto(boolean isReplacing, boolean isManual) {
        this.isReplacing = isReplacing;
        this.isManual = isManual;
    }

    public boolean isReplacing() {
        return isReplacing;
    }

    public void setIsReplacing(boolean isReplacing) {
        this.isReplacing = isReplacing;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }
}
