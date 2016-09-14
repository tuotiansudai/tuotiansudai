package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class AbstractCreateLoanDto implements Serializable {
    //LoanDto
    protected long id;
    protected String projectName;
    protected String agentLoginName;
    protected LoanType type;
    protected int periods;
    protected PledgeType pledgeType;
    protected String minInvestAmount;
    protected String investIncreasingAmount;
    protected String maxInvestAmount;
    protected ActivityType activityType;
    protected ProductType productType;
    protected String activityRate;
    protected String basicRate;
    protected long contractId;
    protected Date fundraisingStartTime;
    protected Date fundraisingEndTime;
    protected Date raisingCompleteTime;
    protected boolean showOnHome;
    protected String loanAmount;
    protected Date createdTime;
    protected Date verifyTime;
    protected Date recheckTime;
    protected LoanStatus loanStatus;
    protected List<LoanTitleRelationModel> loanTitles = new ArrayList<>();
    protected List<LoanTitleModel> loanTitleDto = new ArrayList<>();
    protected double amountNeedRaised;
    protected String maxAvailableInvestAmount;
    protected double balance;
    protected double raiseCompletedRate;
    protected long expectedTotalIncome;
    protected long preheatSeconds;
    protected String createdLoginName;
    protected String verifyLoginName;
    protected String recheckLoginName;
    protected int duration;
    protected List<Long> extraRateIds;

    //LoanDetailsDto
    protected String declaration;
    protected String extraSource;
    protected boolean activity;

    //LoanerDetailsDto
    protected String loanerLoginName;
    protected String loanerUserName;
    protected String loanerIdentityNumber;
    protected Gender loanerGender;
    protected int loanerAge;
    protected Marriage loanerMarriage;
    protected String loanerRegion;
    protected String loanerIncome;
    protected String loanerEmploymentStatus;

    //PledgeDetailsDto
    protected String pledgeLocation;
    protected String estimateAmount;
    protected String pledgeLoanAmount;

    public AbstractCreateLoanDto() {
    }

    public LoanDto getLoanDto() {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(id);
        loanDto.setProjectName(projectName);
        loanDto.setAgentLoginName(agentLoginName);
        loanDto.setLoanerLoginName(loanerLoginName);
        loanDto.setLoanerUserName(loanerUserName);
        loanDto.setLoanerIdentityNumber(loanerIdentityNumber);
        loanDto.setType(type);
        loanDto.setPeriods(periods);
        loanDto.setDescriptionText("");
        loanDto.setDescriptionHtml("");
        loanDto.setPledgeType(pledgeType);
        loanDto.setMinInvestAmount(minInvestAmount);
        loanDto.setInvestIncreasingAmount(investIncreasingAmount);
        loanDto.setMaxInvestAmount(maxInvestAmount);
        loanDto.setActivityType(activityType);
        loanDto.setProductType(productType);
        loanDto.setActivityRate(activityRate);
        loanDto.setBasicRate(basicRate);
        loanDto.setContractId(contractId);
        loanDto.setFundraisingStartTime(fundraisingStartTime);
        loanDto.setFundraisingEndTime(fundraisingEndTime);
        loanDto.setRaisingCompleteTime(raisingCompleteTime);
        loanDto.setShowOnHome(showOnHome);
        loanDto.setLoanAmount(loanAmount);
        loanDto.setCreatedTime(new Date());
        loanDto.setVerifyTime(verifyTime);
        loanDto.setRecheckTime(recheckTime);
        loanDto.setLoanStatus(loanStatus);
        loanDto.setLoanTitles(loanTitles);
        loanDto.setLoanTitleDto(loanTitleDto);
        loanDto.setAmountNeedRaised(amountNeedRaised);
        loanDto.setMaxAvailableInvestAmount(maxAvailableInvestAmount);
        loanDto.setBalance(balance);
        loanDto.setRaiseCompletedRate(raiseCompletedRate);
        loanDto.setExpectedTotalIncome(expectedTotalIncome);
        loanDto.setPreheatSeconds(preheatSeconds);
        loanDto.setCreatedLoginName(createdLoginName);
        loanDto.setVerifyLoginName(verifyLoginName);
        loanDto.setRecheckLoginName(recheckLoginName);
        loanDto.setDuration(duration);
        loanDto.setExtraRateIds(extraRateIds);

        return loanDto;
    }

    public LoanDetailsDto getLoanDetailsDto() {
        LoanDetailsDto loanDetailsDto = new LoanDetailsDto();
        loanDetailsDto.setLoanId(id);
        loanDetailsDto.setDeclaration(declaration);
        loanDetailsDto.setExtraSource(extraSource);
        loanDetailsDto.setActivity(activity);
        return loanDetailsDto;
    }

    public LoanerDetailsDto getLoanerDetailsDto() {
        LoanerDetailsDto loanerDetailsDto = new LoanerDetailsDto();
        loanerDetailsDto.setLoanId(id);
        loanerDetailsDto.setLoanerLoginName(loanerLoginName);
        loanerDetailsDto.setLoanerUserName(loanerUserName);
        loanerDetailsDto.setLoanerGender(loanerGender);
        loanerDetailsDto.setLoanerAge(loanerAge);
        loanerDetailsDto.setLoanerIdentityNumber(loanerIdentityNumber);
        loanerDetailsDto.setLoanerMarriage(loanerMarriage);
        loanerDetailsDto.setLoanerRegion(loanerRegion);
        loanerDetailsDto.setLoanerIncome(loanerIncome);
        loanerDetailsDto.setLoanerEmploymentStatus(loanerEmploymentStatus);

        return loanerDetailsDto;
    }

    abstract public AbstractPledgeDetailsDto getPledgeDetailsDto();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public String getLoanerIdentityNumber() {
        return loanerIdentityNumber;
    }

    public void setLoanerIdentityNumber(String loanerIdentityNumber) {
        this.loanerIdentityNumber = loanerIdentityNumber;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(String investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public String getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(String basicRate) {
        this.basicRate = basicRate;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(Date fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public Date getFundraisingEndTime() {
        return fundraisingEndTime;
    }

    public void setFundraisingEndTime(Date fundraisingEndTime) {
        this.fundraisingEndTime = fundraisingEndTime;
    }

    public Date getRaisingCompleteTime() {
        return raisingCompleteTime;
    }

    public void setRaisingCompleteTime(Date raisingCompleteTime) {
        this.raisingCompleteTime = raisingCompleteTime;
    }

    public boolean isShowOnHome() {
        return showOnHome;
    }

    public void setShowOnHome(boolean showOnHome) {
        this.showOnHome = showOnHome;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
    }

    public List<LoanTitleModel> getLoanTitleDto() {
        return loanTitleDto;
    }

    public void setLoanTitleDto(List<LoanTitleModel> loanTitleDto) {
        this.loanTitleDto = loanTitleDto;
    }

    public double getAmountNeedRaised() {
        return amountNeedRaised;
    }

    public void setAmountNeedRaised(double amountNeedRaised) {
        this.amountNeedRaised = amountNeedRaised;
    }

    public String getMaxAvailableInvestAmount() {
        return maxAvailableInvestAmount;
    }

    public void setMaxAvailableInvestAmount(String maxAvailableInvestAmount) {
        this.maxAvailableInvestAmount = maxAvailableInvestAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getRaiseCompletedRate() {
        return raiseCompletedRate;
    }

    public void setRaiseCompletedRate(double raiseCompletedRate) {
        this.raiseCompletedRate = raiseCompletedRate;
    }

    public long getExpectedTotalIncome() {
        return expectedTotalIncome;
    }

    public void setExpectedTotalIncome(long expectedTotalIncome) {
        this.expectedTotalIncome = expectedTotalIncome;
    }

    public long getPreheatSeconds() {
        return preheatSeconds;
    }

    public void setPreheatSeconds(long preheatSeconds) {
        this.preheatSeconds = preheatSeconds;
    }

    public String getCreatedLoginName() {
        return createdLoginName;
    }

    public void setCreatedLoginName(String createdLoginName) {
        this.createdLoginName = createdLoginName;
    }

    public String getVerifyLoginName() {
        return verifyLoginName;
    }

    public void setVerifyLoginName(String verifyLoginName) {
        this.verifyLoginName = verifyLoginName;
    }

    public String getRecheckLoginName() {
        return recheckLoginName;
    }

    public void setRecheckLoginName(String recheckLoginName) {
        this.recheckLoginName = recheckLoginName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public String getLoanerLoginName() {
        return loanerLoginName;
    }

    public void setLoanerLoginName(String loanerLoginName) {
        this.loanerLoginName = loanerLoginName;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public Gender getLoanerGender() {
        return loanerGender;
    }

    public void setLoanerGender(Gender loanerGender) {
        this.loanerGender = loanerGender;
    }

    public int getLoanerAge() {
        return loanerAge;
    }

    public void setLoanerAge(int loanerAge) {
        this.loanerAge = loanerAge;
    }

    public Marriage getLoanerMarriage() {
        return loanerMarriage;
    }

    public void setLoanerMarriage(Marriage loanerMarriage) {
        this.loanerMarriage = loanerMarriage;
    }

    public String getLoanerRegion() {
        return loanerRegion;
    }

    public void setLoanerRegion(String loanerRegion) {
        this.loanerRegion = loanerRegion;
    }

    public String getLoanerIncome() {
        return loanerIncome;
    }

    public void setLoanerIncome(String loanerIncome) {
        this.loanerIncome = loanerIncome;
    }

    public String getLoanerEmploymentStatus() {
        return loanerEmploymentStatus;
    }

    public void setLoanerEmploymentStatus(String loanerEmploymentStatus) {
        this.loanerEmploymentStatus = loanerEmploymentStatus;
    }

    public String getPledgeLocation() {
        return pledgeLocation;
    }

    public void setPledgeLocation(String pledgeLocation) {
        this.pledgeLocation = pledgeLocation;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getPledgeLoanAmount() {
        return pledgeLoanAmount;
    }

    public void setPledgeLoanAmount(String pledgeLoanAmount) {
        this.pledgeLoanAmount = pledgeLoanAmount;
    }

    public List<Long> getExtraRateIds() {
        return extraRateIds;
    }

    public void setExtraRateIds(List<Long> extraRateIds) {
        this.extraRateIds = extraRateIds;
    }

    public String getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(String extraSource) {
        this.extraSource = extraSource;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }
}
