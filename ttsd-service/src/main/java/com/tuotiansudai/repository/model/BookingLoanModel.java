package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class BookingLoanModel implements Serializable{
    private long id;
    private String mobile;
    private Source source;
    private Date bookingTime;
    private ProductType productType;
    private long amount;
    private Date noticeTime;
    private boolean status;
    private Date createTime;
    private Date updateTime;

    public BookingLoanModel(String mobile, Source source, Date bookingTime, ProductType productType, long amount, Date noticeTime, boolean status, Date createTime) {
        this.mobile = mobile;
        this.source = source;
        this.bookingTime = bookingTime;
        this.productType = productType;
        this.amount = amount;
        this.status = status;
        this.createTime = createTime;
    }
    public BookingLoanModel(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Date noticeTime) {
        this.noticeTime = noticeTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
