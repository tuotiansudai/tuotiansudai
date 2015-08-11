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
    private boolean isOpenFastPayment;

    /**
     * 查询绑卡状态：query_bind_status
     * 查询签约状态：query_sign_status
     */
    private String operationType;

    /**
     * 充值金额
     */
    private String rechargeAmount;

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

    public boolean isOpenFastPayment() {
        return isOpenFastPayment;
    }

    public void setIsOpenFastPayment(boolean isOpenFastPayment) {
        this.isOpenFastPayment = isOpenFastPayment;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
}
