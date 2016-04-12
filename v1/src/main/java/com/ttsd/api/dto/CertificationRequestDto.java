package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/7/28.
 */
public class CertificationRequestDto extends BaseParamDto {

    /**
     * 用户真实姓名
     */
    private String userRealName;

    /**
     * 用户身份证号码
     */
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

}
