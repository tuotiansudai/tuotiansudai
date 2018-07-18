package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;

public class UmpReplaceBindCardDto extends UmpBaseDto{

    private long bankCardModelId;

    private String userName;

    private String identityNumber;

    private String cardNumber;

    public UmpReplaceBindCardDto() {
    }

    public UmpReplaceBindCardDto(String loginName, String payUserId, long bankCardModelId, String userName, String identityNumber, String cardNumber) {
        super(loginName, payUserId);
        this.bankCardModelId = bankCardModelId;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.cardNumber = cardNumber;
    }

    public long getBankCardModelId() {
        return bankCardModelId;
    }

    public String getUserName() {
        return userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && bankCardModelId > 0
                && !Strings.isNullOrEmpty(userName)
                && !Strings.isNullOrEmpty(identityNumber)
                && !Strings.isNullOrEmpty(cardNumber);
    }
}
