package com.tuotiansudai.dto;

import java.io.Serializable;

public class AnxinSwitchSkipAuthDto implements Serializable {

    private String loginName;
    private boolean isOpen;

    public AnxinSwitchSkipAuthDto() {
    }

    public AnxinSwitchSkipAuthDto(String loginName, boolean isOpen) {
        this.loginName = loginName;
        this.isOpen = isOpen;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
