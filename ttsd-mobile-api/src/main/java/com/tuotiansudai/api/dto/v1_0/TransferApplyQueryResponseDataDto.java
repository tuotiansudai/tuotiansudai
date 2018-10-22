package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class TransferApplyQueryResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "项目本金", example = "1000")
    private String investAmount;

    @ApiModelProperty(value = "手续费", example = "10")
    private String transferFee;

    @ApiModelProperty(value = "转让截止时间", example = "2016-11-25 15:23:11")
    private String deadLine;

    @ApiModelProperty(value = "折价上限", example = "10000")
    private String discountUpper;

    @ApiModelProperty(value = "折价下限", example = "9000")
    private String discountLower;
    @ApiModelProperty(value = "转让价格,单位(元)", example = "9000")
    private String transferAmount;

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }


    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getDiscountUpper() {
        return discountUpper;
    }

    public void setDiscountUpper(String discountUpper) {
        this.discountUpper = discountUpper;
    }

    public String getDiscountLower() {
        return discountLower;
    }

    public void setDiscountLower(String discountLower) {
        this.discountLower = discountLower;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }
}
