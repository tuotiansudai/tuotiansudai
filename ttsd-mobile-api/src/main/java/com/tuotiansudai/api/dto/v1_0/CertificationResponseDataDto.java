package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class CertificationResponseDataDto extends BaseResponseDataDto{
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


    /**
     * 用户实名认证使用的姓名
     * @return
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * 用户实名认证使用的姓名
     * @param userRealName
     */
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    /**
     * 用户实名认证使用的身份证号
     * @return
     */
    public String getUserIdCardNumber() {
        return userIdCardNumber;
    }

    /**
     * 用户实名认证使用的身份证号
     * @param userIdCardNumber
     */
    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }

}
