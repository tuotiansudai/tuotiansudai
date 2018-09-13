package com.tuotiansudai.borrow.dto.request;


import com.google.common.base.Strings;

import java.io.Serializable;

public class BaseRequestDto implements Serializable, BaseDtoValidator{

    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(mobile);
    }
}
