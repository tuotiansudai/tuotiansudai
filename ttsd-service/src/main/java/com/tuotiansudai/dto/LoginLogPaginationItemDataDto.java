package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.util.Date;

public class LoginLogPaginationItemDataDto {
    private String mobile;
    private Source source;
    private String ip;
    private Date loginTime;
    private String device;
    private boolean success;

    public LoginLogPaginationItemDataDto(String mobile, Source source, String ip, String device, Date loginTime, boolean success) {
        this.mobile = mobile;
        this.source = source;
        this.ip = ip;
        this.device = device;
        this.loginTime = loginTime;
        this.success = success;
    }

    public String getMobile() {
        return mobile;
    }

    public Source getSource() {
        return source;
    }

    public String getIp() {
        return ip;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public String getDevice() {
        return device;
    }

    public boolean isSuccess() {
        return success;
    }
}
