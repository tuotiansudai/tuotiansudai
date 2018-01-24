package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class BankCardRequestDto extends BaseParamDto {

    //用户ID
    @ApiModelProperty(value = "用户ID", example = "abcd1234")
    private String userId;

    //身份证号
    @ApiModelProperty(value = "身份证号", example = "371111111111111111")
    private String idCard;

    //开卡户名
    @ApiModelProperty(value = "开卡户名", example = "ICBC")
    private String realName;

    //银行卡号
    @ApiModelProperty(value = "银行卡号", example = "6222600001111111111")
    @Pattern(regexp = "\\d+")
    private String cardNo;

    //是否开通快捷支付
    @ApiModelProperty(value = "是否开通快捷支付", example = "true：是")
    private boolean isOpenFastPayment;

    /**
     * 查询绑卡状态：query_bind_status
     * 查询签约状态：query_sign_status
     * 慧租账户激活：huizu_active
     */
    @ApiModelProperty(value = "操作类型", example = "查询绑卡状态：query_bind_status,查询签约状态：query_sign_status,慧租账户激活：huizu_active")
    private String operationType;

    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额", example = "11.00")
    @NotEmpty(message = "充值金额不能为空")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "充值金额不正确")
    private String rechargeAmount;

    @ApiModelProperty(value = "ip", example = "127.0.0.1")
    private String ip;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public RechargeDto convertToRechargeDto() {
        RechargeDto rechargeDto = new RechargeDto();
        rechargeDto.setAmount(this.rechargeAmount);
        rechargeDto.setLoginName(this.userId);
        rechargeDto.setFastPay(this.isOpenFastPayment);
        rechargeDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        rechargeDto.setHuizuActive("huizu_active".equals(this.operationType));
        return rechargeDto;
    }

    public BindBankCardDto convertToBindBankCardDto() {
        BindBankCardDto bindBankCardDto = new BindBankCardDto();
        bindBankCardDto.setLoginName(this.getBaseParam().getUserId());
        bindBankCardDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        bindBankCardDto.setDeviceId(this.getBaseParam().getDeviceId());
        bindBankCardDto.setCardNumber(this.getCardNo());
        bindBankCardDto.setFastPay(this.isOpenFastPayment());
        bindBankCardDto.setIp(this.getIp());
        return bindBankCardDto;
    }

    public AgreementDto convertToAgreementDto() {
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        agreementDto.setLoginName(this.getUserId());
        agreementDto.setFastPay(this.isOpenFastPayment());
        agreementDto.setIp(this.getIp());
        agreementDto.setDeviceId(this.getBaseParam().getDeviceId());
        return agreementDto;
    }
}
