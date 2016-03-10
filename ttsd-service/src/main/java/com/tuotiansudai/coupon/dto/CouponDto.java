package com.tuotiansudai.coupon.dto;


import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponDto implements Serializable {

    private Long id;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private Long totalCount;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String investLowerLimit;


    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String investUpperLimit;

    private Double rate;

    @NotNull
    private List<ProductType> productTypes;

    @NotNull
    private CouponType couponType;

    private boolean smsAlert;

    private Integer deadline;

    private UserGroup userGroup;

    private boolean active;

    private boolean shared;

    private long totalInvestAmount;

    private long usedCount;

    private long issuedCount;

    private long expectedAmount;

    private long actualAmount;

    private String file;

    private Boolean importIsRight;

    private Double birthdayBenefit;

    private boolean multiple;

    private List<String> agents;

    private List<String> channels;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(String investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponType couponType) {
        this.couponType = couponType;
    }

    public boolean isSmsAlert() {
        return smsAlert;
    }

    public void setSmsAlert(boolean smsAlert) {
        this.smsAlert = smsAlert;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public long getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(long totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    public long getIssuedCount() {
        return issuedCount;
    }

    public void setIssuedCount(long issuedCount) {
        this.issuedCount = issuedCount;
    }

    public long getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(long expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(long actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(String investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Boolean getImportIsRight() {
        return importIsRight;
    }

    public void setImportIsRight(Boolean importIsRight) {
        this.importIsRight = importIsRight;
    }

    public Double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(Double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public List<String> getAgents() {
        return agents;
    }

    public void setAgents(List<String> agents) {
        this.agents = agents;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public CouponDto(){

    }

    public CouponDto(CouponModel couponModel){
        this.id = couponModel.getId();
        this.amount = AmountConverter.convertCentToString(couponModel.getAmount());
        this.startTime = couponModel.getStartTime();
        this.endTime = couponModel.getEndTime();
        this.totalCount = couponModel.getTotalCount();
        this.investLowerLimit = AmountConverter.convertCentToString(couponModel.getInvestLowerLimit());
        this.investUpperLimit = AmountConverter.convertCentToString(couponModel.getInvestUpperLimit());
        this.productTypes = couponModel.getProductTypes();
        this.couponType = couponModel.getCouponType();
        this.userGroup = couponModel.getUserGroup();
        this.deadline = couponModel.getDeadline();
        this.smsAlert = couponModel.isSmsAlert();
        this.active = couponModel.isActive();
        this.totalInvestAmount = couponModel.getTotalInvestAmount();
        this.issuedCount = couponModel.getIssuedCount();
        this.usedCount = couponModel.getUsedCount();
        this.expectedAmount = couponModel.getExpectedAmount();
        this.actualAmount = couponModel.getActualAmount();
        this.rate = couponModel.getRate();
        if (couponModel.getCouponType() == CouponType.INTEREST_COUPON && couponModel.getUserGroup() == UserGroup.IMPORT_USER) {
            this.importIsRight = couponModel.getImportIsRight();
        }
        this.shared = couponModel.isShared();
        this.birthdayBenefit = couponModel.getBirthdayBenefit();
        this.multiple = couponModel.isMultiple();
        this.agents = couponModel.getAgents();
        this.channels = couponModel.getChannels();
    }
}
