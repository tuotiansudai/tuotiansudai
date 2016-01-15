package com.tuotiansudai.console.jpush.dto;

import com.tuotiansudai.console.jpush.repository.model.JumpTo;
import com.tuotiansudai.console.jpush.repository.model.PushSource;
import com.tuotiansudai.console.jpush.repository.model.PushStatus;
import com.tuotiansudai.console.jpush.repository.model.PushType;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class JPushAlertDto {
    @NotEmpty
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pushTime;
    @NotEmpty
    private PushType pushType;
    @NotEmpty
    private List<String> pushObjects;
    @NotEmpty
    private PushSource pushSource;

    private String content;

    private JumpTo jumpTo;

    private String linkAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
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

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }
}
