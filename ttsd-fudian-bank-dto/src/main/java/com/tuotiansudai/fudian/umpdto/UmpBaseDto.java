package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;
import com.tuotiansudai.fudian.dto.BankDtoValidator;

import java.io.Serializable;

public class UmpBaseDto implements Serializable, BankDtoValidator{

    private String loginName;

    private String payUserId;

    public UmpBaseDto() {
    }

    public UmpBaseDto(String loginName, String payUserId) {
        this.loginName = loginName;
        this.payUserId = payUserId;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getPayUserId() {
        return payUserId;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(loginName) && !Strings.isNullOrEmpty(payUserId);
    }
}
