package com.ttsd.api.dto;


public class PersonalInfoResponseDataDto extends BaseResponseDataDto {
    private String userId;
    private String userName;
    private String phoneNum;
    private boolean certificationFlag;
    private String realName;
    private String idCard;
    private boolean isBoundBankCard;
    private String photo;
    private boolean isFastPayment;
    private String bankCardNo;
    private String bankId;
    private boolean fastPaymentEnable;


    public boolean getCertificationFlag() {
        return certificationFlag;
    }

    public void setCertificationFlag(boolean certificationFlag) {
        this.certificationFlag = certificationFlag;
    }

    public boolean getBoundBankCard() {
        return isBoundBankCard;
    }

    public void setIsBoundBankCard(boolean isBoundBankCard) {
        this.isBoundBankCard = isBoundBankCard;
    }

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

    public boolean getFastPayment() {
        return isFastPayment;
    }

    public void setIsFastPayment(boolean isFastPayment) {
        this.isFastPayment = isFastPayment;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public boolean getFastPaymentEnable() {
        return fastPaymentEnable;
    }

    public void setFastPaymentEnable(boolean fastPaymentEnable) {
        this.fastPaymentEnable = fastPaymentEnable;
    }
}
