package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class CommonCertificationRequestDto {

    @NotEmpty(message = "0014")
    @ApiModelProperty(value = "用户真实姓名", example = "王拓天")
    private String userRealName;

    @NotEmpty(message = "0013")
    @ApiModelProperty(value = "用户身份证号码", example = "370412333221123431")
    private String userIdCardNumber;

    @NotEmpty(message = "0001")
    @Pattern(regexp = "^1\\d{10}$", message = "0002")
    @ApiModelProperty(value = "手机号", example = "13900000000")
    private String mobile;


    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserIdCardNumber() {
        return userIdCardNumber == null ? null : userIdCardNumber.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }
}
