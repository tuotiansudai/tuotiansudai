package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreatePledgeHouseRequestDto;
import com.tuotiansudai.dto.PledgeHouseDto;

public class PledgeHouseModel extends AbstractPledgeDetail {
    private String loanAmount;
    private String square;
    private String propertyCardId;
    private String propertyRightCertificateId;
    private String estateRegisterId;
    private String authenticAct;

    public PledgeHouseModel() {
        super();
    }

    public PledgeHouseModel(long loanId, String pledgeLocation, String estimateAmount, String loanAmount, String square, String propertyCardId, String propertyRightCertificateId, String estateRegisterId, String authenticAct) {
        super(loanId, pledgeLocation, estimateAmount);
        this.loanAmount = loanAmount;
        this.square = square;
        this.propertyCardId = propertyCardId;
        this.propertyRightCertificateId = propertyRightCertificateId;
        this.estateRegisterId = estateRegisterId;
        this.authenticAct = authenticAct;
    }

    public PledgeHouseModel(PledgeHouseDto pledgeHouseDto) {
        this.loanId = pledgeHouseDto.getLoanId();
        this.pledgeLocation = pledgeHouseDto.getPledgeLocation();
        this.estimateAmount = pledgeHouseDto.getEstimateAmount();
        this.loanAmount = pledgeHouseDto.getLoanAmount();
        this.square = pledgeHouseDto.getSquare();
        this.propertyCardId = pledgeHouseDto.getPropertyCardId();
        this.propertyRightCertificateId = pledgeHouseDto.getPropertyRightCertificateId();
        this.estateRegisterId = pledgeHouseDto.getEstateRegisterId();
        this.authenticAct = pledgeHouseDto.getAuthenticAct();
    }

    public PledgeHouseModel(long loanId, LoanCreatePledgeHouseRequestDto pledgeHouse) {
        this.loanId = loanId;
        this.pledgeLocation = pledgeHouse.getPledgeLocation();
        this.estimateAmount = pledgeHouse.getEstimateAmount();
        this.loanAmount = pledgeHouse.getPledgeLoanAmount();
        this.square = pledgeHouse.getSquare();
        this.propertyCardId = pledgeHouse.getPropertyCardId();
        this.propertyRightCertificateId = pledgeHouse.getPropertyRightCertificateId();
        this.estateRegisterId = pledgeHouse.getEstateRegisterId();
        this.authenticAct = pledgeHouse.getAuthenticAct();
    }

    public String getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(String loanAmount) {
        this.loanAmount = loanAmount;
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
