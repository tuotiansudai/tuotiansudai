package com.tuotiansudai.message;

import com.tuotiansudai.enums.MessageEventType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class EventMessage implements Serializable {

    private MessageEventType eventType;

    private String title;

    private List<String> loginNames;

    private String content;

    private Long businessId;

    private Map<Long, String> investIdLoginNames;

    public EventMessage() {
    }

    public EventMessage(MessageEventType eventType, List<String> loginNames, String title, String content, Long businessId) {
        this.eventType = eventType;
        this.loginNames = loginNames;
        this.title = title;
        this.content = content;
        this.businessId = businessId;
    }

    public EventMessage(MessageEventType eventType, String title, String content, Map<Long, String> investIdLoginNames) {
        this.eventType = eventType;
        this.title = title;
        this.content = content;
        this.investIdLoginNames = investIdLoginNames;
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

    public Map<Long, String> getInvestIdLoginNames() {
        return investIdLoginNames;
    }

    public void setInvestIdLoginNames(Map<Long, String> investIdLoginNames) {
        this.investIdLoginNames = investIdLoginNames;
    }


}
