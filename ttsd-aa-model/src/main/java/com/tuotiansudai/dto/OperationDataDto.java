package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OperationDataDto implements Serializable {
    private int operationDays;   //运营天数
    private long usersCount;     //注册用户数
    private long investUsersCount;  //投资用户数
    private String TradeAmount;     //累计交易金额
    private String totalInterest;   //累计为用户赚取
    private String maleScale;
    private String femaleScale;
    private String loanerMaleScale;
    private String loanerFemaleScale;
    private List<String> month = new ArrayList<>();
    private List<String> money = new ArrayList<>();
    private List<OperationDataAgeDataDto> ageDistribution;
    private List<OperationDataLoanerAgeDataDto> loanerAgeDistribution;
    private List<OperationDataInvestCityDataDto> investCityScaleTop5;
    private List<OperationDataLoanerCityDataDto> loanerCityScaleTop5;
    private List<OperationDataInvestAmountDataDto> investAmountScaleTop3;
    private Date now;
    private String sumLoanAmount;   //累计借贷金额
    private String sumLoanCount;    //累计借贷笔数
    private String sumLoanerCount;   //借款人数
    private String sumOverDueAmount;   //逾期金额
    private String loanOverDueRate;    //项目逾期率
    private String amountOverDueRate;   //金额逾期率
    private String loanerOverDueCount;  //借款人平台逾期次数
    private String loanerOverDueAmount; //平台逾期总金额

    private String sumExpectedAmount;  //待偿金额
    private String sumExpectedInterestAmount;  //待偿利息总额
    private String sumRepayIngInvestCount;  //待偿投资 (笔数)
    private String avgInvestAmount;  //平均出借金额
    private String maxSingleInvestAmountRate;  //最大单户出借余额占比
    private String maxTenInvestAmountRate;  //最大十户出借余额占比

    private String sumNotCompleteLoanerCount;  //当前借款人数
    private String avgLoanAmount;  //平均借款金额
    private String maxSingleLoanAmountRate; //最大单户借款余额占比
    private String maxTenLoanAmountRate;    //最大单户借款余额占比

    private String loanOverDueLess90Rate;  //项目逾期率 90天以内
    private String loanOverDue90To180Rate;  //项目逾期率 90-180
    private String loanOverDueGreater180Rate;  //项目逾期率  180以上
    private String amountOverDueLess90Rate;  //金额逾期率 90天以内
    private String amountOverDue90To180Rate;  //金额逾期率 90-180
    private String amountOverDueGreater180Rate; //金额逾期率 180以上

    public int getOperationDays() {
        return operationDays;
    }

    public void setOperationDays(int operationDays) {
        this.operationDays = operationDays;
    }

    public long getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    public String getTradeAmount() {
        return TradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        TradeAmount = tradeAmount;
    }

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public List<String> getMoney() {
        return money;
    }

    public void setMoney(List<String> money) {
        this.money = money;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
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

    public List<OperationDataAgeDataDto> getAgeDistribution() {
        return ageDistribution;
    }

    public void setAgeDistribution(List<OperationDataAgeDataDto> ageDistribution) {
        this.ageDistribution = ageDistribution;
    }

    public List<OperationDataInvestCityDataDto> getInvestCityScaleTop5() {
        return investCityScaleTop5;
    }

    public void setInvestCityScaleTop5(List<OperationDataInvestCityDataDto> investCityScaleTop5) {
        this.investCityScaleTop5 = investCityScaleTop5;
    }

    public List<OperationDataInvestAmountDataDto> getInvestAmountScaleTop3() {
        return investAmountScaleTop3;
    }

    public void setInvestAmountScaleTop3(List<OperationDataInvestAmountDataDto> investAmountScaleTop3) {
        this.investAmountScaleTop3 = investAmountScaleTop3;
    }

    public List<OperationDataLoanerCityDataDto> getLoanerCityScaleTop5() {
        return loanerCityScaleTop5;
    }

    public void setLoanerCityScaleTop5(List<OperationDataLoanerCityDataDto> loanerCityScaleTop5) {
        this.loanerCityScaleTop5 = loanerCityScaleTop5;
    }

    public String getLoanerMaleScale() {
        return loanerMaleScale;
    }

    public void setLoanerMaleScale(String loanerMaleScale) {
        this.loanerMaleScale = loanerMaleScale;
    }

    public String getLoanerFemaleScale() {
        return loanerFemaleScale;
    }

    public void setLoanerFemaleScale(String loanerFemaleScale) {
        this.loanerFemaleScale = loanerFemaleScale;
    }

    public List<OperationDataLoanerAgeDataDto> getLoanerAgeDistribution() {
        return loanerAgeDistribution;
    }

    public void setLoanerAgeDistribution(List<OperationDataLoanerAgeDataDto> loanerAgeDistribution) {
        this.loanerAgeDistribution = loanerAgeDistribution;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public long getInvestUsersCount() {
        return investUsersCount;
    }

    public void setInvestUsersCount(long investUsersCount) {
        this.investUsersCount = investUsersCount;
    }

    public String getSumLoanAmount() {
        return sumLoanAmount;
    }

    public void setSumLoanAmount(String sumLoanAmount) {
        this.sumLoanAmount = sumLoanAmount;
    }

    public String getSumLoanCount() {
        return sumLoanCount;
    }

    public void setSumLoanCount(String sumLoanCount) {
        this.sumLoanCount = sumLoanCount;
    }

    public String getSumLoanerCount() {
        return sumLoanerCount;
    }

    public void setSumLoanerCount(String sumLoanerCount) {
        this.sumLoanerCount = sumLoanerCount;
    }

    public String getSumOverDueAmount() {
        return sumOverDueAmount;
    }

    public void setSumOverDueAmount(String sumOverDueAmount) {
        this.sumOverDueAmount = sumOverDueAmount;
    }

    public String getAmountOverDueRate() {
        return amountOverDueRate;
    }

    public void setAmountOverDueRate(String amountOverDueRate) {
        this.amountOverDueRate = amountOverDueRate;
    }

    public String getLoanerOverDueCount() {
        return loanerOverDueCount;
    }

    public void setLoanerOverDueCount(String loanerOverDueCount) {
        this.loanerOverDueCount = loanerOverDueCount;
    }

    public String getLoanerOverDueAmount() {
        return loanerOverDueAmount;
    }

    public void setLoanerOverDueAmount(String loanerOverDueAmount) {
        this.loanerOverDueAmount = loanerOverDueAmount;
    }

    public String getSumExpectedAmount() {
        return sumExpectedAmount;
    }

    public void setSumExpectedAmount(String sumExpectedAmount) {
        this.sumExpectedAmount = sumExpectedAmount;
    }

    public String getLoanOverDueRate() {
        return loanOverDueRate;
    }

    public void setLoanOverDueRate(String loanOverDueRate) {
        this.loanOverDueRate = loanOverDueRate;
    }

    public String getSumExpectedInterestAmount() {
        return sumExpectedInterestAmount;
    }

    public void setSumExpectedInterestAmount(String sumExpectedInterestAmount) {
        this.sumExpectedInterestAmount = sumExpectedInterestAmount;
    }

    public String getSumRepayIngInvestCount() {
        return sumRepayIngInvestCount;
    }

    public void setSumRepayIngInvestCount(String sumRepayIngInvestCount) {
        this.sumRepayIngInvestCount = sumRepayIngInvestCount;
    }

    public String getAvgInvestAmount() {
        return avgInvestAmount;
    }

    public void setAvgInvestAmount(String avgInvestAmount) {
        this.avgInvestAmount = avgInvestAmount;
    }

    public String getMaxSingleInvestAmountRate() {
        return maxSingleInvestAmountRate;
    }

    public void setMaxSingleInvestAmountRate(String maxSingleInvestAmountRate) {
        this.maxSingleInvestAmountRate = maxSingleInvestAmountRate;
    }

    public String getMaxTenInvestAmountRate() {
        return maxTenInvestAmountRate;
    }

    public void setMaxTenInvestAmountRate(String maxTenInvestAmountRate) {
        this.maxTenInvestAmountRate = maxTenInvestAmountRate;
    }

    public String getAvgLoanAmount() {
        return avgLoanAmount;
    }

    public void setAvgLoanAmount(String avgLoanAmount) {
        this.avgLoanAmount = avgLoanAmount;
    }

    public String getMaxSingleLoanAmountRate() {
        return maxSingleLoanAmountRate;
    }

    public void setMaxSingleLoanAmountRate(String maxSingleLoanAmountRate) {
        this.maxSingleLoanAmountRate = maxSingleLoanAmountRate;
    }

    public String getMaxTenLoanAmountRate() {
        return maxTenLoanAmountRate;
    }

    public void setMaxTenLoanAmountRate(String maxTenLoanAmountRate) {
        this.maxTenLoanAmountRate = maxTenLoanAmountRate;
    }

    public String getSumNotCompleteLoanerCount() {
        return sumNotCompleteLoanerCount;
    }

    public void setSumNotCompleteLoanerCount(String sumNotCompleteLoanerCount) {
        this.sumNotCompleteLoanerCount = sumNotCompleteLoanerCount;
    }

    public String getLoanOverDueLess90Rate() {
        return loanOverDueLess90Rate;
    }

    public void setLoanOverDueLess90Rate(String loanOverDueLess90Rate) {
        this.loanOverDueLess90Rate = loanOverDueLess90Rate;
    }

    public String getLoanOverDue90To180Rate() {
        return loanOverDue90To180Rate;
    }

    public void setLoanOverDue90To180Rate(String loanOverDue90To180Rate) {
        this.loanOverDue90To180Rate = loanOverDue90To180Rate;
    }

    public String getLoanOverDueGreater180Rate() {
        return loanOverDueGreater180Rate;
    }

    public void setLoanOverDueGreater180Rate(String loanOverDueGreater180Rate) {
        this.loanOverDueGreater180Rate = loanOverDueGreater180Rate;
    }

    public String getAmountOverDueLess90Rate() {
        return amountOverDueLess90Rate;
    }

    public void setAmountOverDueLess90Rate(String amountOverDueLess90Rate) {
        this.amountOverDueLess90Rate = amountOverDueLess90Rate;
    }

    public String getAmountOverDue90To180Rate() {
        return amountOverDue90To180Rate;
    }

    public void setAmountOverDue90To180Rate(String amountOverDue90To180Rate) {
        this.amountOverDue90To180Rate = amountOverDue90To180Rate;
    }

    public String getAmountOverDueGreater180Rate() {
        return amountOverDueGreater180Rate;
    }

    public void setAmountOverDueGreater180Rate(String amountOverDueGreater180Rate) {
        this.amountOverDueGreater180Rate = amountOverDueGreater180Rate;
    }
}
