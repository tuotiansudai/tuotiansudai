package com.tuotiansudai.api.dto.v1_0;

import org.hibernate.validator.constraints.NotEmpty;

public class FeedbackRequestDto extends BaseParamDto {

    @NotEmpty(message = "0080")
    private String content;

    @NotEmpty(message = "0080")
    private String type;

    @NotEmpty(message = "0080")
    private String contact;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
