package com.tuotiansudai.api.dto;


import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.repository.model.Source;

public class AgreementOperateRequestDto extends BaseParamDto{

    private boolean fastPay;

    private boolean autoInvest;

    public boolean isFastPay() {
        return fastPay;
    }

    public void setFastPay(boolean fastPay) {
        this.fastPay = fastPay;
    }

    public boolean isAutoInvest() {
        return autoInvest;
    }

    public void setAutoInvest(boolean autoInvest) {
        this.autoInvest = autoInvest;
    }

    public AgreementDto convertToAgreementDto(){
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setFastPay(fastPay);
        agreementDto.setAutoInvest(autoInvest);
        agreementDto.setLoginName(this.getBaseParam().getUserId());
        agreementDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        return agreementDto;
    }
}
