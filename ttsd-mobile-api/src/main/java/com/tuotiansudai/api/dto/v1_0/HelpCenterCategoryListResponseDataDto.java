package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class HelpCenterCategoryListResponseDataDto extends BaseResponseDataDto{

    private List<HelpCenterCategoryResponseDataDto> categoryList;

    public List<HelpCenterCategoryResponseDataDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<HelpCenterCategoryResponseDataDto> categoryList) {
        this.categoryList = categoryList;
    }
}
