package com.tuotiansudai.coupon.repository.model;


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

    private boolean active;

    private Date createTime;

    private String createUser;

    private String activeUser;

    private Date activeTime;

    private long issuedCount;

    private long expectedAmount;

    private long actualAmount;

    private long investQuota;

    private UserGroup userGroup;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
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

    public long getInvestQuota() {
        return investQuota;
    }

    public void setInvestQuota(long investQuota) {
        this.investQuota = investQuota;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public CouponModel() {

    }

    public CouponModel(CouponDto couponDto) {
        this.name = couponDto.getName();
        this.amount = AmountConverter.convertStringToCent(couponDto.getAmount());
        this.startTime = couponDto.getStartTime();
        this.endTime = couponDto.getEndTime();
        this.totalCount = StringUtils.isEmpty(couponDto.getTotalCount()) ? 0L : Long.parseLong(couponDto.getTotalCount());
        this.createTime = new Date();
        this.investQuota = AmountConverter.convertStringToCent(couponDto.getInvestQuota());
    }
}
