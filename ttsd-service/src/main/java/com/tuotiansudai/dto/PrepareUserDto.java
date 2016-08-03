package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.PrepareModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;

import java.util.Date;

public class PrepareUserDto {
    private long id;
    private String mobile;
    private String referrerName;
    private Source channel;
    private boolean register;
    private Date registerTime;
    private Date useTime;
    private String referrerMobile;

    public PrepareUserDto(PrepareModel prepareModel, AccountModel referrerAccountModel, UserModel useUserModel) {
        this.id = prepareModel.getId();
        this.mobile = prepareModel.getMobile();
        this.referrerName = referrerAccountModel != null ? referrerAccountModel.getUserName() : null;
        this.channel = prepareModel.getChannel();
        this.register = useUserModel != null;
        this.registerTime = register ? useUserModel.getRegisterTime() : null;
        this.useTime = prepareModel.getCreatedTime();
        this.referrerMobile = prepareModel.getReferrerMobile();
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

    public Source getChannel() {
        return channel;
    }

    public void setChannel(Source channel) {
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
