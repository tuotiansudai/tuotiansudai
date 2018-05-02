package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanerEnterpriseDetailsModel;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreateLoanerEnterpriseDetailsDto {
    @NotEmpty
    private String juristicPerson;

    @NotEmpty
    private String address;

    @NotEmpty
    private String purpose;

    @NotEmpty
    private String source;

    public LoanCreateLoanerEnterpriseDetailsDto() {
    }

    public LoanCreateLoanerEnterpriseDetailsDto(LoanerEnterpriseDetailsModel loanerEnterpriseDetailsModel) {
        this.juristicPerson = loanerEnterpriseDetailsModel.getJuristicPerson();
        this.address = loanerEnterpriseDetailsModel.getAddress();
        this.purpose = loanerEnterpriseDetailsModel.getPurpose();
        this.source = loanerEnterpriseDetailsModel.getSource();
    }

    public String getJuristicPerson() {
        return juristicPerson;
    }

    public void setJuristicPerson(String juristicPerson) {
        this.juristicPerson = juristicPerson;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
