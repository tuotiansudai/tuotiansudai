package com.tuotiansudai.push.dto;

import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.push.repository.model.PushAlertModel;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PushCreateDto {

    private Long id;

    @NotNull
    private PushType pushType;

    @NotNull
    private PushSource pushSource;

    @NotNull
    private String content;

    public PushCreateDto() {
    }

    public PushCreateDto(PushAlertModel pushAlertModel) {
        this.id = pushAlertModel.getId();
        this.pushType = pushAlertModel.getPushType();
        this.pushSource = pushAlertModel.getPushSource();
        this.content = pushAlertModel.getContent();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
