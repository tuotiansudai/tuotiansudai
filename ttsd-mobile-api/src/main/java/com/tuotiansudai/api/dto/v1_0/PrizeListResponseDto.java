package com.tuotiansudai.api.dto.v1_0;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PrizeListResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "奖品列表集合", example = "list")
    private List<PrizeResponseDto> prizeList;

    public List<PrizeResponseDto> getPrizeList() {
        return prizeList;
    }

    public void setPrizeList(List<PrizeResponseDto> prizeList) {
        this.prizeList = prizeList;
    }
}
