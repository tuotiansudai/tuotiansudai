package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;

import java.util.Date;

public class PrepareUserDto {
    private long id;
    private String mobile;
    private String referrerName;
    private String channel;
    private boolean register;
    private Date registerTime;
    private Date useTime;
    private String referrerMobile;

    public PrepareUserDto(PrepareUserModel prepareUserModel, UserModel referrerUserModel, UserModel userModel) {
        this.id = prepareUserModel.getId();
        this.mobile = prepareUserModel.getMobile();
        this.referrerName = referrerUserModel != null ? referrerUserModel.getUserName() : null;
        this.channel = prepareUserModel.getChannel();
        this.register = userModel != null;
        this.registerTime = register ? userModel.getRegisterTime() : null;
        this.useTime = prepareUserModel.getCreatedTime();
        this.referrerMobile = prepareUserModel.getReferrerMobile();
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

    public String getReferrerName() {
        return referrerName;
    }

    public void setReferrerName(String referrerName) {
        this.referrerName = referrerName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public String getReferrerMobile() {
        return referrerMobile;
    }

    public void setReferrerMobile(String referrerMobile) {
        this.referrerMobile = referrerMobile;
    }
}
