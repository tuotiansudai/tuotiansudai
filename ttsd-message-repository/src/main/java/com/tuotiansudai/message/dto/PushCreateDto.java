package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.repository.model.PushModel;

import javax.validation.constraints.NotNull;

public class PushCreateDto {

    private Long id;

    @NotNull
    private PushType pushType;

    @NotNull
    private PushSource pushSource;

    @NotNull
    private String content;

    @NotNull
    private AppUrl jumpTo;

    public PushCreateDto() {
    }

    public PushCreateDto(PushModel pushModel) {
        this.id = pushModel.getId();
        this.pushType = pushModel.getPushType();
        this.pushSource = pushModel.getPushSource();
        this.content = pushModel.getContent();
        this.jumpTo = pushModel.getJumpTo();
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

    public AppUrl getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(AppUrl jumpTo) {
        this.jumpTo = jumpTo;
    }
}
