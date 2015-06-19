package com.tuotiansudai.repository.model;

import java.util.Date;

public class UserModel {

    private int id;

    private String login_Name;

    private String password;

    private String email;

    private String address;

    private String mobile_Number;

    private Date last_Login_Time;

    private Date register_Time;

    private Date last_Modified_Time;

    private String last_Modified_User;

    private Date forbidden_Time;

    private String avatar;

    private String referrer;

    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin_Name() {
        return login_Name;
    }

    public void setLogin_Name(String login_Name) {
        this.login_Name = login_Name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_Number() {
        return mobile_Number;
    }

    public void setMobile_Number(String mobile_Number) {
        this.mobile_Number = mobile_Number;
    }

    public Date getLast_Login_Time() {
        return last_Login_Time;
    }

    public void setLast_Login_Time(Date last_Login_Time) {
        this.last_Login_Time = last_Login_Time;
    }

    public Date getRegister_Time() {
        return register_Time;
    }

    public void setRegister_Time(Date register_Time) {
        this.register_Time = register_Time;
    }

    public Date getLast_Modified_Time() {
        return last_Modified_Time;
    }

    public void setLast_Modified_Time(Date last_Modified_Time) {
        this.last_Modified_Time = last_Modified_Time;
    }

    public String getLast_Modified_User() {
        return last_Modified_User;
    }

    public void setLast_Modified_User(String last_Modified_User) {
        this.last_Modified_User = last_Modified_User;
    }

    public Date getForbidden_Time() {
        return forbidden_Time;
    }

    public void setForbidden_Time(Date forbidden_Time) {
        this.forbidden_Time = forbidden_Time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




}
