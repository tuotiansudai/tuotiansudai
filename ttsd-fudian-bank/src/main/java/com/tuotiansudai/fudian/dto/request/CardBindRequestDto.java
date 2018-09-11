package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankBaseDto;

public class CardBindRequestDto extends NotifyRequestDto {

    public CardBindRequestDto(Source source, BankBaseDto bankBaseDto) {
        super(source, bankBaseDto.getLoginName(), bankBaseDto.getMobile(), bankBaseDto.getBankUserName(), bankBaseDto.getBankAccountNo());
    }
}