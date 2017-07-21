package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AccountResponseDto {

    private long id;

    @JsonProperty(value = "login_name")
    private String loginName;

    private long balance;

    @JsonProperty(value = "created_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonProperty(value = "updated_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @JsonProperty(value = "personal_max_deposit")
    private long personalMaxDeposit;

    public long getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public long getBalance() {
        return balance;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public long getPersonalMaxDeposit() {
        return personalMaxDeposit;
    }
}
