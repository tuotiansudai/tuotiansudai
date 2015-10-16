package com.ttsd.special.dto;

public enum InvestTopStatPeriod {
    Week,
    Month,
    Quarter;

    public static InvestTopStatPeriod fromValue(String period) {
        if (Week.toString().equalsIgnoreCase(period)) {
            return Week;
        } else if (Month.toString().equalsIgnoreCase(period)) {
            return Month;
        } else if (Quarter.toString().equalsIgnoreCase(period)) {
            return Quarter;
        }
        throw new RuntimeException("InvestTopStatPeriod 格式不正确");
    }
}
