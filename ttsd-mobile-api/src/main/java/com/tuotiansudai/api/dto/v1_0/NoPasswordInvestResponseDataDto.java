package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

public class NoPasswordInvestResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "是否开通联动优势免密出借协议", example = "true")
    private boolean autoInvest;

    @ApiModelProperty(value = "是否开通免密出借功能", example = "true")
    private boolean noPasswordInvest;

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        this.noPasswordInvest = noPasswordInvest;
    }
}
