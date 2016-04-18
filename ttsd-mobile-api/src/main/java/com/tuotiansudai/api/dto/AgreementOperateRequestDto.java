package com.tuotiansudai.api.dto;


import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.repository.model.Source;

public class AgreementOperateRequestDto extends BaseParamDto {

    private AgreementBusinessType type;

    public AgreementBusinessType getType() {
        return type;
    }

    public void setType(AgreementBusinessType type) {
        this.type = type;
    }

    public AgreementDto convertToAgreementDto() {
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setAutoInvest(type == null || type == AgreementBusinessType.AUTO_INVEST);
        agreementDto.setNoPasswordInvest(type != null && type == AgreementBusinessType.NO_PASSWORD_INVEST);
        agreementDto.setLoginName(this.getBaseParam().getUserId());
        agreementDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        return agreementDto;
    }

}
