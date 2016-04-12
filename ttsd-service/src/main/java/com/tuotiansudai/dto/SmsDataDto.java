package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsDataDto extends BaseDataDto {

    private boolean isRestricted;

    @JsonProperty(value = "isRestricted")
    public boolean isRestricted() {
        return isRestricted;
    }

    public void setIsRestricted(boolean isRestricted) {
        this.isRestricted = isRestricted;
    }
}
