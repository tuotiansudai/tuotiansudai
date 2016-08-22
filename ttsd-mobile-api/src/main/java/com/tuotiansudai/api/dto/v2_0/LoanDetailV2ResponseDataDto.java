package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.EvidenceResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ExtraLoanRateDto;
import com.tuotiansudai.repository.model.ActivityType;

import java.util.Date;
import java.util.List;

public class LoanDetailV2ResponseDataDto extends BaseResponseDataDto {

    private Long loanId;//标的id

    private String loanType;//产品线：SYL("速盈利"),WYX("稳盈绣"),JYF("久盈富");

    private String loanName;//标的名称

    private String repayTypeCode;//还款方式代码

    private String repayTypeName;//还款方式名称

    private String interestPointName;//计息方式

    private int periods;//项目期限

    private String raisingPeriod;//募集天数

    private String repayUnit;//期限单位

    private String ratePercent;//总年化受益

    private String loanMoney;//借款总额

    private String loanStatus;//项目状态代码 //recheck:等待复核,repaying:回款中,raising:抢投,complete:已完成。

    private String loanStatusDesc;//项目状态描述

    private String investedMoney;//已投金额

    private String baseRatePercent;//基本利率

    private String activityRatePercent;//活动利率

    private String declaration;//声明

    private String verifyTime;//发布日期

    private Date fundRaisingEndTime;//募集截至日期

    private String remainTime;//剩余时间

    private String investBeginTime;//起投时间

    private String investBeginSeconds;//剩余秒数

    private String minInvestMoney;//起投金额

    private String cardinalNumber;//递增金额

    private String maxInvestMoney;//投资上限

    private Long investedCount;//已投人数

    private String raiseCompletedTime;//募集完成时间

    private LoanerDto loaner;//借款详情

    private PledgeHouseDto pledgeHouse;

    private PledgeVehicleDto pledgeVehicle;

    private String investFeeRate;//手续费比例

    private String marqueeTitle;

    private String title;

    private String content;

    private Integer duration;

    private String productNewType;

    private String deadline;

    public String minInvestMoneyCent;

    public String cardinalNumberCent;

    public String maxInvestMoneyCent;

    public String investedMoneyCent;

    public String loanMoneyCent;

    private List<ExtraLoanRateDto> extraRates;

    private List<EvidenceResponseDataDto> evidence;

    private ActivityType activityType;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getRepayTypeCode() {
        return repayTypeCode;
    }

    public void setRepayTypeCode(String repayTypeCode) {
        this.repayTypeCode = repayTypeCode;
    }

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }

    public String getInterestPointName() {
        return interestPointName;
    }

    public void setInterestPointName(String interestPointName) {
        this.interestPointName = interestPointName;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public String getRaisingPeriod() {
        return raisingPeriod;
    }

    public void setRaisingPeriod(String raisingPeriod) {
        this.raisingPeriod = raisingPeriod;
    }

    public String getRepayUnit() {
        return repayUnit;
    }

    public void setRepayUnit(String repayUnit) {
        this.repayUnit = repayUnit;
    }

    public String getRatePercent() {
        return ratePercent;
    }

    public void setRatePercent(String ratePercent) {
        this.ratePercent = ratePercent;
    }

    public String getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(String loanMoney) {
        this.loanMoney = loanMoney;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getLoanStatusDesc() {
        return loanStatusDesc;
    }

    public void setLoanStatusDesc(String loanStatusDesc) {
        this.loanStatusDesc = loanStatusDesc;
    }

    public String getInvestedMoney() {
        return investedMoney;
    }

    public void setInvestedMoney(String investedMoney) {
        this.investedMoney = investedMoney;
    }

    public String getBaseRatePercent() {
        return baseRatePercent;
    }

    public void setBaseRatePercent(String baseRatePercent) {
        this.baseRatePercent = baseRatePercent;
    }

    public String getActivityRatePercent() {
        return activityRatePercent;
    }

    public void setActivityRatePercent(String activityRatePercent) {
        this.activityRatePercent = activityRatePercent;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getFundRaisingEndTime() {
        return fundRaisingEndTime;
    }

    public void setFundRaisingEndTime(Date fundRaisingEndTime) {
        this.fundRaisingEndTime = fundRaisingEndTime;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public String getInvestBeginTime() {
        return investBeginTime;
    }

    public void setInvestBeginTime(String investBeginTime) {
        this.investBeginTime = investBeginTime;
    }

    public String getInvestBeginSeconds() {
        return investBeginSeconds;
    }

    public void setInvestBeginSeconds(String investBeginSeconds) {
        this.investBeginSeconds = investBeginSeconds;
    }

    public String getMinInvestMoney() {
        return minInvestMoney;
    }

    public void setMinInvestMoney(String minInvestMoney) {
        this.minInvestMoney = minInvestMoney;
    }

    public String getCardinalNumber() {
        return cardinalNumber;
    }

    public void setCardinalNumber(String cardinalNumber) {
        this.cardinalNumber = cardinalNumber;
    }

    public String getMaxInvestMoney() {
        return maxInvestMoney;
    }

    public void setMaxInvestMoney(String maxInvestMoney) {
        this.maxInvestMoney = maxInvestMoney;
    }

    public Long getInvestedCount() {
        return investedCount;
    }

    public void setInvestedCount(Long investedCount) {
        this.investedCount = investedCount;
    }

    public String getRaiseCompletedTime() {
        return raiseCompletedTime;
    }

    public void setRaiseCompletedTime(String raiseCompletedTime) {
        this.raiseCompletedTime = raiseCompletedTime;
    }

    public LoanerDto getLoaner() {
        return loaner;
    }

    public void setLoaner(LoanerDto loaner) {
        this.loaner = loaner;
    }

    public PledgeHouseDto getPledgeHouse() {
        return pledgeHouse;
    }

    public void setPledgeHouse(PledgeHouseDto pledgeHouse) {
        this.pledgeHouse = pledgeHouse;
    }

    public PledgeVehicleDto getPledgeVehicle() {
        return pledgeVehicle;
    }

    public void setPledgeVehicle(PledgeVehicleDto pledgeVehicle) {
        this.pledgeVehicle = pledgeVehicle;
    }

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public List<EvidenceResponseDataDto> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceResponseDataDto> evidence) {
        this.evidence = evidence;
    }

    public String getMarqueeTitle() {
        return marqueeTitle;
    }

    public void setMarqueeTitle(String marqueeTitle) {
        this.marqueeTitle = marqueeTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public List<ExtraLoanRateDto> getExtraRates() {
        return extraRates;
    }

    public void setExtraRates(List<ExtraLoanRateDto> extraRates) {
        this.extraRates = extraRates;
    }

    public String getMinInvestMoneyCent() {
        return minInvestMoneyCent;
    }

    public void setMinInvestMoneyCent(String minInvestMoneyCent) {
        this.minInvestMoneyCent = minInvestMoneyCent;
    }

    public String getCardinalNumberCent() {
        return cardinalNumberCent;
    }

    public void setCardinalNumberCent(String cardinalNumberCent) {
        this.cardinalNumberCent = cardinalNumberCent;
    }

    public String getMaxInvestMoneyCent() {
        return maxInvestMoneyCent;
    }

    public void setMaxInvestMoneyCent(String maxInvestMoneyCent) {
        this.maxInvestMoneyCent = maxInvestMoneyCent;
    }

    public String getInvestedMoneyCent() {
        return investedMoneyCent;
    }

    public void setInvestedMoneyCent(String investedMoneyCent) {
        this.investedMoneyCent = investedMoneyCent;
    }

    public String getLoanMoneyCent() {
        return loanMoneyCent;
    }

    public void setLoanMoneyCent(String loanMoneyCent) {
        this.loanMoneyCent = loanMoneyCent;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
