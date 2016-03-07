package com.tuotiansudai.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class UserCouponResponseDataDto extends BaseCouponResponseDataDto{


    private String loanId;

    private String loanName;

    private ProductType loanProductType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date usedTime;

    private String expectedInterest;

    private String investUpperLimit;

    private String investAmount;


    public UserCouponResponseDataDto() {
        super();
    }

    public UserCouponResponseDataDto(UserCouponView userCouponView) {
        super(userCouponView);
        this.usedTime = userCouponView.getUsedTime();
        this.expectedInterest = AmountConverter.convertCentToString(userCouponView.getExpectedIncome());
        this.investUpperLimit = AmountConverter.convertCentToString(userCouponView.getInvestUpperLimit());
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
