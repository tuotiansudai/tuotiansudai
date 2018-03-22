package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class RegisterResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "用户名", example = "wangtuotian")
    private String userId;

    @ApiModelProperty(value = "姓名", example = "王拓天")
    private String userName;

    @ApiModelProperty(value = "手机", example = "13900000022")
    private String phoneNum;

    @ApiModelProperty(value = "token", example = "d96078c8-9bc8-4525-a830-a7e4c7dfb8f6")
    private String token;
    @ApiModelProperty(value = "identityNumber", example = "110000000000000000")
    private String identityNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }
}
