package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class HelpCenterSearchListResponseDataDto extends BaseResponseDataDto {

    private List<HelpCenterSearchResponseDataDto> searchList;

    public List<HelpCenterSearchResponseDataDto> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<HelpCenterSearchResponseDataDto> searchList) {
        this.searchList = searchList;
    }
}
