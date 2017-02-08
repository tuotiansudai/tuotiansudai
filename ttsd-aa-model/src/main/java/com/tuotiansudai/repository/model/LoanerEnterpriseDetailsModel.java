package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreateLoanerEnterpriseDetailsDto;

import java.io.Serializable;

public class LoanerEnterpriseDetailsModel implements Serializable {
    private long id;
    private long loanId;
    private String juristicPerson;
    private String shareholder;
    private String address;
    private String purpose;

    public LoanerEnterpriseDetailsModel() {
    }

    public LoanerEnterpriseDetailsModel(long loanId, LoanCreateLoanerEnterpriseDetailsDto dto) {
        this.loanId = loanId;
        this.juristicPerson = dto.getJuristicPerson();
        this.shareholder = dto.getShareholder();
        this.address = dto.getAddress();
        this.purpose = dto.getPurpose();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
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
