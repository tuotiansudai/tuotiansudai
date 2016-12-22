package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.repository.model.PushModel;

public class PushPaginationItemDto {

    private PushSource pushSource;

    public PushPaginationItemDto(PushModel pushModel) {
        this.pushSource = pushModel.getPushSource();
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }
}
