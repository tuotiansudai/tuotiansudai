package com.tuotiansudai.api.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class FeedbackRequestDto extends BaseParamDto {

    @NotEmpty(message = "0080")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
