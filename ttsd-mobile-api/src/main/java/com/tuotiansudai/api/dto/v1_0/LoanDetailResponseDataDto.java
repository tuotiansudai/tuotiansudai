package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LoanDetailResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private String loanId;

    @ApiModelProperty(value = "产品线", example = "SYL")
    private String loanType;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;

    @ApiModelProperty(value = "还款方式代码", example = "LOAN_INTEREST_MONTHLY_REPAY")
    private String repayTypeCode;

    @ApiModelProperty(value = "还款方式名称", example = "先付收益后还出借本金，按天计息，放款后生息")
    private String repayTypeName;

    @ApiModelProperty(value = "项目期限", example = "12")
    private Integer periods;

    @ApiModelProperty(value = "期数", example = "12")
    private Integer deadline;

    @ApiModelProperty(value = "期限单位", example = "日")
    private String repayUnit;

    @ApiModelProperty(value = "总年化受益", example = "10")
    private String ratePercent;

    @ApiModelProperty(value = "借款总额", example = "10000")
    private String loanMoney;

    @ApiModelProperty(value = "项目状态代码", example = "recheck,repaying,raising,complete")
    private String loanStatus;

    @ApiModelProperty(value = "项目状态描述", example = "等待复核,回款中,筹款中,已完成")
    private String loanStatusDesc;

    @ApiModelProperty(value = "已投金额", example = "10000")
    private String investedMoney;

    @ApiModelProperty(value = "基础利率", example = "10")
    private String baseRatePercent;

    @ApiModelProperty(value = "活动利率", example = "10")
    private String activityRatePercent;

    @ApiModelProperty(value = "已投人数", example = "100")
    private Long investedCount;

    @ApiModelProperty(value = "借款详情", example = "HOUSE")
    private String loanDetail;

    @ApiModelProperty(value = "代理人", example = "test")
    private String agent;

    @ApiModelProperty(value = "借款人", example = "test")
    private String loaner;

    @ApiModelProperty(value = "发布日期", example = "2016-11-24 12:00:00")
    private String verifyTime;

    @ApiModelProperty(value = "剩余时间", example = "1天12时25分")
    private String remainTime;

    @ApiModelProperty(value = "起投时间", example = "2016-11-24 12:00:00")
    private String investBeginTime;

    @ApiModelProperty(value = "剩余秒数", example = "10")
    private String investBeginSeconds;

    @ApiModelProperty(value = "募集截至日期", example = "2016-11-24 12:00:00")
    private String fundRaisingEndTime;

    @ApiModelProperty(value = "起投金额", example = "50")
    private String minInvestMoney;

    @ApiModelProperty(value = "递增金额", example = "50")
    private String cardinalNumber;

    @ApiModelProperty(value = "出借上限", example = "100")
    private String maxInvestMoney;

    @ApiModelProperty(value = "募集完成时间", example = "2016-11-24 17:00:00")
    private String raiseCompletedTime;

    @ApiModelProperty(value = "计息方式", example = "先付收益后还出借本金")
    private String interestPointName;

    @ApiModelProperty(value = "手续费比例", example = "10")
    private String investFeeRate;

    @ApiModelProperty(value = "分享标题", example = "拓天速贷引领出借热，开启互金新概念")
    private String title;

    @ApiModelProperty(value = "分享内容", example = "个人经营借款理财项目，总额{0}元期限{1}{2}，年化利率{3}%，先到先抢！！")
    private String content;

    @ApiModelProperty(value = "借款天数", example = "360")
    private String duration;

    @ApiModelProperty(value = "募集天数", example = "360")
    private String raisingPeriod;

    @ApiModelProperty(value = "跑马灯内容", example = "第一个出借者将获得“拓荒先锋”称号及0.2％加息券＋50元红包")
    private String marqueeTitle;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360")
    private String productNewType;

    @ApiModelProperty(value = "活动利率", example = "list")
    private List<ExtraLoanRateDto> extraRates;

    @ApiModelProperty(value = "申请材料", example = "list")
    private List<EvidenceResponseDataDto> evidence;

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String extraSource;

    @ApiModelProperty(value = "标的类型", example = "NORMAL(普通出借),NEWBIE(新手专享),EXCLUSIVE(定向出借),PROMOTION(加息出借)")
    private String activityDesc;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
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

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public String getRepayUnit() {
        return repayUnit;
    }

    public void setRepayUnit(String repayUnit) {
        this.repayUnit = repayUnit;
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

    public Long getInvestedCount() {
        return investedCount;
    }

    public void setInvestedCount(Long investedCount) {
        this.investedCount = investedCount;
    }

    public String getLoanDetail() {
        return loanDetail;
    }

    public void setLoanDetail(String loanDetail) {
        this.loanDetail = loanDetail;
    }

    public List<EvidenceResponseDataDto> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceResponseDataDto> evidence) {
        this.evidence = evidence;
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

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getLoaner() {
        return loaner;
    }

    public void setLoaner(String loaner) {
        this.loaner = loaner;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
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

    public String getRaiseCompletedTime() {
        return raiseCompletedTime;
    }

    public void setRaiseCompletedTime(String raiseCompletedTime) {
        this.raiseCompletedTime = raiseCompletedTime;
    }

    public String getInterestPointName() {
        return interestPointName;
    }

    public void setInterestPointName(String interestPointName) {
        this.interestPointName = interestPointName;
    }

    public String getInvestFeeRate() {
        return investFeeRate;
    }

    public void setInvestFeeRate(String investFeeRate) {
        this.investFeeRate = investFeeRate;
    }

    public String getFundRaisingEndTime() {
        return fundRaisingEndTime;
    }

    public void setFundRaisingEndTime(String fundRaisingEndTime) {
        this.fundRaisingEndTime = fundRaisingEndTime;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
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

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public String getRaisingPeriod() { return raisingPeriod; }

    public void setRaisingPeriod(String raisingPeriod) { this.raisingPeriod = raisingPeriod; }

    public String getMarqueeTitle() {
        return marqueeTitle;
    }

    public void setMarqueeTitle(String marqueeTitle) {
        this.marqueeTitle = marqueeTitle;
    }

    public String getProductNewType() {
        return productNewType;
    }

    public void setProductNewType(String productNewType) {
        this.productNewType = productNewType;
    }

    public List<ExtraLoanRateDto> getExtraRates() {
        if (CollectionUtils.isEmpty(extraRates)) {
            return new ArrayList<>();
        }
        return extraRates;
    }

    public void setExtraRates(List<ExtraLoanRateDto> extraRates) {
        this.extraRates = extraRates;
    }

    public String getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(String extraSource) {
        this.extraSource = extraSource;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }
}
