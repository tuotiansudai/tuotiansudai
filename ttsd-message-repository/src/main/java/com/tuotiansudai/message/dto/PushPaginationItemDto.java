package com.tuotiansudai.message.dto;

import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.message.repository.model.PushAlertModel;

public class PushPaginationItemDto {

    private PushSource pushSource;

    public PushPaginationItemDto(PushAlertModel pushAlertModel) {
        this.pushSource = pushAlertModel.getPushSource();
    }

    public PushSource getPushSource() {
        return pushSource;
    }

    public void setPushSource(PushSource pushSource) {
        this.pushSource = pushSource;
    }
}
