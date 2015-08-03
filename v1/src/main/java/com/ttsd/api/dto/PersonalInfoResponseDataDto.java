package com.ttsd.api.dto;


public class PersonalInfoResponseDataDto extends BaseResponseDataDto {
    private String userId;
    private String userName;
    private String phoneNum;
    private boolean certificationFlag;
    private String realName;
    private String idCard;
    private boolean isBoundBankCard;


    public boolean isCertificationFlag() {
        return certificationFlag;
    }

    public void setCertificationFlag(boolean certificationFlag) {
        this.certificationFlag = certificationFlag;
    }

    public boolean isBoundBankCard() {
        return isBoundBankCard;
    }

    public void setIsBoundBankCard(boolean isBoundBankCard) {
        this.isBoundBankCard = isBoundBankCard;
    }

    private String photo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }



    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
