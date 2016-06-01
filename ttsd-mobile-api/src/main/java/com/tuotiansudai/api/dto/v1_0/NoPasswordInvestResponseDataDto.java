package com.tuotiansudai.api.dto.v1_0;


public class NoPasswordInvestResponseDataDto extends BaseResponseDataDto {
    private boolean autoInvest;
    private boolean noPasswordInvest;

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public boolean isNoPasswordInvest() {
        return noPasswordInvest;
    }

    public void setNoPasswordInvest(boolean noPasswordInvest) {
        this.noPasswordInvest = noPasswordInvest;
    }
}
