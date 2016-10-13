package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class MembershipPerceptionRequestDto extends BaseParamDto {

    private String loanId;
    private String investAmount;
    private List<Long> userCouponIds;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public List<Long> getUserCouponIds() {
        return userCouponIds;
    }

    public void setUserCouponIds(List<Long> userCouponIds) {
        this.userCouponIds = userCouponIds;
    }
}
