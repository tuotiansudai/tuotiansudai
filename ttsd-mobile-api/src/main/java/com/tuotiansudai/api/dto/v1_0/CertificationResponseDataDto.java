package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CertificationResponseDataDto extends BaseResponseDataDto {
    /**
     * 用户实名认证使用的姓名
     */
    @ApiModelProperty(value = "用户实名认证使用的姓名", example = "王拓天")
    private String userRealName;

    /**
     * 用户实名认证使用的身份证号
     */
    @ApiModelProperty(value = "用户实名认证使用的身份证号", example = "365431111122331122")
    private String userIdCardNumber;

    public CertificationResponseDataDto() {
    }

    public CertificationResponseDataDto(String userRealName, String userIdCardNumber) {
        this.userRealName = userRealName;
        this.userIdCardNumber = userIdCardNumber;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserIdCardNumber() {
        return userIdCardNumber;
    }

    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }
}
