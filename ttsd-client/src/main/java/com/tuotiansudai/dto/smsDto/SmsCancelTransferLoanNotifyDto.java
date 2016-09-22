package com.tuotiansudai.dto.smsDto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class SmsCancelTransferLoanNotifyDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[\\dZR-]{14}$")
    private String transferLoanName;

    public SmsCancelTransferLoanNotifyDto() {
    }

    public SmsCancelTransferLoanNotifyDto(String mobile, String transferLoanName) {
        this.mobile = mobile;
        this.transferLoanName = transferLoanName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTransferLoanName() {
        return transferLoanName;
    }

    public void setTransferLoanName(String transferLoanName) {
        this.transferLoanName = transferLoanName;
    }
}
