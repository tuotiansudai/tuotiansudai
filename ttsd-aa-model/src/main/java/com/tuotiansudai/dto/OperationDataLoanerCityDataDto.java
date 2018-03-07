package com.tuotiansudai.dto;


import java.io.Serializable;

public class OperationDataLoanerCityDataDto implements Serializable {

    private String city;
    private String scale;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }
}
