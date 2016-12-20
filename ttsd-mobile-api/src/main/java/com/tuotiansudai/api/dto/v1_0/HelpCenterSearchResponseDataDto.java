package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.HelpCenterModel;
import io.swagger.annotations.ApiModelProperty;

public class HelpCenterSearchResponseDataDto {

    @ApiModelProperty(value = "标题", dataType = "拓天速贷的优势是什么？")
    private String title;

    @ApiModelProperty(value = "内容", dataType = "拓天速贷")
    private String content;

    public HelpCenterSearchResponseDataDto(HelpCenterModel helpCenterModel){
        this.title = helpCenterModel.getTitle();
        this.content = helpCenterModel.getContent();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
