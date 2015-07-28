package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/7/28.
 */
public class CertificationDataDto {
    /**
     * 用户实名认证使用的姓名（已经过处理）
     */
    private String userRealName;

    /**
     * 用户实名认证使用的身份证号（已经过处理）
     */
    private String userIdCardNumber;



    /**
     * 用户实名认证使用的姓名（已经过处理）
     * @return
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * 用户实名认证使用的姓名（已经过处理）
     * @param userRealName
     */
    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    /**
     * 用户实名认证使用的身份证号（已经过处理）
     * @return
     */
    public String getUserIdCardNumber() {
        return userIdCardNumber;
    }

    /**
     * 用户实名认证使用的身份证号（已经过处理）
     * @param userIdCardNumber
     */
    public void setUserIdCardNumber(String userIdCardNumber) {
        this.userIdCardNumber = userIdCardNumber;
    }
}
