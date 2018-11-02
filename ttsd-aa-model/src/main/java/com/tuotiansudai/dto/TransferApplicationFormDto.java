package com.tuotiansudai.dto;

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

    private boolean anxinUser;

    private boolean anxinAuthenticationRequired;

    private String transferAmount;

    public TransferApplicationFormDto(long transferInvestId, long investAmount, long transferAmountLower, double transferFeeRate, long transferFee, Date expiredDate, int holdDays, boolean isAnxinUser, boolean isAnxinAuthenticationRequired,long transferAmount) {
        this.transferInvestId = transferInvestId;
        this.investAmount = AmountConverter.convertCentToString(investAmount);
        this.transferAmountLower = AmountConverter.convertCentToString(transferAmountLower);
        this.transferFee = AmountConverter.convertCentToString(transferFee);
        this.transferFeeRate = transferFeeRate;
        this.expiredDate = expiredDate;
        this.holdDays = holdDays;
        this.anxinUser = isAnxinUser;
        this.anxinAuthenticationRequired = isAnxinAuthenticationRequired;
        this.transferAmount=AmountConverter.convertCentToString(transferAmount);
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

    public boolean isAnxinUser() {
        return anxinUser;
    }

    public boolean isAnxinAuthenticationRequired() {
        return anxinAuthenticationRequired;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }
}
