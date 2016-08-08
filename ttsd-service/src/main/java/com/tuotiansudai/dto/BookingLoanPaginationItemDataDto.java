package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.BookingLoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.AmountConverter;

import java.io.Serializable;
import java.util.Date;

public class BookingLoanPaginationItemDataDto implements Serializable {
    private long bookingLoanId;
    private String userName;
    private String mobile;
    private Source source;
    private Date bookingTime;
    private ProductType productType;
    private String amount;
    private Date noticeTime;
    private boolean status;

    public BookingLoanPaginationItemDataDto() {

    }

    public BookingLoanPaginationItemDataDto(BookingLoanModel bookingLoanModel) {
        this.bookingLoanId = bookingLoanModel.getId();
        this.mobile = bookingLoanModel.getMobile();
        this.source = bookingLoanModel.getSource();
        this.bookingTime = bookingLoanModel.getBookingTime();
        this.productType = bookingLoanModel.getProductType();
        this.amount = AmountConverter.convertCentToString(bookingLoanModel.getAmount());
        this.noticeTime = bookingLoanModel.getNoticeTime();
        this.status = bookingLoanModel.isStatus();
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getBookingLoanId() {
        return bookingLoanId;
    }

    public void setBookingLoanId(long bookingLoanId) {
        this.bookingLoanId = bookingLoanId;
    }
}