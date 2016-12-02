package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class HelpCenterCategoryListResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "问题类型", dataType = "list")
    private List<HelpCenterCategoryResponseDataDto> categoryList;

    public List<HelpCenterCategoryResponseDataDto> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<HelpCenterCategoryResponseDataDto> categoryList) {
        this.categoryList = categoryList;
    }
}
