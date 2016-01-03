package com.tuotiansudai.coupon.repository.model;


import com.google.common.base.Joiner;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

public class CouponModel implements Serializable {

    private long id;

    private String name;

    private long amount;

    private Date startTime;

    private Date endTime;

    private long usedCount;

    private long totalCount;

    private boolean active = false;

    private Date createdTime;

    private String createdBy;

    private String activatedBy;

    private Date activatedTime;

    private long issuedCount;

    private long expectedAmount;

    private long actualAmount;

    private long investQuota;

    private String productType;

    private String couponType;

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

    public CouponModel(){

    }

    public long getInvestQuota() {
        return investQuota;
    }

    public void setInvestQuota(long investQuota) {
        this.investQuota = investQuota;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public CouponModel(CouponDto couponDto){
        this.name = couponDto.getCouponType().getDesc();
        this.amount = AmountConverter.convertStringToCent(couponDto.getAmount());
        this.startTime = couponDto.getStartTime();
        this.endTime = couponDto.getEndTime();
        this.totalCount = StringUtils.isEmpty(couponDto.getTotalCount())?0l:Long.parseLong(couponDto.getTotalCount());
        this.createdTime = new Date();
        this.productType = Joiner.on(",").join(couponDto.getProductType()) ;
        this.couponType = couponDto.getCouponType().name();
        this.investQuota = AmountConverter.convertStringToCent(couponDto.getInvestQuota());
    }
}
