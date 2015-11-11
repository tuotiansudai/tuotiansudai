package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDto extends BaseDataDto {

    private boolean isLocked;

    @JsonProperty(value = "isLocked")
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
}
