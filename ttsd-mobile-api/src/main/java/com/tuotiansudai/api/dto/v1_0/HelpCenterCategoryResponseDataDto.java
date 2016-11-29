package com.tuotiansudai.api.dto.v1_0;

public class HelpCenterCategoryResponseDataDto extends BaseResponseDataDto{

    private String title;

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
