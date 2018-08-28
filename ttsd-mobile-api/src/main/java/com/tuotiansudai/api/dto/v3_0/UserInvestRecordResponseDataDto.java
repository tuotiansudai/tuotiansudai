package com.tuotiansudai.api.dto.v3_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.InvestStatus;
import com.tuotiansudai.dto.UserInvestRecordDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;

import java.util.List;

public class UserInvestRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "10001")
    private String loanId;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "投资ID", example = "1001")
    private String investId;

    @ApiModelProperty(value = "申请债权转让ID", example = "201")
    private String transferApplicationId;

    @ApiModelProperty(value = "投资金额", example = "1000")
    private String investAmount;

    @ApiModelProperty(value = "投资时间", example = "2016-11-25 18:10:01")
    private String investTime;

    @ApiModelProperty(value = "投资状态", example = "BID_SUCCESS")
    private InvestStatus investStatus;

    @ApiModelProperty(value = "预计利息", example = "100")
    private String expectedInterest;

    @ApiModelProperty(value = "已收利息", example = "20")
    private String actualInterest;

    @ApiModelProperty(value = "到期日", example = "2016-11-25 18:10:01")
    private String lastRepayDate;

    @ApiModelProperty(value = "债权转让状态", example = "TRANSFERABLE(可转让),SUCCESS(已转让)")
    private String transferStatus;

    @ApiModelProperty(value = "称号", example = "FIRST_INVEST(首投),MAX_AMOUNT(标王),LAST_INVEST(尾投)")
    private List<InvestAchievement> achievements;

    @ApiModelProperty(value = "是否使用券", example = "true")
    private boolean usedCoupon;

    @ApiModelProperty(value = "是否使用红包", example = "true")
    private boolean usedRedEnvelope;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360,EXPERIENCE")
    private String productNewType;

    @ApiModelProperty(value = "投资加息", example = "10")
    private String extraRate;

    @ApiModelProperty(value = "抵押类型", example = "HOUSE:房标,车标:VEHICLE,无抵押物:NONE")
    private PledgeType pledgeType;

    @ApiModelProperty(value = "是否债权转让", example = "true")
    private boolean isTransferInvest;

    @ApiModelProperty(value = "还款进度", example = "100")
    private int repayProgress;

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

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getExtraRate() {
        return extraRate;
    }

    public void setExtraRate(String extraRate) {
        this.extraRate = extraRate;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(String transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public boolean isTransferInvest() {
        return isTransferInvest;
    }

    public void setTransferInvest(boolean transferInvest) {
        isTransferInvest = transferInvest;
    }

    public int getRepayProgress() {
        return repayProgress;
    }

    public void setRepayProgress(int repayProgress) {
        this.repayProgress = repayProgress;
    }

    public UserInvestRecordResponseDataDto() {

    }

    public UserInvestRecordResponseDataDto(UserInvestRecordDataDto userInvestRecordDataDto) {
        InvestStatus investStatus = InvestStatus.convertInvestStatus(userInvestRecordDataDto.getInvestStatus());
        this.loanId = String.valueOf(userInvestRecordDataDto.getLoanId());
        this.loanName = userInvestRecordDataDto.getLoanName();
        this.investId = userInvestRecordDataDto.getInvestId();
        this.investAmount = userInvestRecordDataDto.getInvestAmount();
        this.investTime = userInvestRecordDataDto.getInvestTime();
        this.investStatus = investStatus;
        this.expectedInterest = userInvestRecordDataDto.getExpectedInterest();
        this.actualInterest = userInvestRecordDataDto.getActualInterest();
        this.lastRepayDate = userInvestRecordDataDto.getLastRepayDate();
        this.transferStatus = userInvestRecordDataDto.getTransferStatus();
        this.achievements = userInvestRecordDataDto.getAchievements();
        this.usedCoupon = userInvestRecordDataDto.isUsedCoupon();
        this.usedRedEnvelope = userInvestRecordDataDto.isUsedRedEnvelope();
        this.productNewType = userInvestRecordDataDto.getProductNewType();
        this.extraRate = userInvestRecordDataDto.getExtraRate();
        this.pledgeType = userInvestRecordDataDto.getPledgeType();
        this.isTransferInvest = userInvestRecordDataDto.isTransferInvest();
        this.repayProgress = userInvestRecordDataDto.getRepayProgress();
        this.transferApplicationId=userInvestRecordDataDto.getTransferApplicationId();
    }
}
