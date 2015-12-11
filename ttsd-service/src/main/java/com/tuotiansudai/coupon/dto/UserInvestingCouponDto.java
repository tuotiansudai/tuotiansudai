package com.tuotiansudai.coupon.dto;

public class UserInvestingCouponDto {
    private final long id;
    private final String name;
    private final long amount;
    private long loanId;
    private long interest;

    public UserInvestingCouponDto(UserCouponDto userCouponDto){
        this.id = userCouponDto.getId();
        this.name = userCouponDto.getName();
        this.amount = userCouponDto.getAmount();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public long getInterest() {
        return interest;
    }

    public void setInterest(long interest) {
        this.interest = interest;
    }
}
