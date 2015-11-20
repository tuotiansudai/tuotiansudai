package com.tuotiansudai.api.dto;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.Source;

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

    public RechargeDto convertToRechargeDto(){
        RechargeDto rechargeDto = new RechargeDto();
        rechargeDto.setAmount(this.rechargeAmount);
        rechargeDto.setLoginName(this.userId);
        rechargeDto.setFastPay(this.isOpenFastPayment);
        rechargeDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        return rechargeDto;
    }

    public BindBankCardDto convertToBindBankCardDto(){
        BindBankCardDto bindBankCardDto = new BindBankCardDto();
        bindBankCardDto.setLoginName(this.getBaseParam().getUserId());
        bindBankCardDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        bindBankCardDto.setCardNumber(this.getCardNo());
        bindBankCardDto.setFastPay(this.isOpenFastPayment());
        return bindBankCardDto;
    }

    public AgreementDto convertToAgreementDto(){
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        agreementDto.setLoginName(this.getUserId());
        agreementDto.setFastPay(this.isOpenFastPayment());
        return agreementDto;
    }
}
