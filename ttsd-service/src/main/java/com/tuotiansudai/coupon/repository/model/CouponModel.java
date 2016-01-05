package com.tuotiansudai.coupon.repository.model;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponModel implements Serializable {

    private long id;

    private long amount;

    private Date startTime;

    private Date endTime;

    private long usedCount;

    private Long totalCount;

    private boolean active;

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

    private boolean smsAlert;

    private Integer deadline;

    private UserGroup userGroup;

    private long totalInvestAmount;

    private boolean deleted;

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

    public long getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(long usedCount) {
        this.usedCount = usedCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public boolean isSmsAlert() {
        return smsAlert;
    }

    public void setSmsAlert(boolean smsAlert) {
        this.smsAlert = smsAlert;
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

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public CouponModel(CouponDto couponDto){
        this.amount = AmountConverter.convertStringToCent(couponDto.getAmount());
        this.startTime = couponDto.getStartTime();
        this.endTime = couponDto.getEndTime();
        this.totalCount = couponDto.getTotalCount();
        this.productTypes = couponDto.getProductTypes() ;
        this.couponType = couponDto.getCouponType();
        this.investLowerLimit = AmountConverter.convertStringToCent(couponDto.getInvestLowerLimit());
        this.createdTime = new Date();
        this.smsAlert = couponDto.isSmsAlert();
        this.deadline = couponDto.getDeadline();
        this.userGroup = couponDto.getUserGroup();
    }
}
