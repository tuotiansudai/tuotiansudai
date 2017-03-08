package com.tuotiansudai.cfca.model;

import java.io.Serializable;
import java.util.Date;

public class AnxinCreateAccountResponseModel implements Serializable {

    private long id;

    private String txTime;

    private String retCode;

    private String retMessage;

    private String userId;

    private String personName;

    private String identTypeCode;

    private String identNo;

    private String email;

    private String mobilePhone;

    private String address;

    private String authenticationMode;

    private String notSendPwd;

    private String anxinSignEmail;

    private String anxinSignMobilePhone;

    private Date createdTime;

    public AnxinCreateAccountResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMessage() {
        return retMessage;
    }

    public void setRetMessage(String retMessage) {
        this.retMessage = retMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdentTypeCode() {
        return identTypeCode;
    }

    public void setIdentTypeCode(String identTypeCode) {
        this.identTypeCode = identTypeCode;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(String authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public String getNotSendPwd() {
        return notSendPwd;
    }

    public void setNotSendPwd(String notSendPwd) {
        this.notSendPwd = notSendPwd;
    }

    public String getAnxinSignEmail() {
        return anxinSignEmail;
    }

    public void setAnxinSignEmail(String anxinSignEmail) {
        this.anxinSignEmail = anxinSignEmail;
    }

    public String getAnxinSignMobilePhone() {
        return anxinSignMobilePhone;
    }

    public void setAnxinSignMobilePhone(String anxinSignMobilePhone) {
        this.anxinSignMobilePhone = anxinSignMobilePhone;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
