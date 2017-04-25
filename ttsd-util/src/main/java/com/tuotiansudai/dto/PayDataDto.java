package com.tuotiansudai.dto;

import java.util.Map;

public class PayDataDto extends BaseDataDto {

    private String code;

    private Map<String, String> extraValues;

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

    public Map<String, String> getExtraValues() {
        return extraValues;
    }

    public void setExtraValues(Map<String, String> extraValues) {
        this.extraValues = extraValues;
    }
}
