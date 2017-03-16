package com.tuotiansudai.dto;

public class PayDataDto extends BaseDataDto {

    private String code;

    public PayDataDto() {
    }

    public PayDataDto(boolean status) {
        super(status);
    }

    public PayDataDto(boolean status, String message) {
        super(status, message);
    }

    public PayDataDto(boolean status, String message, String code) {
        super(status, message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
