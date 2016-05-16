package com.tuotiansudai.api.dto.v1_0;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;

import java.text.DecimalFormat;
import java.util.Date;

public class UserCouponResponseDataDto extends BaseCouponResponseDataDto{


    private String loanId;

    private String loanName;

    private ProductType loanProductType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usedTime;

    private String expectedInterest;

    private String investUpperLimit = "1000000.00";

    private String investAmount;


    public UserCouponResponseDataDto() {
    }

    public UserCouponResponseDataDto(UserCouponView userCouponView) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        this.name = userCouponView.getCouponType().getName();
        this.type = userCouponView.getCouponType();
        this.amount = AmountConverter.convertCentToString(userCouponView.getCouponAmount());
        this.investLowerLimit = AmountConverter.convertCentToString(userCouponView.getInvestLowerLimit());
        this.productTypes = userCouponView.getProductTypeList();
        this.rate = decimalFormat.format(userCouponView.getRate() * 100);
        this.birthdayRate = String.valueOf(userCouponView.getBirthdayBenefit());
        this.shared = userCouponView.isShared();
        this.startDate = userCouponView.getStartTime();
        this.endDate = userCouponView.getEndTime();
        this.investAmount = AmountConverter.convertCentToString(userCouponView.getInvestAmount());
        this.usedTime = userCouponView.getUsedTime();
        this.expectedInterest = AmountConverter.convertCentToString(userCouponView.getExpectedIncome());
        this.loanId = String.valueOf(userCouponView.getLoanId());
        this.loanName = userCouponView.getLoanName();
        this.loanProductType = userCouponView.getLoanProductType();
        this.investAmount = AmountConverter.convertCentToString(userCouponView.getInvestAmount());

    }



    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public ProductType getLoanProductType() {
        return loanProductType;
    }

    public void setLoanProductType(ProductType loanProductType) {
        this.loanProductType = loanProductType;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getInvestUpperLimit() {
        return investUpperLimit;
    }

    public void setInvestUpperLimit(String investUpperLimit) {
        this.investUpperLimit = investUpperLimit;
    }


    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

}
