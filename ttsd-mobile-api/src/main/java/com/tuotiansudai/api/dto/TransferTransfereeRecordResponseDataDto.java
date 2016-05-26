package com.tuotiansudai.api.dto;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.util.Date;

public class TransferTransfereeRecordResponseDataDto extends BaseResponseDataDto {

    private String transfereeLoginName;

    private String transferAmount;

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
