package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class BookingLoanModel implements Serializable{
    private long id;
    private String loginName;
    private String userName;
    private String mobile;
    private Source source;
    private Date bookingTime;
    private ProductType productType;
    private long amount;
    private Date noticeTime;
    private String status;
    private Date createTime;
    private Date updateTime;
    private boolean deleted;

    public BookingLoanModel(UserModel userModel,String userName,String source,Date bookingTime,String productType,long amount,Date createTime,boolean deleted){
        this.loginName = userModel.getLoginName();
        this.mobile = userModel.getMobile();
        this.userName = userName;
        this.source = Source.valueOf(source.toUpperCase());
        this.bookingTime = bookingTime;
        this.productType = ProductType.valueOf(productType);
        this.amount = amount;
        this.createTime = createTime;
        this.deleted = deleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
