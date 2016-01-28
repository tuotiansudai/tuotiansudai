package com.tuotiansudai.jpush.dto;

import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.JumpTo;
import com.tuotiansudai.jpush.repository.model.PushSource;
import com.tuotiansudai.jpush.repository.model.PushType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JPushAlertDto {

    private String id;
    @NotEmpty
    private String name;
    @NotNull
    private PushType pushType;

    private List<String> pushObjects;
    @NotNull
    private PushSource pushSource;

    private String content;

    private JumpTo jumpTo;

    private String jumpToLink;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }

    public List<String> getPushObjects() {
        return pushObjects;
    }

    public void setPushObjects(List<String> pushObjects) {
        this.pushObjects = pushObjects;
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

    public JumpTo getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(JumpTo jumpTo) {
        this.jumpTo = jumpTo;
    }

    public String getJumpToLink() {
        return jumpToLink;
    }

    public void setJumpToLink(String jumpToLink) {
        this.jumpToLink = jumpToLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JPushAlertDto(){

    }
    public JPushAlertDto(JPushAlertModel jPushAlertModel){
        this.id = "" + jPushAlertModel.getId();
        this.name = jPushAlertModel.getName();
        this.pushType = jPushAlertModel.getPushType();
        this.pushObjects = jPushAlertModel.getPushObjects();
        this.pushSource = jPushAlertModel.getPushSource();
        this.content = jPushAlertModel.getContent();
        this.jumpTo = jPushAlertModel.getJumpTo();
        this.jumpToLink = jPushAlertModel.getJumpToLink();
    }
}
