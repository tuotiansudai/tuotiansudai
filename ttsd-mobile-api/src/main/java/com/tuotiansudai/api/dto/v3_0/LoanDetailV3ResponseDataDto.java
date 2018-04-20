package com.tuotiansudai.api.dto.v3_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.EvidenceResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ExtraLoanRateDto;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.PledgeType;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class LoanDetailV3ResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "标的ID", example = "123456789")
    private Long loanId;//标的id

    @ApiModelProperty(value = "产品线", example = "SYL")
    private String loanType;//产品线：SYL("速盈利"),WYX("稳盈绣"),JYF("久盈富");

    @ApiModelProperty(value = "标的名称", example = "车辆抵押借款")
    private String loanName;//标的名称

    @ApiModelProperty(value = "还款方式代码", example = "LOAN_INTEREST_MONTHLY_REPAY")
    private String repayTypeCode;//还款方式代码

    @ApiModelProperty(value = "还款方式名称", example = "先付收益后还投资本金，按天计息，放款后生息")
    private String repayTypeName;//还款方式名称

    @ApiModelProperty(value = "计息方式", example = "先付收益后还投资本金")
    private String interestPointName;//计息方式

    @ApiModelProperty(value = "项目期限", example = "12")
    private int periods;//项目期限

    @ApiModelProperty(value = "募集天数", example = "360")
    private String raisingPeriod;//募集天数

    @ApiModelProperty(value = "期限单位", example = "日")
    private String repayUnit;//期限单位

    @ApiModelProperty(value = "总年化受益", example = "10")
    private String ratePercent;//总年化受益

    @ApiModelProperty(value = "借款总额", example = "10000")
    private String loanMoney;//借款总额

    @ApiModelProperty(value = "项目状态代码", example = "recheck,repaying,raising,complete")
    private String loanStatus;//项目状态代码 //recheck:等待复核,repaying:回款中,raising:抢投,complete:已完成。

    @ApiModelProperty(value = "项目状态描述", example = "等待复核,回款中,筹款中,已完成")
    private String loanStatusDesc;//项目状态描述

    @ApiModelProperty(value = "已投金额", example = "10000")
    private String investedMoney;//已投金额

    @ApiModelProperty(value = "基础利率", example = "10")
    private String baseRatePercent;//基本利率

    @ApiModelProperty(value = "活动利率", example = "10")
    private String activityRatePercent;//活动利率

    @ApiModelProperty(value = "声明", example = "提供的资料全部真实有效并实物代存放于本公司，为保证借款人隐私安全无法全部展示，如出借人有疑义可联系本公司进行查询。")
    private String declaration;//声明

    @ApiModelProperty(value = "发布日期", example = "2016-11-24 12:00:00")
    private String verifyTime;//发布日期

    @ApiModelProperty(value = "募集截至日期", example = "2016-11-24 12:00:00")
    private Date fundRaisingEndTime;//募集截至日期

    @ApiModelProperty(value = "剩余时间", example = "1天12时25分")
    private String remainTime;//剩余时间

    @ApiModelProperty(value = "起投时间", example = "2016-11-24 12:00:00")
    private String investBeginTime;//起投时间

    @ApiModelProperty(value = "剩余秒数", example = "10")
    private String investBeginSeconds;//剩余秒数

    @ApiModelProperty(value = "起投金额", example = "50")
    private String minInvestMoney;//起投金额

    @ApiModelProperty(value = "递增金额", example = "50")
    private String cardinalNumber;//递增金额

    @ApiModelProperty(value = "投资上限", example = "100")
    private String maxInvestMoney;//投资上限

    @ApiModelProperty(value = "已投人数", example = "100")
    private Long investedCount;//已投人数

    @ApiModelProperty(value = "募集完成时间", example = "2016-11-24 17:00:00")
    private String raiseCompletedTime;//募集完成时间

    @ApiModelProperty(value = "手续费比例", example = "10")
    private String investFeeRate;//手续费比例

    @ApiModelProperty(value = "跑马灯内容", example = "第一个投资者将获得“拓荒先锋”称号及0.2％加息券＋50元红包")
    private String marqueeTitle;

    @ApiModelProperty(value = "分享标题", example = "拓天速贷引领投资热，开启互金新概念")
    private String title;

    @ApiModelProperty(value = "分享内容", example = "个人经营借款投资项目，总额{0}元期限{1}{2}，年化利率{3}%，先到先抢！！")
    private String content;

    @ApiModelProperty(value = "借款天数", example = "360")
    private Integer duration;

    @ApiModelProperty(value = "从当前时间到借款截止时间天数", example = "360")
    private Integer availableDuration;

    @ApiModelProperty(value = "标的类型", example = "_30,_90,_180,_360")
    private String productNewType;

    @ApiModelProperty(value = "期数", example = "12")
    private String deadline;

    @ApiModelProperty(value = "起投金额", example = "50")
    public String minInvestMoneyCent;

    @ApiModelProperty(value = "递增金额", example = "50")
    public String cardinalNumberCent;

    @ApiModelProperty(value = "投资上限", example = "100")
    public String maxInvestMoneyCent;

    @ApiModelProperty(value = "已投金额", example = "100")
    public String investedMoneyCent;

    @ApiModelProperty(value = "借款总额", example = "100")
    public String loanMoneyCent;

    @ApiModelProperty(value = "阶梯加息", example = "list")
    private List<ExtraLoanRateDto> extraRates;

    @ApiModelProperty(value = "申请材料", example = "list")
    private List<EvidenceResponseDataDto> evidence;

    @ApiModelProperty(value = "投资来源", example = "WEB，MOBILE")
    private String extraSource;

    @ApiModelProperty(value = "标的类型", example = "NORMAL")
    private ActivityType activityType;

    @ApiModelProperty(value = "标的类型描述", example = "普通投资")
    private String activityDesc;

    @ApiModelProperty(value = "抵押类型", example = "HOUSE:房标,车标:VEHICLE,无抵押物:NONE")
    private PledgeType pledgeType;

    @ApiModelProperty(value = "信息披露", example = "model")
    private List<DisclosureDto> disclosures;

    @ApiModelProperty(value = "是否可以转让", example = "true")
    private boolean nonTransferable;

    @ApiModelProperty(value = "万元收益", example = "1000")
    private String interestPerTenThousands;

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

    public Integer getAvailableDuration() {
        return availableDuration;
    }

    public void setAvailableDuration(Integer availableDuration) {
        this.availableDuration = availableDuration;
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

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public List<DisclosureDto> getDisclosures() {
        return disclosures;
    }

    public void setDisclosures(List<DisclosureDto> disclosures) {
        this.disclosures = disclosures;
    }

    public String getInterestPerTenThousands() {
        return interestPerTenThousands;
    }

    public void setInterestPerTenThousands(String interestPerTenThousands) {
        this.interestPerTenThousands = interestPerTenThousands;
    }

    public boolean isNonTransferable() {
        return nonTransferable;
    }

    public void setNonTransferable(boolean nonTransferable) {
        this.nonTransferable = nonTransferable;
    }
}
