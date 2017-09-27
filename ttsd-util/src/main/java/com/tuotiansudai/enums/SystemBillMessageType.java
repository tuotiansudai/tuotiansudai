package com.tuotiansudai.enums;

public enum SystemBillMessageType {

    TRANSFER_OUT("转出"),

    TRANSFER_IN("转入");

    private String description;

    SystemBillMessageType(String description) {
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

}
