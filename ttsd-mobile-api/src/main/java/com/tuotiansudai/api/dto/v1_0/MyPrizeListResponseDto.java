package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class MyPrizeListResponseDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "我的奖品列表集合", example = "list")
    private List<PrizeResponseDto> myPrizeList;

    public List<PrizeResponseDto> getMyPrizeList() {
        return myPrizeList;
    }

    public void setMyPrizeList(List<PrizeResponseDto> myPrizeList) {
        this.myPrizeList = myPrizeList;
    }
}
