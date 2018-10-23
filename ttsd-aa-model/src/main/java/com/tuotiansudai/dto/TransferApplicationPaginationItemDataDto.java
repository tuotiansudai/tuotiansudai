package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferApplicationRecordView;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class TransferApplicationPaginationItemDataDto implements Serializable {
    private String transferApplicationId;
    private String transferAmount;
    private String investAmount;
    private Date transferTime;
    private String transferStatus;
    private long loanId;
    private String transferrerMobile;
    private String leftPeriod;
    private String leftDays;
    private String transfereeMobile;
    private String transferFee;
    private Date deadLine;
    private String name;
    private String sumRate;
    private double baseRate;
    private double activityRate;
    private Date applicationTime;
    private Source source;
    private String contractNo;
    private boolean transferNewSuccess;
    private boolean transferOldSuccess;
    private boolean cancelTransfer;
    private Date transferInvestTime;


    public TransferApplicationPaginationItemDataDto(){}

    public TransferApplicationPaginationItemDataDto(TransferApplicationRecordView transferApplicationRecordView){
        this.transferApplicationId = String.valueOf(transferApplicationRecordView.getTransferApplicationId());
        this.loanId = transferApplicationRecordView.getLoanId();
        this.transferAmount = AmountConverter.convertCentToString(transferApplicationRecordView.getTransferAmount());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationRecordView.getInvestAmount());
        this.transferrerMobile = transferApplicationRecordView.getTransferrerMobile();
        this.transfereeMobile = transferApplicationRecordView.getTransfereeMobile();
        this.transferFee = AmountConverter.convertCentToString(transferApplicationRecordView.getTransferFee());
        this.transferTime = transferApplicationRecordView.getTransferTime();
        this.deadLine = transferApplicationRecordView.getDeadLine();
        this.leftPeriod = String.valueOf(transferApplicationRecordView.getLeftPeriod());
        this.name = transferApplicationRecordView.getName();
        this.sumRate = transferApplicationRecordView.getSumRatePercent();
        this.baseRate = transferApplicationRecordView.getBaseRate()*100;
        this.activityRate = transferApplicationRecordView.getActivityRate()*100;
        this.transferStatus = TransferStatus.getTransferStatus(transferApplicationRecordView.getTransferStatus()).name();
        this.applicationTime = transferApplicationRecordView.getApplicationTime();
        this.source = transferApplicationRecordView.getSource();
        this.transferInvestTime = transferApplicationRecordView.getTransferInvestTime();
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
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

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getTransferrerMobile() {
        return transferrerMobile;
    }

    public void setTransferrerMobile(String transferrerMobile) {
        this.transferrerMobile = transferrerMobile;
    }

    public String getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(String leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getTransfereeMobile() {
        return transfereeMobile;
    }

    public void setTransfereeMobile(String transfereeMobile) {
        this.transfereeMobile = transfereeMobile;
    }

    public String getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(String transferFee) {
        this.transferFee = transferFee;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSumRate() {
        return sumRate;
    }

    public void setSumRate(String sumRate) {
        this.sumRate = sumRate;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(double activityRate) {
        this.activityRate = activityRate;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }
    
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public boolean isTransferNewSuccess() {
        return transferNewSuccess;
    }

    public void setTransferNewSuccess(boolean transferNewSuccess) {
        this.transferNewSuccess = transferNewSuccess;
    }

    public boolean isTransferOldSuccess() {
        return transferOldSuccess;
    }

    public void setTransferOldSuccess(boolean transferOldSuccess) {
        this.transferOldSuccess = transferOldSuccess;
    }

    public boolean isCancelTransfer() {
        return cancelTransfer;
    }

    public void setCancelTransfer(boolean cancelTransfer) {
        this.cancelTransfer = cancelTransfer;
    }

    public Date getTransferInvestTime() {
        return transferInvestTime;
    }

    public void setTransferInvestTime(Date transferInvestTime) {
        this.transferInvestTime = transferInvestTime;
    }
}
