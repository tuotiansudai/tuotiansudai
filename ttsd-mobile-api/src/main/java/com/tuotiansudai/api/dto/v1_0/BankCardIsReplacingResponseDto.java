package com.tuotiansudai.api.dto.v1_0;


public class BankCardIsReplacingResponseDto extends BaseResponseDataDto{

    private boolean isReplacing;

    private boolean isManual;

    public BankCardIsReplacingResponseDto(boolean isReplacing, boolean isManual) {
        this.isReplacing = isReplacing;
        this.isManual = isManual;
    }

    public boolean isReplacing() {
        return isReplacing;
    }

    public void setIsReplacing(boolean isReplacing) {
        this.isReplacing = isReplacing;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setIsManual(boolean isManual) {
        this.isManual = isManual;
    }
}
