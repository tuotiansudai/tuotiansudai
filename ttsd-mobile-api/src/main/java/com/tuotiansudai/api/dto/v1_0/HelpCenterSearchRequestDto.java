package com.tuotiansudai.api.dto.v1_0;

public class HelpCenterSearchRequestDto extends BaseParamDto {

    private String hot;

    private String category;

    private String keywords;

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
