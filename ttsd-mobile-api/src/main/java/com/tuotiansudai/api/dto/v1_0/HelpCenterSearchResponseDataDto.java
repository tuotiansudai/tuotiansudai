package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.HelpCenterModel;

public class HelpCenterSearchResponseDataDto {

    private String title;
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
