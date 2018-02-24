package com.tuotiansudai.api.dto.v1_0;


import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;

public class AgreementOperateRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "自动投资类型", example = "//AUTO_INVEST：自动投标 ，NO_PASSWORD_INVEST：免密投资")
    private AgreementBusinessType type;

    public AgreementBusinessType getType() {
        return type;
    }

    public void setType(AgreementBusinessType type) {
        this.type = type;
    }

    public AgreementDto convertToAgreementDto() {
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setNoPasswordInvest(AgreementBusinessType.NO_PASSWORD_INVEST.equals(type));
        agreementDto.setLoginName(this.getBaseParam().getUserId());
        agreementDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        agreementDto.setDeviceId(this.getBaseParam().getDeviceId());
        agreementDto.setHuizuAutoRepay(AgreementBusinessType.HUIZU_AUTO_REPAY.equals(type));
        return agreementDto;
    }

}
