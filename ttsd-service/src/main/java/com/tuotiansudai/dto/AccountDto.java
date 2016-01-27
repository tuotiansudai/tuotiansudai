package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class AccountDto implements Serializable{

    private String order_id;

    @NotEmpty
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^[1-9]\\d{13,16}[a-zA-Z0-9]$")
    private String identityNumber;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public AccountDto(String loginName, String identityNumber) {
        this.loginName = loginName;
        this.identityNumber = identityNumber;
    }

    public AccountDto() {

    }
}
