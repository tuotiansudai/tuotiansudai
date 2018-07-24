package com.tuotiansudai.fudian.umpdto;


import com.google.common.base.Strings;

public class UmpBindCardDto extends UmpBaseDto{

    private long bankCardModelId;

    private String userName;

    private String identityNumber;

    private String cardNumber;

    private boolean replaceCard;

    public UmpBindCardDto() {
    }

    public UmpBindCardDto(String loginName, String payUserId, long bankCardModelId, String userName, String identityNumber, String cardNumber, boolean replaceCard) {
        super(loginName, payUserId);
        this.bankCardModelId = bankCardModelId;
        this.userName = userName;
        this.identityNumber = identityNumber;
        this.cardNumber = cardNumber;
        this.replaceCard = replaceCard;
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

    public boolean isReplaceCard() {
        return replaceCard;
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
