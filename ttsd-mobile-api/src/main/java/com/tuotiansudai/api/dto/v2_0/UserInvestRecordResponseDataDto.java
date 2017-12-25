package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestStatus;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.List;

public class UserInvestRecordResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "标的ID", example = "10001")
    private String loanId;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "投资ID", example = "1001")
    private String investId;

    @ApiModelProperty(value = "投资金额", example = "1000")
    private String investAmount;

    @ApiModelProperty(value = "投资时间", example = "2016-11-25 18:10:01")
    private String investTime;

    @ApiModelProperty(value = "投资状态", example = "BID_SUCCESS")
    private InvestStatus investStatus;

    @ApiModelProperty(value = "投资状态描述", example = "投资成功")
    private String investStatusDesc;

    @ApiModelProperty(value = "预计利息", example = "100")
    private String expectedInterest;

    @ApiModelProperty(value = "已收利息", example = "20")
    private String actualInterest;

    @ApiModelProperty(value = "到期日利息", example = "2016-11-25 18:10:01")
    private String lastRepayDate;

    @ApiModelProperty(value = "债券转让状态", example = "TRANSFERABLE(可转让),SUCCESS(已转让)")
    private String transferStatus;

    @ApiModelProperty(value = "称号", example = "FIRST_INVEST(首投),MAX_AMOUNT(标王),LAST_INVEST(尾投)")
    private List<InvestAchievement> achievements;

    @ApiModelProperty(value = "红包类型", example = "RED_ENVELOPE(投资红包),NEWBIE_COUPON(新手体验券),INVEST_COUPON(投资体验券),INTEREST_COUPON(加息券),BIRTHDAY_COUPON(生日福利)")
    private List<CouponType> userCoupons;

    @ApiModelProperty(value = "是否使用券", example = "true")
    private boolean usedCoupon;

    @ApiModelProperty(value = "是否使用红包", example = "true")
    private boolean usedRedEnvelope;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360,EXPERIENCE")
    private String productNewType;

    @ApiModelProperty(value = "投资加息", example = "10")
    private String extraRate;

    @ApiModelProperty(value = "活动描述", example = "普通投资")
    private String activityDesc;

    @ApiModelProperty(value = "抵押类型", example = "HOUSE:房标,车标:VEHICLE,无抵押物:NONE")
    private PledgeType pledgeType;

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

    public String getInvestTime() {
        return investTime;
    }

    public void setInvestTime(String investTime) {
        this.investTime = investTime;
    }

    public InvestStatus getInvestStatus() {
        return investStatus;
    }

    public void setInvestStatus(InvestStatus investStatus) {
        this.investStatus = investStatus;
    }

    public String getInvestStatusDesc() {
        return investStatusDesc;
    }

    public void setInvestStatusDesc(String investStatusDesc) {
        this.investStatusDesc = investStatusDesc;
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

    public String getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(String lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public List<InvestAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<InvestAchievement> achievements) {
        this.achievements = achievements;
    }

    public List<CouponType> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<CouponType> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public boolean isUsedCoupon() {
        return usedCoupon;
    }

    public void setUsedCoupon(boolean usedCoupon) {
        this.usedCoupon = usedCoupon;
    }

    public boolean isUsedRedEnvelope() {
        return usedRedEnvelope;
    }

    public void setUsedRedEnvelope(boolean usedRedEnvelope) {
        this.usedRedEnvelope = usedRedEnvelope;
    }

    public String getProductNewType() { return productNewType; }

    public void setProductNewType(String productNewType) { this.productNewType = productNewType; }

    public String getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(String extraRate) {
        this.extraRate = extraRate;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public UserInvestRecordResponseDataDto() {

    }

    public UserInvestRecordResponseDataDto(InvestModel invest, LoanModel loan, LoanDetailsModel loanDetailsModel) {
        InvestStatus investStatus = InvestStatus.convertInvestStatus(invest.getStatus());
        this.loanId = String.valueOf(invest.getLoanId());
        this.loanName = loan.getName();
        this.investId = String.valueOf(invest.getId());
        this.investAmount = AmountConverter.convertCentToString(invest.getAmount());
        this.investTime = new DateTime(invest.getTradingTime() == null ? invest.getCreatedTime() : invest.getTradingTime()).toString("yyyy-MM-dd");
        this.investStatus = investStatus;
        this.investStatusDesc = investStatus.getMessage();
        this.achievements = invest.getAchievements();
        this.activityDesc = loanDetailsModel != null ? loanDetailsModel.getActivityDesc() : "";
        this.pledgeType = loan.getPledgeType();
    }
}
