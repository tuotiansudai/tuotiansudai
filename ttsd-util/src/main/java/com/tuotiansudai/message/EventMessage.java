package com.tuotiansudai.message;

import com.tuotiansudai.enums.MessageEventType;

import java.io.Serializable;
import java.util.List;

public class EventMessage implements Serializable {

    private MessageEventType eventType;

    private String title;

    private List<String> loginNames;

    private String content;

    private Long businessId;

    public EventMessage() {
    }

    public EventMessage(MessageEventType eventType, List<String> loginNames, String title, String content, Long businessId) {
        this.eventType = eventType;
        this.loginNames = loginNames;
        this.title = title;
        this.content = content;
        this.businessId = businessId;
    }

    public MessageEventType getEventType() {
        return eventType;
    }

    public void setEventType(MessageEventType eventType) {
        this.eventType = eventType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLoginNames() {
        return loginNames;
    }

    public void setLoginNames(List<String> loginNames) {
        this.loginNames = loginNames;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
