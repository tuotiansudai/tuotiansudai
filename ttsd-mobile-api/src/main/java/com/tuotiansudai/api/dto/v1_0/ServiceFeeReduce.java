package com.tuotiansudai.api.dto.v1_0;

import java.util.Arrays;

public enum ServiceFeeReduce {
    SEVENTY_PERCENT(0.07, "服务费七折"),
    EIGHTY_PERCENT(0.08, "服务费八折"),
    NINETY_PERCENT(0.09, "服务费九折"),
    DEFAULT_PERCENT(0.10, ""),
    ZERO_PERCENT(0.0, "");

    private double rate;

    private String description;

    ServiceFeeReduce(double rate, String description) {
        this.rate = rate;
        this.description = description;
    }

    public double getRate() {
        return rate;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByRate(double rate) {
        return Arrays.stream(ServiceFeeReduce.values())
                .filter(serviceFeeReduce -> serviceFeeReduce.getRate() == rate)
                .findFirst().get().getDescription();
    }
}
