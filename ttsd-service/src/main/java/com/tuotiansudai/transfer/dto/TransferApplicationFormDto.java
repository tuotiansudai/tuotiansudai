package com.tuotiansudai.transfer.dto;

import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class TransferApplicationFormDto implements Serializable {

    private long transferInvestId;

    private String investAmount;

    private String transferAmountLower;

    private double transferFeeRate;

    private String transferFee;

    private Date expiredDate;

    private int holdDays;

    public TransferApplicationFormDto(long transferInvestId, long investAmount, long transferAmountLower, double transferFeeRate, long transferFee, Date expiredDate, int holdDays) {
        this.transferInvestId = transferInvestId;
        this.investAmount = AmountConverter.convertCentToString(investAmount);
        this.transferAmountLower = AmountConverter.convertCentToString(transferAmountLower);
        this.transferFee = AmountConverter.convertCentToString(transferFee);
        this.transferFeeRate = transferFeeRate;
        this.expiredDate = expiredDate;
        this.holdDays = holdDays;
    }

    public long getTransferInvestId() {
        return transferInvestId;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public String getTransferAmountLower() {
        return transferAmountLower;
    }

    public double getTransferFeeRate() {
        return transferFeeRate;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public int getHoldDays() {
        return holdDays;
    }
}
