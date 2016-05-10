package com.tuotiansudai.repository.model;

public enum ProductType {

    _30("30天", "SYL", 1, 30),
    _90("90天", "WYX", 3, 90),
    _180("180天", "JYF", 6, 180),
    _360("360天", "JYF", 12, 360);

    private String name;

    private String productLine;

    private int periods;

    private int duration;

    public String getName() {
        return name;
    }

    public String getProductLine() {
        return productLine;
    }

    public int getPeriods() {
        return periods;
    }

    public int getDuration() {
        return duration;
    }

    ProductType(String name, String productLine, int periods, int duration) {
        this.name = name;
        this.productLine = productLine;
        this.periods = periods;
        this.duration = duration;
    }
}
