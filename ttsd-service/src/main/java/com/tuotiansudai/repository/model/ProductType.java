package com.tuotiansudai.repository.model;

public enum ProductType {

    SYL("速盈利", 1, 10.00),
    WYX("稳盈绣", 3, 12.00),
    JYF("久盈富", 6, 13.00);

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
