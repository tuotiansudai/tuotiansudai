package com.tuotiansudai.api.dto;


import com.tuotiansudai.dto.TransferApplicationDetailDto;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferApplicationDetailResponseDataDto extends BaseResponseDataDto {

    private String transferApplicationId;
    private String name;
    private String transferrer;
    private String loanId;
    private String productType;
    private String productTypeName;
    private String loanName;
    private String loanType;
    private String investAmount;
    private String transferAmount;
    private String baseRate;
    private String activityRate;
    private int leftPeriod;
    private String deadline;
    private String expecedInterest;
    private String transferStatus;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransferrer() {
        return transferrer;
    }

    public void setTransferrer(String transferrer) {
        this.transferrer = transferrer;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public int getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(int leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getExpecedInterest() {
        return expecedInterest;
    }

    public void setExpecedInterest(String expecedInterest) {
        this.expecedInterest = expecedInterest;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public TransferApplicationDetailResponseDataDto(TransferApplicationDetailDto transferApplicationDetailDto) {
        this.transferApplicationId = String.valueOf(transferApplicationDetailDto.getId());
        this.name = transferApplicationDetailDto.getName();
        this.transferrer = transferApplicationDetailDto.getLoginName();
        this.loanId = String.valueOf(transferApplicationDetailDto.getLoanId());
        this.productType = transferApplicationDetailDto.getProductType().name();
        this.productTypeName = transferApplicationDetailDto.getProductType().getName();
        this.loanName = transferApplicationDetailDto.getLoanName();
        this.loanType = transferApplicationDetailDto.getLoanType();
        this.investAmount = transferApplicationDetailDto.getInvestAmount();
        this.transferAmount = transferApplicationDetailDto.getTransferAmount();
        this.baseRate = String.valueOf(transferApplicationDetailDto.getBaseRate());
        this.activityRate = String.valueOf(transferApplicationDetailDto.getActivityRate());
        this.leftPeriod = transferApplicationDetailDto.getLeftPeriod();
        this.expecedInterest = transferApplicationDetailDto.getExpecedInterest();
        this.deadline = sdf.format(transferApplicationDetailDto.getDeadLine());
        this.transferStatus = transferApplicationDetailDto.getTransferStatus().name();
    }
}
