package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class OperationDataResponseDataDto extends BaseResponseDataDto {
    private String currentDay;
    private String operationDays;
    private String totalTradeAmount;
    private String totalInterest;
    private String totalTradeCount;
    private List<OperationDataInvestByProductTypeResponseDataDto> investListByProdyctType;
    private List<OperationDataLatestSixMonthResponseDataDto> latestSixMonthDetail;
    private String totalInvestUserCount;
    private String maleScale;
    private String femaleScale;
    private List<OperationDataAgeResponseDataDto> ageDistribution;
    private List<OperationDataInvestCityResponseDataDto> investCityScaleTop3;
    private List<OperationDataInvestAmountResponseDataDto> investAmountScaleTop3;

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

    public List<OperationDataInvestByProductTypeResponseDataDto> getInvestListByProdyctType() {
        return investListByProdyctType;
    }

    public void setInvestListByProdyctType(List<OperationDataInvestByProductTypeResponseDataDto> investListByProdyctType) {
        this.investListByProdyctType = investListByProdyctType;
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

    public List<OperationDataInvestCityResponseDataDto> getInvestCityScaleTop3() {
        return investCityScaleTop3;
    }

    public void setInvestCityScaleTop3(List<OperationDataInvestCityResponseDataDto> investCityScaleTop3) {
        this.investCityScaleTop3 = investCityScaleTop3;
    }

    public List<OperationDataInvestAmountResponseDataDto> getInvestAmountScaleTop3() {
        return investAmountScaleTop3;
    }

    public void setInvestAmountScaleTop3(List<OperationDataInvestAmountResponseDataDto> investAmountScaleTop3) {
        this.investAmountScaleTop3 = investAmountScaleTop3;
    }
}
