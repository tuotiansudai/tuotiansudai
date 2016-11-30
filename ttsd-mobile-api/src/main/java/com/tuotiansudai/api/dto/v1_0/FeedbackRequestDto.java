package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class FeedbackRequestDto extends BaseParamDto {
    @NotEmpty(message = "0080")
    @ApiModelProperty(value = "内容", example = "登录界面可以优化")
    private String content;

    @ApiModelProperty(value = "意见类型", example = "opinion(意见), complain(投诉), consult(咨询), other(其他)")
    private String type;

    @ApiModelProperty(value = "联系方式", example = "15211112341")
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
