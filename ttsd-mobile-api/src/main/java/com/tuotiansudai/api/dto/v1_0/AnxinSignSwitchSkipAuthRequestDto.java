package com.tuotiansudai.api.dto.v1_0;

public class AnxinSignSwitchSkipAuthRequestDto extends BaseParamDto {

    boolean open;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}