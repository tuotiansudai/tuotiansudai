package com.tuotiansudai.repository.model;

public enum ProductType {

    SYL("30天", 1, 10.00),
    WYX("90天", 3, 12.00),
    JYF("180天", 6, 13.00),
    _360("360天", 12, 13.00);

    private String name;

    private int periods;

    private double rate;

    public String getName() {
        return name;
    }

    public int getPeriods() {
        return periods;
    }

    public double getRate() {
        return rate;
    }

    ProductType(String name, int periods, double rate) {
        this.name = name;
        this.periods = periods;
        this.rate = rate;
    }
}
