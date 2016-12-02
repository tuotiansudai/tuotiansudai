package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class HelpCenterCategoryResponseDataDto extends BaseResponseDataDto{

    @ApiModelProperty(value = "问题标题", dataType = "安全保障")
    private String title;

    @ApiModelProperty(value = "问题分类", dataType = "SECURITY")
    private String categoryType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
