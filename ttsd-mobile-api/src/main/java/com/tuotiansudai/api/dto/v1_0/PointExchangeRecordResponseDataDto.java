package com.tuotiansudai.api.dto.v1_0;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DecimalFormat;
import java.util.List;

public class PointExchangeRecordResponseDataDto {

    private long point;
    private String couponId;
    private CouponType couponType;
    private String name;
    private String amount;
    private String rate;
    private String investLowerLimit;
    private int deadline;
    private List<String> productTypes;

    public String getCouponId() {
        return couponId;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getRate() {
        return rate;
    }

    public String getInvestLowerLimit() {
        return investLowerLimit;
    }

    public List<String> getProductTypes() {
        return productTypes;
    }

    public long getPoint() {
        return point;
    }

    public int getDeadline() {
        return deadline;
    }

    public PointExchangeRecordResponseDataDto(CouponModel couponModel, long point) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.couponId = String.valueOf(couponModel.getId());
        this.couponType = couponModel.getCouponType();
        this.point = point;
        this.name = couponModel.getCouponType().getName();
        this.amount = AmountConverter.convertCentToString(couponModel.getAmount());
        this.rate = String.valueOf(decimalFormat.format(couponModel.getRate() * 100));
        this.investLowerLimit = AmountConverter.convertCentToString(couponModel.getInvestLowerLimit());
        this.deadline = Days.daysBetween(new DateTime().withTimeAtStartOfDay(), new DateTime(couponModel.getEndTime()).withTimeAtStartOfDay()).getDays() + 1;
        this.productTypes = Lists.transform(couponModel.getProductTypes(), new Function<ProductType, String>() {
            @Override
            public String apply(ProductType input) {
                return input.getProductLine();
            }
        });
    }

}
