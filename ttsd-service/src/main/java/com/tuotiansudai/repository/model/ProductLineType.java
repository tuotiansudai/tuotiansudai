package com.tuotiansudai.repository.model;

public enum ProductLineType {

    SYL("速盈利",1,10.00),
    WYX("稳盈绣",3,12.00),
    JYF("久盈富",6,14.00);

    private String productLineTypeName;

    private int productLineTypePeriod;

    private double productLineTypeBaseRate;

    public String getProductLineTypeName() {
        return productLineTypeName;
    }

    public void setProductLineTypeName(String productLineTypeName) {
        this.productLineTypeName = productLineTypeName;
    }

    public int getProductLineTypePeriod() {
        return productLineTypePeriod;
    }

    public void setProductLineTypePeriod(int productLineTypePeriod) {
        this.productLineTypePeriod = productLineTypePeriod;
    }

    public double getProductLineTypeBaseRate() {
        return productLineTypeBaseRate;
    }

    public void setProductLineTypeBaseRate(double productLineTypeBaseRate) {
        this.productLineTypeBaseRate = productLineTypeBaseRate;
    }

    ProductLineType(String productLineTypeName, int productLineTypePeriod, double productLineTypeBaseRate) {
        this.productLineTypeName = productLineTypeName;
        this.productLineTypePeriod = productLineTypePeriod;
        this.productLineTypeBaseRate = productLineTypeBaseRate;
    }
}
