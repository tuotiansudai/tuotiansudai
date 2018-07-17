package com.tuotiansudai.repository.model;


public class ReconciliationModel {

    private String orderNo;

    private String status;

    private long amount;

    private int billCount;

    public ReconciliationModel() {
    }

    public ReconciliationModel(String orderNo, long amount) {
        this.orderNo = orderNo;
        this.amount = amount;
        this.billCount = 0;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getBillCount() {
        return billCount;
    }

    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }
}
