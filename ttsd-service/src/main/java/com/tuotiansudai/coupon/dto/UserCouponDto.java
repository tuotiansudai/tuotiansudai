package com.tuotiansudai.coupon.dto;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserCouponDto implements Serializable {
    private long id;
    private long couponId;
    private CouponType couponType;
    private String name;
    private long amount;
    private Date startTime;
    private Date endTime;
    private Long loanId;
    private boolean used;
    private boolean expired;
    private boolean unused;
    private long investLowerLimit;
    private Date createdTime;
    private List<ProductType> productTypeList;

    public UserCouponDto() {
    }

    public UserCouponDto(CouponModel coupon, UserCouponModel userCoupon) {
        this.id = userCoupon.getId();
        this.couponType = coupon.getCouponType();
        this.name = coupon.getCouponType().getName();
        this.couponId = coupon.getId();
        this.amount = coupon.getAmount();
        this.startTime = coupon.getStartTime();
        this.endTime = coupon.getEndTime();
        this.loanId = userCoupon.getLoanId();
        this.used = InvestStatus.SUCCESS == userCoupon.getStatus();
        this.expired = !this.used && new DateTime(this.endTime).plusDays(1).withTimeAtStartOfDay().isBeforeNow();
        this.unused = !this.used && !this.expired;
        this.investLowerLimit = coupon.getInvestLowerLimit();
        this.createdTime = userCoupon.getCreatedTime();
        this.productTypeList = coupon.getProductTypes();
    }

    public long getId() {
        return id;
    }

    public long getCouponId() {
        return couponId;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Long getLoanId() {
        return loanId;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isUnused() {
        return unused;
    }

    public long getInvestLowerLimit() {
        return investLowerLimit;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public List<ProductType> getProductTypeList() {
        return productTypeList;
    }
}
