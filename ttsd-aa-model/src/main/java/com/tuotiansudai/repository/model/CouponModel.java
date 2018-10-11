package com.tuotiansudai.repository.model;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.CouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CouponModel implements Serializable {
    private long id;
    private long amount;
    private double rate;
    private double birthdayBenefit;
    private Integer period;
    private boolean multiple;
    private Date startTime;
    private Date endTime;
    private Integer deadline;
    private Date failureTime;
    private long usedCount;
    private Long totalCount;
    private boolean active;
    private boolean shared;
    private Date createdTime;
    private String createdBy;
    private String activatedBy;
    private Date activatedTime;
    private String updatedBy;
    private Date updatedTime;
    private long issuedCount;
    private long expectedAmount;
    private long actualAmount;
    private long investLowerLimit;
    private List<ProductType> productTypes;
    private CouponType couponType;
    private UserGroup userGroup;
    private long totalInvestAmount;
    private boolean deleted;
    private Boolean importIsRight;
    private List<String> agents;
    private List<String> channels;
    private String couponSource;
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getBirthdayBenefit() {
        return birthdayBenefit;
    }

    public void setBirthdayBenefit(double birthdayBenefit) {
        this.birthdayBenefit = birthdayBenefit;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
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

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    public Long getTotalCount() {
        return totalCount;
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

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getActivatedBy() {
        return activatedBy;
    }

    public void setActivatedBy(String activatedBy) {
        this.activatedBy = activatedBy;
    }

    public Date getActivatedTime() {
        return activatedTime;
    }

    public void setActivatedTime(Date activatedTime) {
        this.activatedTime = activatedTime;
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

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public void setInvestLowerLimit(long investLowerLimit) {
        this.investLowerLimit = investLowerLimit;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public CouponModel() {
        this.updatedTime = new Date();
    }

    public long getTotalInvestAmount() {
        return totalInvestAmount;
    }

    public void setTotalInvestAmount(long totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getImportIsRight() {
        return importIsRight;
    }

    public void setImportIsRight(Boolean importIsRight) {
        this.importIsRight = importIsRight;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
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

    public String getCouponSource() {
        return couponSource;
    }

    public void setCouponSource(String couponSource) {
        this.couponSource = couponSource;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Date getFailureTime() {
        return failureTime;
    }

    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
    }

    public CouponModel(CouponDto couponDto) {
        this.shared = couponDto.isShared();
        this.amount = AmountConverter.convertStringToCent(couponDto.getAmount());
        this.startTime = couponDto.getStartTime() != null ? new DateTime(couponDto.getStartTime()).withTimeAtStartOfDay().toDate() : null;
        this.endTime = couponDto.getEndTime() != null ? new DateTime(couponDto.getEndTime()).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate() : null;
        this.deadline = couponDto.getUseDeadline() ? couponDto.getDeadline() : 0;
        this.failureTime = couponDto.getUseDeadline() ? null : new DateTime(couponDto.getFailureTime()).withTimeAtStartOfDay().plusDays(1).minusSeconds(1).toDate();
        this.totalCount = couponDto.getTotalCount() != null ? couponDto.getTotalCount() : 0;
        this.productTypes = couponDto.getProductTypes();
        this.couponType = couponDto.getCouponType();
        this.investLowerLimit = AmountConverter.convertStringToCent(couponDto.getInvestLowerLimit());
        this.userGroup = couponDto.getUserGroup();
        this.rate = couponDto.getRate() == null ? 0 : new BigDecimal(couponDto.getRate()).divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        this.birthdayBenefit = couponDto.getBirthdayBenefit() == null ? 0 : new BigDecimal(couponDto.getBirthdayBenefit()).subtract(new BigDecimal(1)).doubleValue();
        this.agents = couponDto.getAgents();
        this.channels = couponDto.getChannels();
        this.multiple = couponDto.getCouponType() == CouponType.BIRTHDAY_COUPON || Lists.newArrayList(UserGroup.FIRST_INVEST_ACHIEVEMENT, UserGroup.MAX_AMOUNT_ACHIEVEMENT, UserGroup.LAST_INVEST_ACHIEVEMENT, UserGroup.EXCHANGER).contains(couponDto.getUserGroup());
        this.couponSource = couponDto.getCouponSource();
        this.comment = couponDto.getComment();
        this.updatedTime = new Date();

    }

    protected CouponModel(CouponModel couponModel) {
        this.id = couponModel.getId();
        this.amount = couponModel.getAmount();
        this.rate = couponModel.getRate();
        this.birthdayBenefit = couponModel.getBirthdayBenefit();
        this.multiple = couponModel.isMultiple();
        this.startTime = couponModel.getStartTime();
        this.endTime = couponModel.getEndTime();
        this.deadline = couponModel.getDeadline();
        this.usedCount = couponModel.getUsedCount();
        this.totalCount = couponModel.getTotalCount();
        this.active = couponModel.isActive();
        this.shared = couponModel.isShared();
        this.createdTime = couponModel.getCreatedTime();
        this.createdBy = couponModel.getCreatedBy();
        this.activatedBy = couponModel.getActivatedBy();
        this.activatedTime = couponModel.getActivatedTime();
        this.updatedBy = couponModel.getUpdatedBy();
        this.updatedTime = couponModel.getUpdatedTime();
        this.issuedCount = couponModel.getIssuedCount();
        this.expectedAmount = couponModel.getExpectedAmount();
        this.actualAmount = couponModel.getActualAmount();
        this.investLowerLimit = couponModel.getInvestLowerLimit();
        this.productTypes = couponModel.getProductTypes();
        this.couponType = couponModel.getCouponType();
        this.userGroup = couponModel.getUserGroup();
        this.totalInvestAmount = couponModel.getTotalInvestAmount();
        this.deleted = couponModel.isDeleted();
        this.importIsRight = couponModel.getImportIsRight();
        this.agents = couponModel.getAgents();
        this.channels = couponModel.getChannels();
        this.couponSource = couponModel.couponSource;
        this.comment = couponModel.getComment();
    }
}
