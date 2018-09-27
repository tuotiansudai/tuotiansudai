package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MembershipPerceptionRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "loanId", example = "1555")
    private String loanId;

    @ApiModelProperty(value = "出借金额", example = "1000.00")
    private String investAmount;

    @ApiModelProperty(value = "优惠券Id", example = "[100,101]")
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
