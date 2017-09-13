package com.tuotiansudai.dto.sms;

import java.io.Serializable;
import java.util.List;

public class NeteaseCallbackRequestDto implements Serializable {

    private String eventType;

    private List<NeteaseCallbackRequestItemDto> objects;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<NeteaseCallbackRequestItemDto> getObjects() {
        return objects;
    }

    public void setObjects(List<NeteaseCallbackRequestItemDto> objects) {
        this.objects = objects;
    }
}
