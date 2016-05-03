package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;

import java.util.Date;

public class TransferApplicationRecodesDto extends BaseDataDto {

    private long transferApplicationId;

    private String transferApplicationReceiver;

    private String receiveAmount;

    private Source source;

    private String expecedInterest;

    private String investAmount;

    private Date transferTime;

    public TransferApplicationRecodesDto() {
    }

    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public String getTransferApplicationReceiver() {
        return transferApplicationReceiver;
    }

    public void setTransferApplicationReceiver(String transferApplicationReceiver) {
        this.transferApplicationReceiver = transferApplicationReceiver;
    }

    public String getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(String receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getExpecedInterest() {
        return expecedInterest;
    }

    public void setExpecedInterest(String expecedInterest) {
        this.expecedInterest = expecedInterest;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public Date getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

}
