package com.tuotiansudai.api.dto;

import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.repository.model.Source;

public class BankCardReplaceRequestDto extends BaseParamDto{
    private String cardNo;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BindBankCardDto convertToBindBankCardDto(){
        BindBankCardDto bindBankCardDto = new BindBankCardDto();
        bindBankCardDto.setCardNumber(this.getCardNo());
        bindBankCardDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        bindBankCardDto.setLoginName(this.getBaseParam().getUserId());
        return bindBankCardDto;

    }


}
