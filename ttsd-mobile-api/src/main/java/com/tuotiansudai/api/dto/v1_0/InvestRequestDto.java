package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class InvestRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "用户id", example = "test")
    private String userId;

    @ApiModelProperty(value = "投资金额", example = "5888800")
    private String investMoney;

    @ApiModelProperty(value = "标的ID", example = "1")
    private String loanId;

    @Deprecated
    @ApiModelProperty(value = "密码", example = "123abc")
    private String password;

    @ApiModelProperty(value = "优惠券集合", example = "1,2,3")
    private List<Long> userCouponIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    @Deprecated
    public String getPassword() {
        return password;
    }

    @Deprecated
    public void setPassword(String password) {
        this.password = password;
    }

    public String getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(String investMoney) {
        this.investMoney = investMoney;
    }

    public List<Long> getUserCouponIds() {
        return userCouponIds;
    }

    public void setUserCouponIds(List<Long> userCouponIds) {
        this.userCouponIds = userCouponIds;
    }

    public InvestDto convertToInvestDto(){
        InvestDto investDto = new InvestDto();
        investDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        investDto.setAmount(this.getInvestMoney());
        investDto.setLoginName(this.getBaseParam().getUserId());
        investDto.setLoanId(this.getLoanId());
        investDto.setUserCouponIds(this.userCouponIds);
        return investDto;

    }


}
