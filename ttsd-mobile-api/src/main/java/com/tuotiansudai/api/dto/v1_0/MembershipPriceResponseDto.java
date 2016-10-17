package com.tuotiansudai.api.dto.v1_0;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.MembershipPriceModel;

import java.util.List;

public class MembershipPriceResponseDto extends BaseResponseDataDto {

    private List<MembershipPriceModel> prices = Lists.newArrayList();

    public MembershipPriceResponseDto(List<MembershipPriceModel> prices) {
        this.prices = prices;
    }

    public List<MembershipPriceModel> getPrices() {
        return prices;
    }
}
