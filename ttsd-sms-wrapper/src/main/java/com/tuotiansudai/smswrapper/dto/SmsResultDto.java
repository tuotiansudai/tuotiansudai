package com.tuotiansudai.smswrapper.dto;

public class SmsResultDto {

    private boolean success = true;

    private SmsResultDataDto data;

    public SmsResultDto(SmsResultDataDto data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public SmsResultDataDto getData() {
        return data;
    }

}
