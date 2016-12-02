package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class HelpCenterSearchListResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "问题", dataType = "list")
    private List<HelpCenterSearchResponseDataDto> searchList;

    public List<HelpCenterSearchResponseDataDto> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<HelpCenterSearchResponseDataDto> searchList) {
        this.searchList = searchList;
    }
}
