package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.RegisterAccountDto;
import com.tuotiansudai.repository.model.UserModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CertificationRequestDto extends BaseParamDto {

    /**
     * 用户真实姓名
     */
    @NotEmpty(message = "0014")
    @ApiModelProperty(value = "用户真实姓名", example = "王拓天")
    private String userRealName;

    /**
     * 用户身份证号码
     */
    @NotEmpty(message = "0013")
    @ApiModelProperty(value = "用户身份证号码", example = "370412333221123431")
    private String userIdCardNumber;


    /**
     * 用户真实姓名
     *
     * @return
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * 用户真实姓名
     *
     * @param userRealName
     */
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    /**
     * 用户身份证号码
     *
     * @return
     */
    public String getUserIdCardNumber() {
        return userIdCardNumber == null ? null : userIdCardNumber.trim();
    }

    /**
     * 用户身份证号码
     *
     * @param userIdCardNumber
     */
    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }

    public RegisterAccountDto convertToRegisterAccountDto(UserModel userModel) {
        RegisterAccountDto registerAccountDto = new RegisterAccountDto();
        registerAccountDto.setIdentityNumber(this.getUserIdCardNumber());
        registerAccountDto.setUserName(this.getUserRealName());
        registerAccountDto.setLoginName(this.getBaseParam().getUserId());
        registerAccountDto.setMobile(userModel.getMobile());
        return registerAccountDto;

    }

    public CertificationRequestDto() {
    }

    public CertificationRequestDto(CommonCertificationRequestDto commonCertificationRequestDto) {
        this.userRealName = commonCertificationRequestDto.getUserRealName();
        this.userIdCardNumber = commonCertificationRequestDto.getUserIdCardNumber();
        this.getBaseParam().setPhoneNum(commonCertificationRequestDto.getMobile());
    }

}
