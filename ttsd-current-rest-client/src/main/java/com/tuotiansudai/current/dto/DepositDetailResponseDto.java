package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class DepositDetailResponseDto implements Serializable {

    private long id;

    @JsonProperty(value = "login_name")
    private String loginName;

    private long amount;

    private Source source;

    @JsonProperty(value = "no_password")
    private boolean noPassword;

    @JsonProperty(value = "created_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonProperty(value = "updated_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    public long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getAmount() {
        return amount;
    }

    public Source getSource() {
        return source;
    }

    public boolean isNoPassword() {
        return noPassword;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }
}
