package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanListDto {

    private long id;

    private String name;

    private LoanType type;

    private String agentLoginName;

    private String loanerUserName;

    private long loanAmount;

    private long periods;

    private String basicRate;

    private String activityRate;

    private LoanStatus status;

    private Date createdTime;

    private ProductType productType;

    private PledgeType pledgeType;

    private List<ExtraLoanRateItemDto> extraLoanRateModels;

    private List<Source> extraSource;

    private boolean nonTransferable;

    private boolean disableCoupon;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public String getLoanerUserName() {
        return loanerUserName;
    }

    public void setLoanerUserName(String loanerUserName) {
        this.loanerUserName = loanerUserName;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public long getPeriods() {
        return periods;
    }

    public void setPeriods(long periods) {
        this.periods = periods;
    }

    public String getBasicRate() {
        return basicRate;
    }

    public void setBasicRate(String basicRate) {
        this.basicRate = basicRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public PledgeType getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(PledgeType pledgeType) {
        this.pledgeType = pledgeType;
    }

    public List<ExtraLoanRateItemDto> getExtraLoanRateModels() {
        if (extraLoanRateModels == null || extraLoanRateModels.size() == 0) {
            return new ArrayList<>();
        }
        return extraLoanRateModels;
    }

    public void setExtraLoanRateModels(List<ExtraLoanRateItemDto> extraLoanRateModels) {
        this.extraLoanRateModels = extraLoanRateModels;
    }

    public List<Source> getExtraSource() {
        return extraSource;
    }

    public void setExtraSource(List<Source> extraSource) {
        this.extraSource = extraSource;
    }

    public boolean getNonTransferable() {
        return nonTransferable;
    }

    public void setNonTransferable(boolean nonTransferable) {
        this.nonTransferable = nonTransferable;
    }

    public boolean getDisableCoupon() {
        return disableCoupon;
    }

    public void setDisableCoupon(boolean disableCoupon) {
        this.disableCoupon = disableCoupon;
    }
}
