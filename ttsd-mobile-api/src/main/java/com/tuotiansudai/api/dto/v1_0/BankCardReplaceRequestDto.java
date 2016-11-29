package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;

public class BankCardReplaceRequestDto extends BaseParamDto{

    @ApiModelProperty(value = "要绑定的新卡号", example = "6222600001111111111")
    private String cardNo;

    @ApiModelProperty(value = "银行卡缩写", example = "ICBC")
    private String bankCode;

    @ApiModelProperty(value = "ip", example = "127.0.0.1")
    private String ip;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BindBankCardDto convertToBindBankCardDto(){
        BindBankCardDto bindBankCardDto = new BindBankCardDto();
        bindBankCardDto.setCardNumber(this.getCardNo());
        bindBankCardDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        bindBankCardDto.setLoginName(this.getBaseParam().getUserId());
        bindBankCardDto.setIp(this.getIp());
        bindBankCardDto.setDeviceId(this.getBaseParam().getDeviceId());
        bindBankCardDto.setBankCode(this.getBankCode());
        return bindBankCardDto;

    }


}
