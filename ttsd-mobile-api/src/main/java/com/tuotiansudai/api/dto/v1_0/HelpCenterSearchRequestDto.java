package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class HelpCenterSearchRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "热门问题", dataType = "true")
    private String hot;

    @ApiModelProperty(value = "问题种类", dataType = "SECURITY")
    private String category;

    @ApiModelProperty(value = "搜索关键字", dataType = "拓天速贷")
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
