package com.tuotiansudai.borrow.dto.response;


public class AuthenticationResponseDto extends BaseResponseDto{

    private boolean isRegister;

    private boolean isBindCard;

    private boolean isAutoRepay;

    private boolean isAnxin;

    public AuthenticationResponseDto() {
    }

    public AuthenticationResponseDto(boolean status, boolean isRegister, boolean isBindCard, boolean isAutoRepay, boolean isAnxin) {
        super(status);
        this.isRegister = isRegister;
        this.isBindCard = isBindCard;
        this.isAutoRepay = isAutoRepay;
        this.isAnxin = isAnxin;
    }

    public boolean getIsRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public boolean getIsBindCard() {
        return isBindCard;
    }

    public void setBindCard(boolean bindCard) {
        isBindCard = bindCard;
    }

    public boolean getIsAutoRepay() {
        return isAutoRepay;
    }

    public void setAutoRepay(boolean autoRepay) {
        isAutoRepay = autoRepay;
    }

    public boolean getIsAnxin() {
        return isAnxin;
    }

    public void setAnxin(boolean anxin) {
        isAnxin = anxin;
    }
}
