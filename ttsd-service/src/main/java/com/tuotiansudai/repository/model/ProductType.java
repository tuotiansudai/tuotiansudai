package com.tuotiansudai.repository.model;

public enum ProductType {

    _30("30天", "SYL", "速盈利", 1, 30, 0.1),
    _90("90天", "WYX", "稳盈绣", 3, 90, 0.11),
    _180("180天", "JYF", "久盈富", 6, 180, 0.12),
    _360("360天", "JYF", "久盈富", 12, 360, 0.13),
    EXPERIENCE("新手体验标", "", "", 1, 3, 0.15);

    private final String name;
    private final String productLine;
    private final String productLineName;
    private final int periods;
    private final int duration;
    private final double baseRate;

    public String getName() {
        return name;
    }

    public String getProductLine() {
        return productLine;
    }

    public String getProductLineName() {
        return productLineName;
    }

    public int getPeriods() {
        return periods;
    }

    public int getDuration() {
        return duration;
    }

    public double getBaseRate() {
        return baseRate;
    }

    ProductType(String name, String productLine, String productLineName, int periods, int duration, double baseRate) {
        this.name = name;
        this.productLine = productLine;
        this.productLineName = productLineName;
        this.periods = periods;
        this.duration = duration;
        this.baseRate = baseRate;
    }
}
