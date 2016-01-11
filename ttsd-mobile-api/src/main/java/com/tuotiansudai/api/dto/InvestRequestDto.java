package com.tuotiansudai.api.dto;


import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.model.Source;

public class InvestRequestDto extends BaseParamDto {
    private String userId;
    private String investMoney;
    private String loanId;
    @Deprecated
    private String password;

    private String userCouponId;

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

    public String getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(String userCouponId) {
        this.userCouponId = userCouponId;
    }

    public InvestDto convertToInvestDto(){
        InvestDto investDto = new InvestDto();
        investDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        investDto.setAmount(this.getInvestMoney());
        investDto.setLoginName(this.getBaseParam().getUserId());
        investDto.setLoanId(this.getLoanId());
        investDto.setUserCouponId(this.userCouponId);
        return investDto;

    }


}
