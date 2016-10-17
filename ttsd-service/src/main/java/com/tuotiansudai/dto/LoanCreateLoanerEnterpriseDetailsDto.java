package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanerEnterpriseDetailsModel;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreateLoanerEnterpriseDetailsDto {
    @NotEmpty
    private String juristicPerson;

    @NotEmpty
    private String shareholder;

    @NotEmpty
    private String address;

    @NotEmpty
    private String purpose;

    public LoanCreateLoanerEnterpriseDetailsDto() {
    }

    public LoanCreateLoanerEnterpriseDetailsDto(LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel) {
        this.juristicPerson = loanerEnterpriseDetailsModel.getJuristicPerson();
        this.shareholder = loanerEnterpriseDetailsModel.getShareholder();
        this.address = loanerEnterpriseDetailsModel.getAddress();
        this.purpose = loanerEnterpriseDetailsModel.getPurpose();
    }

    public String getJuristicPerson() {
        return juristicPerson;
    }

    public void setJuristicPerson(String juristicPerson) {
        this.juristicPerson = juristicPerson;
    }

    public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
