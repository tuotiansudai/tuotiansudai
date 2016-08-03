package com.tuotiansudai.dto;

public class PledgeHouseDto extends AbstractPledgeDetailsDto {
    private String square;
    private String propertyCardId;
    private String estateRegisterId;
    private String authenticAct;

    public PledgeHouseDto(long loanId, String pledgeLocation, String estimateAmount, String loanAmount, String square, String propertyCardId, String estateRegisterId, String authenticAct) {
        super(loanId, pledgeLocation, estimateAmount, loanAmount);
        this.square = square;
        this.propertyCardId = propertyCardId;
        this.estateRegisterId = estateRegisterId;
        this.authenticAct = authenticAct;
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
