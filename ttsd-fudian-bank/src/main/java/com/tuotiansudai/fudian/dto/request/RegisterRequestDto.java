package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class RegisterRequestDto extends UserBaseRequestDto {

    private String realName;

    private String identityType = "1";

    private String identityCode;

    private String mobilePhone;

    public RegisterRequestDto(Source source, String loginName, String mobile, String realName, String identityCode) {
        super(source, loginName, mobile, null, null, ApiType.REGISTER);
        this.realName = realName;
        this.identityCode = identityCode;
        this.mobilePhone = mobile;
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
