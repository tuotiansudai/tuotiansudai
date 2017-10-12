package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.PledgeHouseModel;
import org.hibernate.validator.constraints.NotEmpty;

public class LoanCreatePledgeHouseRequestDto {
    @NotEmpty
    private String pledgeLocation;

    @NotEmpty
    private String estimateAmount;

    @NotEmpty
    private String pledgeLoanAmount;

    @NotEmpty
    private String square;

    @NotEmpty
    private String propertyCardId;

    @NotEmpty
    private String estateRegisterId;

    private String authenticAct;

    private String propertyRightCertificateId;

    public LoanCreatePledgeHouseRequestDto() {
    }

    public LoanCreatePledgeHouseRequestDto(PledgeHouseModel pledgeHouseModel) {
        this.pledgeLocation = pledgeHouseModel.getPledgeLocation();
        this.estimateAmount = pledgeHouseModel.getEstimateAmount();
        this.pledgeLoanAmount = pledgeHouseModel.getLoanAmount();
        this.square = pledgeHouseModel.getSquare();
        this.propertyCardId = pledgeHouseModel.getPropertyCardId();
        this.propertyRightCertificateId = pledgeHouseModel.getPropertyRightCertificateId();
        this.estateRegisterId = pledgeHouseModel.getEstateRegisterId();
        this.authenticAct = pledgeHouseModel.getAuthenticAct();
    }

    public String getPledgeLocation() {
        return pledgeLocation;
    }

    public void setPledgeLocation(String pledgeLocation) {
        this.pledgeLocation = pledgeLocation;
    }

    public String getEstimateAmount() {
        return estimateAmount;
    }

    public void setEstimateAmount(String estimateAmount) {
        this.estimateAmount = estimateAmount;
    }

    public String getPledgeLoanAmount() {
        return pledgeLoanAmount;
    }

    public void setPledgeLoanAmount(String pledgeLoanAmount) {
        this.pledgeLoanAmount = pledgeLoanAmount;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getPropertyCardId() {
        return propertyCardId;
    }

    public void setPropertyCardId(String propertyCardId) {
        this.propertyCardId = propertyCardId;
    }

    public String getPropertyRightCertificateId() {
        return propertyRightCertificateId;
    }

    public void setPropertyRightCertificateId(String propertyRightCertificateId) {
        this.propertyRightCertificateId = propertyRightCertificateId;
    }

    public String getEstateRegisterId() {
        return estateRegisterId;
    }

    public void setEstateRegisterId(String estateRegisterId) {
        this.estateRegisterId = estateRegisterId;
    }

    public String getAuthenticAct() {
        return authenticAct;
    }

    public void setAuthenticAct(String authenticAct) {
        this.authenticAct = authenticAct;
    }
}
