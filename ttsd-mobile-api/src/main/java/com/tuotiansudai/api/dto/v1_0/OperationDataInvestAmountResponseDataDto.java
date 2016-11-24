package com.tuotiansudai.api.dto.v1_0;

public class OperationDataInvestAmountResponseDataDto extends BaseResponseDataDto {
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
