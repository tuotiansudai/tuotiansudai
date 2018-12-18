package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class UserInvestRepayResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "11")
    private String loanId;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "基本利率", example = "10")
    private String baseRate;

    @ApiModelProperty(value = "活动利率", example = "2")
    private String activityRate;

    @ApiModelProperty(value = "借款天数", example = "2")
    private String duration;

    @ApiModelProperty(value = "生息类别", example = "INTEREST_START_AT_INVEST")
    private String interestInitiateType;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360,EXPERIENCE")
    private String productNewType;

    @ApiModelProperty(value = "出借ID", example = "1001")
    private String investId;

    @ApiModelProperty(value = "出借金额", example = "100")
    private String investAmount;

    @ApiModelProperty(value = "预计收益", example = "10")
    private String expectedInterest;

    @ApiModelProperty(value = "已收收益", example = "0")
    private String actualInterest;

    @ApiModelProperty(value = "出借时间", example = "2016-11-25 16:12:01")
    private String investTime;

    @ApiModelProperty(value = "起息日", example = "2016-11-25 16:12:01")
    private String recheckTime;

    @ApiModelProperty(value = "到期日", example = "2016-12-25 16:12:01")
    private String lastRepayDate;

    @ApiModelProperty(value = "已付收益", example = "5")
    private String unPaidRepay;

    @ApiModelProperty(value = "会员等级", example = "2")
    private String membershipLevel;

    @ApiModelProperty(value = "服务费折扣描述", example = "服务费七折")
    private String serviceFeeDesc;

    @ApiModelProperty(value = "所有优惠券", example = "[1%加息券]")
    private List<String> usedCoupons = new ArrayList<>();

    @ApiModelProperty(value = "回款记录", example = "list")
    private List<InvestRepayDataDto> investRepays = new ArrayList<>();

    @ApiModelProperty(value = "CFCA合同", example = "/contract/invest/contractNo/JK20170822000085681")
    private String contractLocation;

    public UserInvestRepayResponseDataDto(LoanModel loanModel, TransferApplicationModel transferApplicationModel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.loanId = String.valueOf(transferApplicationModel.getLoanId());
        this.loanName = transferApplicationModel.getName();
        this.baseRate = String.valueOf(loanModel.getBaseRate() * 100);
        this.activityRate = String.valueOf(loanModel.getActivityRate() * 100);
        this.duration = String.valueOf(loanModel.getDuration());
        this.interestInitiateType = loanModel.getType().getInterestInitiateType().name();
        this.productNewType = loanModel.getProductType().name();
        this.investId = String.valueOf(transferApplicationModel.getInvestId());
        this.investAmount = AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount());
        this.investTime = simpleDateFormat.format(transferApplicationModel.getTransferTime());
        this.recheckTime = simpleDateFormat.format(loanModel.getRecheckTime());
    }

    public UserInvestRepayResponseDataDto(LoanModel loanModel, InvestModel investModel){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.loanId = String.valueOf(loanModel.getId());
        this.loanName = loanModel.getName();
        this.baseRate = String.valueOf(decimalFormat.format(loanModel.getBaseRate() * 100));
        this.activityRate = String.valueOf(decimalFormat.format(loanModel.getActivityRate() * 100));
        this.duration = String.valueOf(loanModel.getDuration());
        this.interestInitiateType = loanModel.getType().getInterestInitiateType().name();
        this.productNewType = loanModel.getProductType().name();
        this.investId = String.valueOf(investModel.getId());
        this.investAmount = AmountConverter.convertCentToString(investModel.getAmount());
        this.investTime = sdf.format(investModel.getInvestTime());
    }


    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getInterestInitiateType() {
        return interestInitiateType;
    }

    public void setInterestInitiateType(String interestInitiateType) {
        this.interestInitiateType = interestInitiateType;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getInvestId() {
        return investId;
    }

    public void setInvestId(String investId) {
        this.investId = investId;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public String getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(String recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public List<InvestRepayDataDto> getInvestRepays() {
        return investRepays;
    }

    public void setInvestRepays(List<InvestRepayDataDto> investRepays) {
        this.investRepays = investRepays;
    }

    public String getUnPaidRepay() {
        return unPaidRepay;
    }

    public void setUnPaidRepay(String unPaidRepay) {
        this.unPaidRepay = unPaidRepay;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public List<String> getUsedCoupons() {
        return usedCoupons;
    }

    public void setUsedCoupons(List<String> usedCoupons) {
        this.usedCoupons = usedCoupons;
    }

    public String getServiceFeeDesc() {
        return serviceFeeDesc;
    }

    public void setServiceFeeDesc(String serviceFeeDesc) {
        this.serviceFeeDesc = serviceFeeDesc;
    }

    public String getContractLocation() {
        return contractLocation;
    }

    public void setContractLocation(String contractLocation) {
        this.contractLocation = contractLocation;
    }
}
