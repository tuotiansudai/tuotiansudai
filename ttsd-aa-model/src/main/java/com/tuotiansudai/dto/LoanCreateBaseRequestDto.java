package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

public class LoanCreateBaseRequestDto {

    private Long id;

    @NotEmpty
    private ProductType productType;

    @NotEmpty
    private LoanType loanType;

    @NotEmpty
    private PledgeType pledgeType;

    @NotEmpty
    private ActivityType activityType;

    @NotEmpty
    private String name;

    @NotEmpty
    private String agent;

    @NotEmpty
    private String loanAmount;

    @NotEmpty
    private String baseRate;

    @NotEmpty
    private String activityRate;

    private int originalDuration;

    @NotEmpty
    private String minInvestAmount;

    @NotEmpty
    private String maxInvestAmount;

    @NotEmpty
    private String investIncreasingAmount;

    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date fundraisingStartTime;

    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date fundraisingEndTime;

    @NotEmpty
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadline;

    @NotEmpty
    private long contractId;

    @NotEmpty
    private LoanStatus status;

    private List<LoanTitleRelationModel> loanTitles = Lists.newArrayList();

    private String createdBy;

    private Date verifyTime;

    private String verifyLoginName;

    private Date recheckTime;

    private String recheckLoginName;

    private Date raisingCompleteTime;

    private Boolean isBankPlatForm;//资金平台

    @NotEmpty
    private String loanFee;

    public LoanCreateBaseRequestDto() {
    }

    public LoanCreateBaseRequestDto(LoanModel loanModel, List<LoanTitleRelationModel> loanTitles) {
        this.id = loanModel.getId();
        this.productType = loanModel.getProductType();
        this.loanType = loanModel.getType();
        this.pledgeType = loanModel.getPledgeType();
        this.activityType = loanModel.getActivityType();
        this.name = loanModel.getName();
        this.agent = loanModel.getAgentLoginName();
        this.loanAmount = AmountConverter.convertCentToString(loanModel.getLoanAmount());
        this.baseRate = String.valueOf(loanModel.getBaseRate());
        this.activityRate = String.valueOf(loanModel.getActivityRate());
        this.originalDuration = loanModel.getOriginalDuration();
        this.minInvestAmount = AmountConverter.convertCentToString(loanModel.getMinInvestAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(loanModel.getMaxInvestAmount());
        this.investIncreasingAmount = AmountConverter.convertCentToString(loanModel.getInvestIncreasingAmount());
        this.fundraisingStartTime = loanModel.getFundraisingStartTime();
        this.fundraisingEndTime = loanModel.getFundraisingEndTime();
        this.deadline = loanModel.getDeadline();
        this.contractId = loanModel.getContractId();
        this.status = loanModel.getStatus();
        this.loanTitles = loanTitles;
        this.verifyTime = loanModel.getVerifyTime();
        this.verifyLoginName = loanModel.getVerifyLoginName();
        this.recheckTime = loanModel.getRecheckTime();
        this.recheckLoginName = loanModel.getRecheckLoginName();
        this.raisingCompleteTime = loanModel.getRaisingCompleteTime();
        this.isBankPlatForm=loanModel.getIsBankPlatform();
        this.loanFee=AmountConverter.convertCentToString(loanModel.getLoanFee());;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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

    public int getOriginalDuration() {
        return originalDuration;
    }

    public void setOriginalDuration(int originalDuration) {
        this.originalDuration = originalDuration;
    }

    public String getMinInvestAmount() {
        return minInvestAmount;
    }

    public void setMinInvestAmount(String minInvestAmount) {
        this.minInvestAmount = minInvestAmount;
    }

    public String getMaxInvestAmount() {
        return maxInvestAmount;
    }

    public void setMaxInvestAmount(String maxInvestAmount) {
        this.maxInvestAmount = maxInvestAmount;
    }

    public String getInvestIncreasingAmount() {
        return investIncreasingAmount;
    }

    public void setInvestIncreasingAmount(String investIncreasingAmount) {
        this.investIncreasingAmount = investIncreasingAmount;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public long getContractId() {
        return contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public List<LoanTitleRelationModel> getLoanTitles() {
        return loanTitles;
    }

    public void setLoanTitles(List<LoanTitleRelationModel> loanTitles) {
        this.loanTitles = loanTitles;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyLoginName() {
        return verifyLoginName;
    }

    public void setVerifyLoginName(String verifyLoginName) {
        this.verifyLoginName = verifyLoginName;
    }

    public Date getRecheckTime() {
        return recheckTime;
    }

    public void setRecheckTime(Date recheckTime) {
        this.recheckTime = recheckTime;
    }

    public String getRecheckLoginName() {
        return recheckLoginName;
    }

    public void setRecheckLoginName(String recheckLoginName) {
        this.recheckLoginName = recheckLoginName;
    }

    public Date getRaisingCompleteTime() {
        return raisingCompleteTime;
    }

    public void setRaisingCompleteTime(Date raisingCompleteTime) {
        this.raisingCompleteTime = raisingCompleteTime;
    }

    public Boolean getIsBankPlatForm() {
        return isBankPlatForm;
    }

    public void setIsBankPlatForm(Boolean isBankPlatForm) {
        this.isBankPlatForm = isBankPlatForm;
    }

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
    }
}
