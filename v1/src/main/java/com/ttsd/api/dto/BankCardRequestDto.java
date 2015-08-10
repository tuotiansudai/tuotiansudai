package com.ttsd.api.dto;

/**
 * Created by tuotian on 15/8/7.
 */
public class BankCardRequestDto extends BaseParamDto{

    //用户ID
    private String userId;

    //身份证号
    private String idCard;

    //开卡户名
    private String realName;

    //银行卡号
    private String cardNo;

    //是否开通快捷支付
    private String isOpenFastPayment;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIsOpenFastPayment() {
        return isOpenFastPayment;
    }

    public void setIsOpenFastPayment(String isOpenFastPayment) {
        this.isOpenFastPayment = isOpenFastPayment;
    }
}
