package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PrizeImageListResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "抽奖转盘图片列表", example = "null")
    private List<PrizeImageResponseDataDto> prizeImageList;

    public List<PrizeImageResponseDataDto> getPrizeImageList() {
        return prizeImageList;
    }

    public void setPrizeImageList(List<PrizeImageResponseDataDto> prizeImageList) {
        this.prizeImageList = prizeImageList;
    }
}
