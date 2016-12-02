package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.Date;

public class TransferTransfereeRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "承接人", example = "wangtuotian")
    private String transfereeLoginName;

    @ApiModelProperty(value = "承接金额", example = "100")
    private String transferAmount;

    @ApiModelProperty(value = "/承接时间", example = "2016-11-25 19:19:01")
    private String transferTime;

    public TransferTransfereeRecordResponseDataDto() {

    }

    public TransferTransfereeRecordResponseDataDto(String transfereeLoginName, long transferAmount, Date transferTime) {
        this.transfereeLoginName = transfereeLoginName;
        this.transferAmount = AmountConverter.convertCentToString(transferAmount);
        this.transferTime = transferTime == null ? "" : new DateTime(transferTime).toString("yyyy-MM-dd HH:mm:ss");
    }

    public String getTransfereeLoginName() {
        return transfereeLoginName;
    }

    public void setTransfereeLoginName(String transfereeLoginName) {
        this.transfereeLoginName = transfereeLoginName;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime;
    }

}
