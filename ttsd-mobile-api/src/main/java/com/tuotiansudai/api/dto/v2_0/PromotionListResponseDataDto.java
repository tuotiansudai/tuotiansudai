package com.tuotiansudai.api.dto.v2_0;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDataDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PromotionListResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "弹框数量", example = "10")
    private Integer totalCount;

    @ApiModelProperty(value = "记录", example = "list")
    private List<PromotionRecordResponseDataDto> popList;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<PromotionRecordResponseDataDto> getPopList() {
        return popList;
    }

    public void setPopList(List<PromotionRecordResponseDataDto> popList) {
        this.popList = popList;
    }
}
