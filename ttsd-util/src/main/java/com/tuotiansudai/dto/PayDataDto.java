package com.tuotiansudai.dto;

import java.util.Map;

public class PayDataDto extends BaseDataDto {

    private String code;

    private Map<String, String> extraValues;

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
