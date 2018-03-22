package com.tuotiansudai.dto;

import java.io.Serializable;

public class OperationDataLoanerAgeDataDto implements Serializable {

    private String name;
    private String scale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
