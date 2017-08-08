package com.tuotiansudai.current.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class DepositDto {
    private Long id;
    @JsonProperty("login_name")
    private String loginName;
    private long amount;
    @NotNull
    private Source source;
    @NotNull
    private DepositStatus status;
    @JsonProperty("no_password")
    private boolean noPassword;

    public DepositDto() {
    }

    public DepositDto(Long id, String loginName, long amount, Source source, DepositStatus status, boolean noPassword) {
        this.id = id;
        this.loginName = loginName;
        this.amount = amount;
        this.source = source;
        this.status = status;
        this.noPassword = noPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public DepositStatus getStatus() {
        return status;
    }

    public void setStatus(DepositStatus status) {
        this.status = status;
    }

    public boolean isNoPassword() {
        return noPassword;
    }

    public void setNoPassword(boolean noPassword) {
        this.noPassword = noPassword;
    }
}
