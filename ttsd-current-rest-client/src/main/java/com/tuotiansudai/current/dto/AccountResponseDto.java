package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AccountResponseDto {

    private Long id;

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

    @JsonProperty(value = "personal_max_redeem")
    private long personalMaxRedeem;

    @JsonProperty(value = "personal_available_redeem")
    private long personalAvailableRedeem;

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

    public long getPersonalMaxRedeem() {
        return personalMaxRedeem;
    }

    public long getPersonalAvailableRedeem() {
        return personalAvailableRedeem;
    }
}
