package com.tuotiansudai.api.dto;

import com.tuotiansudai.dto.RegisterAccountDto;
import org.hibernate.validator.constraints.NotEmpty;

public class CertificationRequestDto extends BaseParamDto {

    /**
     * 用户真实姓名
     */
    @NotEmpty
    private String userRealName;

    /**
     * 用户身份证号码
     */
    @NotEmpty
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
        return userIdCardNumber;
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
        return registerAccountDto;

    }

}
