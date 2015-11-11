package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.util.Date;

public class LoginLogPaginationItemDataDto {
    private String loginName;

    private Source source;

    private String ip;

    private Date loginTime;

    private boolean success;

    public LoginLogPaginationItemDataDto(String loginName, Source source, String ip, Date loginTime, boolean success) {
        this.loginName = loginName;
        this.source = source;
        this.ip = ip;
        this.loginTime = loginTime;
        this.success = success;
    }

    public String getLoginName() {
        return loginName;
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

    public boolean isSuccess() {
        return success;
    }
}
