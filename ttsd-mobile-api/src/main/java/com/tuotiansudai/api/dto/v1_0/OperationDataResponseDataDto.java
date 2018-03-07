package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class OperationDataResponseDataDto extends BaseResponseDataDto {
    @ApiModelProperty(value = "截止日期", example = "11月20日")
    private String currentDay;
    @ApiModelProperty(value = "安全运营天数", example = "360")
    private String operationDays;
    @ApiModelProperty(value = "累计交易金额，单位为分", example = "293837272737")
    private String totalTradeAmount;
    @ApiModelProperty(value = "累计为用户赚取金额，单位为分", example = "33434234232")
    private String totalInterest;
    @ApiModelProperty(value = "累计交易笔数", example = "1620")
    private String totalTradeCount;
    @ApiModelProperty(value = "各期限累计交易金额，单位为分", example = "30天:1333322,90天:23434343")
    private List<OperationDataInvestByProductTypeResponseDataDto> investListByProductType;
    @ApiModelProperty(value = "近半年每月累计交易金额，单位为分", example = "5月:10088343,4月:203834843,3月:343434343,2月:14343242,1月:3423423342,12月:343434343")
    private List<OperationDataLatestSixMonthResponseDataDto> latestSixMonthDetail;
    @ApiModelProperty(value = "累计注册投资用户", example = "23843")
    private String totalInvestUserCount;
    @ApiModelProperty(value = "男性比例", example = "60.5")
    private String maleScale;
    @ApiModelProperty(value = "女性比例", example = "39.5")
    private String femaleScale;
    @ApiModelProperty(value = "各用户年龄段分布", example = "20岁以下:20%,20~30岁:30")
    private List<OperationDataAgeResponseDataDto> ageDistribution;
    @ApiModelProperty(value = "投资人数top3", example = "北京:60,上海:30,济南:10")
    private List<OperationDataInvestCityResponseDataDto> investCityScaleTop5;
    @ApiModelProperty(value = "投资金额top3", example = "北京:60,上海:30,济南:10")
    private List<OperationDataInvestAmountResponseDataDto> investAmountScaleTop3;

    @ApiModelProperty(value = "借款人数top5", example = "北京:60,上海:30,济南:10")
    private List<OperationDataLoanerCityResponseDataDto> loanerCityScaleTop5;

    public OperationDataResponseDataDto() {

    }

    public String getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(String currentDay) {
        this.currentDay = currentDay;
    }

    public String getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(String operationDays) {
        this.operationDays = operationDays;
    }

    public String getTotalTradeAmount() {
        return totalTradeAmount;
    }

    public void setTotalTradeAmount(String totalTradeAmount) {
        this.totalTradeAmount = totalTradeAmount;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalTradeCount() {
        return totalTradeCount;
    }

    public void setTotalTradeCount(String totalTradeCount) {
        this.totalTradeCount = totalTradeCount;
    }

    public List<OperationDataInvestByProductTypeResponseDataDto> getInvestListByProductType() {
        return investListByProductType;
    }

    public void setInvestListByProductType(List<OperationDataInvestByProductTypeResponseDataDto> investListByProductType) {
        this.investListByProductType = investListByProductType;
    }

    public List<OperationDataLatestSixMonthResponseDataDto> getLatestSixMonthDetail() {
        return latestSixMonthDetail;
    }

    public void setLatestSixMonthDetail(List<OperationDataLatestSixMonthResponseDataDto> latestSixMonthDetail) {
        this.latestSixMonthDetail = latestSixMonthDetail;
    }

    public String getTotalInvestUserCount() {
        return totalInvestUserCount;
    }

    public void setTotalInvestUserCount(String totalInvestUserCount) {
        this.totalInvestUserCount = totalInvestUserCount;
    }

    public String getMaleScale() {
        return maleScale;
    }

    public void setMaleScale(String maleScale) {
        this.maleScale = maleScale;
    }

    public String getFemaleScale() {
        return femaleScale;
    }

    public void setFemaleScale(String femaleScale) {
        this.femaleScale = femaleScale;
    }

    public List<OperationDataAgeResponseDataDto> getAgeDistribution() {
        return ageDistribution;
    }

    public void setAgeDistribution(List<OperationDataAgeResponseDataDto> ageDistribution) {
        this.ageDistribution = ageDistribution;
    }

    public List<OperationDataInvestCityResponseDataDto> getInvestCityScaleTop5() {
        return investCityScaleTop5;
    }

    public void setInvestCityScaleTop5(List<OperationDataInvestCityResponseDataDto> investCityScaleTop5) {
        this.investCityScaleTop5 = investCityScaleTop5;
    }

    public List<OperationDataInvestAmountResponseDataDto> getInvestAmountScaleTop3() {
        return investAmountScaleTop3;
    }

    public void setInvestAmountScaleTop3(List<OperationDataInvestAmountResponseDataDto> investAmountScaleTop3) {
        this.investAmountScaleTop3 = investAmountScaleTop3;
    }

    public List<OperationDataLoanerCityResponseDataDto> getLoanerCityScaleTop5() {
        return loanerCityScaleTop5;
    }

    public void setLoanerCityScaleTop5(List<OperationDataLoanerCityResponseDataDto> loanerCityScaleTop5) {
        this.loanerCityScaleTop5 = loanerCityScaleTop5;
    }
}
