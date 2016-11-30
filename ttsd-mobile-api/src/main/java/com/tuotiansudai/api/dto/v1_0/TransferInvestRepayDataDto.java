package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferInvestRepayDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "实际还款日", example = "2016/01/01")
    private String repayDate;

    @ApiModelProperty(value = "应收金额", example = "100")
    private String expectedInterest;

    @ApiModelProperty(value = "还款状态", example = "REPAYING(待还) ,COMPLETE(完成)")
    private String status;

    public TransferInvestRepayDataDto() {
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
