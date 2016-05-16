package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.RegisterAccountDto;
import org.hibernate.validator.constraints.NotEmpty;

public class CertificationRequestDto extends BaseParamDto {

    /**
     * 用户真实姓名
     */
    @NotEmpty(message = "0014")
    private String userRealName;

    /**
     * 用户身份证号码
     */
    @NotEmpty(message = "0013")
    private String userIdCardNumber;


    /**
     * 用户真实姓名
     * @return
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * 用户真实姓名
     * @param userRealName
     */
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    /**
     * 用户身份证号码
     * @return
     */
    public String getUserIdCardNumber() {
        return userIdCardNumber == null ? null : userIdCardNumber.trim();
    }

    /**
     * 用户身份证号码
     * @param userIdCardNumber
     */
    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }

    public RegisterAccountDto convertToRegisterAccountDto(){
        RegisterAccountDto registerAccountDto = new RegisterAccountDto();
        registerAccountDto.setIdentityNumber(this.getUserIdCardNumber());
        registerAccountDto.setUserName(this.getUserRealName());
        registerAccountDto.setLoginName(this.getBaseParam().getUserId());
        registerAccountDto.setMobile(this.getBaseParam().getPhoneNum());
        return registerAccountDto;

    }

}
