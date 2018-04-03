package com.tuotiansudai.dto;

import java.util.Map;

public class HuiZuDataDto extends BaseDataDto {

    private String code;

    private Map<String, String> extraValues;

    public HuiZuDataDto() {
    }

    public HuiZuDataDto(boolean status) {
        super(status);
    }

    public HuiZuDataDto(boolean status, String message) {
        super(status, message);
    }

    public HuiZuDataDto(boolean status, String message, String code) {
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
