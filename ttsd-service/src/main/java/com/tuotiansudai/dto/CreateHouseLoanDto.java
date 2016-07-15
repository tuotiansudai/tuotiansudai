package com.tuotiansudai.dto;

public class CreateHouseLoanDto extends AbstractCreateLoanDto {
    private String square;
    private String propertyCardId;
    private String estateRegisterId;
    private String authenticAct;

    public CreateHouseLoanDto() {
    }

    public AbstractPledgeDetailsDto getPledgeDetailsDto() {
        return new PledgeHouseDto(id, pledgeLocation, estimateAmount, pledgeLoanAmount, square, propertyCardId, estateRegisterId, authenticAct);
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
