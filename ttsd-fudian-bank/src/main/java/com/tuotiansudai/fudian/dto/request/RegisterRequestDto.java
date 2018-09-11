package com.tuotiansudai.fudian.dto.request;

public class RegisterRequestDto extends NotifyRequestDto {

    private String realName;

    private String roleType; // 1 借款人 3 出借人

    private String identityType = "1";

    private String identityCode;

    private String mobilePhone;

    public RegisterRequestDto(Source source, String loginName, String mobile, String realName, String roleType, String identityCode) {
        super(source, loginName, mobile, null, null);
        this.realName = realName;
        this.roleType = roleType;
        this.identityCode = identityCode;
        this.mobilePhone = mobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
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
