package com.tuotiansudai.repository.model;


import java.io.Serializable;

public class BookingLoanSumAmountView implements Serializable {
    private ProductType productType;
    private String sumAmount;

    public BookingLoanSumAmountView() {

    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }
}