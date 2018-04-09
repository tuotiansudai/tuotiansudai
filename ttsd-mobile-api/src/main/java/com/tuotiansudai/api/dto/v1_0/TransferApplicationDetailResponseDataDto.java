package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.dto.TransferApplicationDetailDto;
import io.swagger.annotations.ApiModelProperty;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransferApplicationDetailResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "债权转让ID", example = "1")
    private String transferApplicationId;

    @ApiModelProperty(value = "名称", example = "债权转让")
    private String name;

    @ApiModelProperty(value = "转让人", example = "wangtuotian")
    private String transferrer;

    @ApiModelProperty(value = "标的ID", example = "11")
    private String loanId;

    @ApiModelProperty(value = "标的类型", example = "_30")
    private String productType;

    @ApiModelProperty(value = "标的类型名称", example = "30天")
    private String productTypeName;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "利率类型", example = "LOAN_INTEREST_MONTHLY_REPAY")
    private String loanType;

    @ApiModelProperty(value = "投资金额", example = "100")
    private String investAmount;

    @ApiModelProperty(value = "债权转让金额", example = "1")
    private String transferAmount;

    @ApiModelProperty(value = "基本利率", example = "10")
    private String baseRate;

    @ApiModelProperty(value = "活动利率", example = "2")
    private String activityRate;

    @ApiModelProperty(value = "剩余期数", example = "1")
    private int leftPeriod;

    @ApiModelProperty(value = "剩余天数", example = "30")
    private String leftDays;

    @ApiModelProperty(value = "截止时间", example = "2016-11-25 16:02:01")
    private String deadline;

    @ApiModelProperty(value = "预计收益", example = "10")
    private String expecedInterest;

    @ApiModelProperty(value = "转让状态", example = "SUCCESS")
    private String transferStatus;

    @ApiModelProperty(value = "截止倒计时", example = "100")
    private String countdown;

    @ApiModelProperty(value = "用户分级", example = "CONSERVATIVE STEADY POSITIVE")
    private List<String> estimates;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

    public String getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public List<String> getEstimates() {
        return estimates;
    }

    public void setEstimates(List<String> estimates) {
        this.estimates = estimates;
    }

    public TransferApplicationDetailResponseDataDto(TransferApplicationDetailDto transferApplicationDetailDto) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.transferApplicationId = String.valueOf(transferApplicationDetailDto.getId());
        this.name = transferApplicationDetailDto.getName();
        this.transferrer = transferApplicationDetailDto.getTransferrer();
        this.loanId = String.valueOf(transferApplicationDetailDto.getLoanId());
        this.productType = transferApplicationDetailDto.getProductType().name();
        this.productTypeName = transferApplicationDetailDto.getProductType().getName();
        this.loanName = transferApplicationDetailDto.getLoanName();
        this.loanType = transferApplicationDetailDto.getLoanType() == null ? "" : transferApplicationDetailDto.getLoanType();
        this.investAmount = transferApplicationDetailDto.getInvestAmount();
        this.transferAmount = transferApplicationDetailDto.getTransferAmount();
        this.baseRate = decimalFormat.format(transferApplicationDetailDto.getBaseRate());
        this.activityRate = decimalFormat.format(transferApplicationDetailDto.getActivityRate());
        this.leftPeriod = transferApplicationDetailDto.getLeftPeriod();
        this.expecedInterest = transferApplicationDetailDto.getExpecedInterest();
        //app取得是这个字段
        this.deadline = sdf.format(transferApplicationDetailDto.getDueDate());
        this.transferStatus = transferApplicationDetailDto.getTransferStatus().name();
        this.countdown = String.valueOf(transferApplicationDetailDto.getCountdown());
    }
}
