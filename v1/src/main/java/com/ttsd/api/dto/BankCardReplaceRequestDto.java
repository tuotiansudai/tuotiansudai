package com.ttsd.api.dto;

public class BankCardReplaceRequestDto extends BaseParamDto{
    private String cardNo;

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
