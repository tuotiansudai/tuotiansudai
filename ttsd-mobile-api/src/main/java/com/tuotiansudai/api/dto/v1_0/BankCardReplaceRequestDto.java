package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.repository.model.Source;

public class BankCardReplaceRequestDto extends BaseParamDto{
    private String cardNo;

    private String bankCode;

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
