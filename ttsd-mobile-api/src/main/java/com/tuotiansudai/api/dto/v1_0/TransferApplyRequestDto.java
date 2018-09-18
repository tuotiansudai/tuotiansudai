package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.dto.TransferApplicationDto;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

public class TransferApplyRequestDto extends BaseParamDto {

    @NotEmpty(message = "0023")
    @Pattern(regexp = "^\\d+$", message = "0023")
    @ApiModelProperty(value = "债权转让ID", example = "1001")
    private String transferInvestId;

    @NotEmpty(message = "0023")
    @ApiModelProperty(value = "转让金额", example = "1000")
    private String transferAmount;

    @ApiModelProperty(value = "来源", example = "WEB")
    private Source source;

    public TransferApplyRequestDto() {
    }

    public String getTransferInvestId() {
        return transferInvestId;
    }

    public void setTransferInvestId(String transferInvestId) {
        this.transferInvestId = transferInvestId;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public TransferApplicationDto convertToTransferApplicationDto() {
        TransferApplicationDto transferApplicationDto = new TransferApplicationDto();
        transferApplicationDto.setTransferInvestId(Long.parseLong(this.transferInvestId));
        transferApplicationDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        return transferApplicationDto;
    }
}

