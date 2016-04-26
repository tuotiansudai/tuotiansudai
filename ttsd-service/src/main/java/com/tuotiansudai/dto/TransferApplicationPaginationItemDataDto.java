package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class TransferApplicationPaginationItemDataDto implements Serializable {
    private long transferApplicationId;
    private String transferAmount;
    private String investAmount;
    private Date transferTime;
    private TransferStatus transferStatus;
    private long loanId;
    private String transferrerLoginName;
    private String leftPeriod;
    private String transfereeLoginName;
    private String transferFee;
    private Date deadLine;

    public TransferApplicationPaginationItemDataDto(){}

    public TransferApplicationPaginationItemDataDto(TransferApplicationRecordDto transferApplicationRecordDto){
        this.transferApplicationId = transferApplicationRecordDto.getTransferApplicationId();
        this.loanId = transferApplicationRecordDto.getLoanId();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferAmount());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordDto.getInvestAmount());
        this.transferStatus = transferApplicationRecordDto.getTransferStatus();
        this.transferrerLoginName = transferApplicationRecordDto.getTransferrerLoginName();
        this.transfereeLoginName = transferApplicationRecordDto.getTransfereeLoginName();
        this.transferFee = AmountConverter.convertCentToString(transferApplicationRecordDto.getTransferFee());
        this.transferTime = transferApplicationRecordDto.getTransferTime();
        this.leftPeriod = String.valueOf(transferApplicationRecordDto.getLeftPeriod());
        this.deadLine = transferApplicationRecordDto.getDeadLine();
    }


    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
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

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferrerLoginName() {
        return transferrerLoginName;
    }

    public void setTransferrerLoginName(String transferrerLoginName) {
        this.transferrerLoginName = transferrerLoginName;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getTransfereeLoginName() {
        return transfereeLoginName;
    }

    public void setTransfereeLoginName(String transfereeLoginName) {
        this.transfereeLoginName = transfereeLoginName;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }
}
