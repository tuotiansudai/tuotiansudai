package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankRegisterDto extends BankBaseDto {

    private String token;

    private String realName;

    private String identityCode;

    public BankRegisterDto() {
    }

    public BankRegisterDto(String loginName, String mobile, String token, String realName, String identityCode) {
        super(loginName, mobile, null, null);
        this.token = token;
        this.realName = realName;
        this.identityCode = identityCode;
    }

    public String getToken() {
        return token;
    }

    public String getRealName() {
        return realName;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(getLoginName())
                && !Strings.isNullOrEmpty(getMobile())
                && !Strings.isNullOrEmpty(token)
                && !Strings.isNullOrEmpty(realName)
                && !Strings.isNullOrEmpty(identityCode);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
