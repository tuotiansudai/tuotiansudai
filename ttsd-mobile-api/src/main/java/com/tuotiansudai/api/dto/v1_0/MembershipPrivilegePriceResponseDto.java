package com.tuotiansudai.api.dto.v1_0;

import com.google.common.collect.Lists;
import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MembershipPrivilegePriceResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "增加特权价格列表", example = "list")
    private List<MembershipPrivilegePriceResponseDataDto> prices = Lists.newArrayList();

    public MembershipPrivilegePriceResponseDto(List<MembershipPrivilegePriceResponseDataDto> prices) {
        this.prices = prices;
    }

    public List<MembershipPrivilegePriceResponseDataDto> getPrices() {
        return prices;
    }
}
