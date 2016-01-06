package com.tuotiansudai.coupon.dto;


import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CouponDto implements Serializable {

    private Long id;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    private Long totalCount;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String investLowerLimit;

    @NotNull
    private List<ProductType> productTypes;

    @NotNull
    private CouponType couponType;

    private boolean smsAlert;

    private Integer deadline;

    private UserGroup userGroup;

    private boolean active;

    private long totalInvestAmount;

    private long usedCount;

    private long issuedCount;

    private long expectedAmount;

    private long actualAmount;

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

    public CouponDto(){

    }

    public CouponDto(CouponModel couponModel){
        this.id = couponModel.getId();
        this.amount = AmountConverter.convertCentToString(couponModel.getAmount());
        this.startTime = couponModel.getStartTime();
        this.endTime = couponModel.getEndTime();
        this.totalCount = couponModel.getTotalCount();
        this.investLowerLimit = AmountConverter.convertCentToString(couponModel.getInvestLowerLimit());
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
    }
}
