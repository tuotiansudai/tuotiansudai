package com.tuotiansudai.repository.model;

public enum ProductType {

    _30("30天", "SYL", "速盈利", 1, 30),
    _90("90天", "WYX", "稳盈绣", 3, 90),
    _180("180天", "JYF", "久盈富", 6, 180),
    _360("360天", "JYF", "久盈富", 12, 360),
    EXPERIENCE("新手体验标", "", "", 1, 3);

    private String name;

    private String productLine;
    
    private String productLineName;

    private int periods;

    private int duration;

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

    ProductType(String name, String productLine, String productLineName, int periods, int duration) {
        this.name = name;
        this.productLine = productLine;
        this.productLineName = productLineName;
        this.periods = periods;
        this.duration = duration;
    }
}
