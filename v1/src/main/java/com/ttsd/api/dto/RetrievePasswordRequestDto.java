package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/7/29.
 */
public class RetrievePasswordRequestDto extends BaseParamDto {
    /**
     * 手机号码
     */
    private String phoneNum;

    /**
     * 验证码
     */
    private String validateCode;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     * @return
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * 手机号
     * @param phoneNum
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * 验证码
     * @return
     */
    public String getValidateCode() {
        return validateCode;
    }

    /**
     * 验证码
     * @param validateCode
     */
    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    /**
     * 密码
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
