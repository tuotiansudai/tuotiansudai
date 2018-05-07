package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class RegisterRequestDto extends UserBaseRequestDto {

    private String realName;

    private String identityType = "1";

    private String identityCode;

    private String mobilePhone;

    public RegisterRequestDto(String loginName, String realName, String identityCode, String mobilePhone) {
        super(null, null, ApiType.REGISTER, loginName, mobilePhone);
        this.realName = realName;
        this.identityCode = identityCode;
        this.mobilePhone = mobilePhone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
