package com.tuotiansudai.api.dto.v1_0;


import java.util.List;

public class InvestExperienceResponseDto extends BaseResponseDataDto{

    private List<InvestExperienceResponseDataDto> coupons;

    public List<InvestExperienceResponseDataDto> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<InvestExperienceResponseDataDto> coupons) {
        this.coupons = coupons;
    }

    public InvestExperienceResponseDto() {

    }

    public InvestExperienceResponseDto(List<InvestExperienceResponseDataDto> coupons) {
        this.coupons = coupons;
    }

}
