package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.RetrievePasswordDto;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class RetrievePasswordRequestDto extends BaseParamDto {
    /**
     * 手机号码
     */
    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    private String phoneNum;

    /**
     * 验证码
     */
    @NotEmpty(message = "0020")
    @Pattern(regexp = "^[0-9]{6}$", message = "0009")
    private String validateCode;

    /**
     * 密码
     */
    @NotEmpty(message = "0012")
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$", message = "0012")
    private String password;

    /**
     * 验证码类型
     */
    private String authType;



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

    /**
     * 验证码类型
     * @return
     */
    public String getAuthType() {
        return authType;
    }

    /**
     * 验证码类型
     * @param authType
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public RetrievePasswordDto convertToRetrievePasswordDto(){
        RetrievePasswordDto retrievePasswordDto = new RetrievePasswordDto();
        retrievePasswordDto.setCaptcha(this.getValidateCode());
        retrievePasswordDto.setMobile(this.getPhoneNum());
        retrievePasswordDto.setPassword(this.getPassword());
        return retrievePasswordDto;
    }
}
