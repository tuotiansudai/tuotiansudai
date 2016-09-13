package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanerDetailsModel;
import com.tuotiansudai.repository.model.PledgeHouseModel;
import com.tuotiansudai.util.AmountConverter;

public class CreateHouseLoanDto extends AbstractCreateLoanDto {
    private String square;
    private String propertyCardId;
    private String estateRegisterId;
    private String authenticAct;

    public CreateHouseLoanDto() {
    }

    public CreateHouseLoanDto(LoanModel loanModel, LoanDetailsModel loanDetailsModel, LoanerDetailsModel loanerDetailsModel,
                              PledgeHouseModel pledgeHouseModel) {
        //LoanDto
        this.id = loanModel.getId();
        this.projectName = loanModel.getName();
        this.agentLoginName = loanModel.getAgentLoginName();
        this.type = loanModel.getType();
        this.periods = loanModel.getPeriods();
        this.pledgeType = loanModel.getPledgeType();
        this.minInvestAmount = AmountConverter.convertCentToString(loanModel.getMinInvestAmount());
        this.investIncreasingAmount = AmountConverter.convertCentToString(loanModel.getInvestIncreasingAmount());
        this.maxInvestAmount = AmountConverter.convertCentToString(loanModel.getMaxInvestAmount());
        this.activityType = loanModel.getActivityType();
        this.productType = loanModel.getProductType();
        this.activityRate = String.valueOf(loanModel.getActivityRate());
        this.basicRate = String.valueOf(loanModel.getBaseRate());
        this.contractId = loanModel.getContractId();
        this.fundraisingStartTime = loanModel.getFundraisingStartTime();
        this.fundraisingEndTime = loanModel.getFundraisingEndTime();
        this.raisingCompleteTime = loanModel.getRaisingCompleteTime();
        this.showOnHome = loanModel.isShowOnHome();
        this.loanAmount = AmountConverter.convertCentToString(loanModel.getLoanAmount());
        this.createdTime = loanModel.getCreatedTime();
        this.verifyTime = loanModel.getVerifyTime();
        this.recheckTime = loanModel.getRecheckTime();
        this.loanStatus = loanModel.getStatus();
        this.loanTitles = loanModel.getLoanTitles();
        this.createdLoginName = loanModel.getCreatedLoginName();
        this.verifyLoginName = loanModel.getVerifyLoginName();
        this.recheckLoginName = loanModel.getRecheckLoginName();
        this.duration = loanModel.getDuration();
        //LoanDetailsDto
        this.declaration = loanDetailsModel.getDeclaration();
        this.extraSource = loanDetailsModel.getExtraSource();
        this.activity = loanDetailsModel.isActivity();
        //LoanerDetailsDto
        this.loanerLoginName = loanerDetailsModel.getLoginName();
        this.loanerUserName = loanerDetailsModel.getUserName();
        this.loanerIdentityNumber = loanerDetailsModel.getIdentityNumber();
        this.loanerGender = loanerDetailsModel.getGender();
        this.loanerAge = loanerDetailsModel.getAge();
        this.loanerMarriage = loanerDetailsModel.getMarriage();
        this.loanerRegion = loanerDetailsModel.getRegion();
        this.loanerIncome = loanerDetailsModel.getIncome();
        this.loanerEmploymentStatus = loanerDetailsModel.getEmploymentStatus();
        //PledgeDetailsDto
        this.pledgeLocation = pledgeHouseModel.getPledgeLocation();
        this.estimateAmount = pledgeHouseModel.getEstimateAmount();
        this.pledgeLoanAmount = pledgeHouseModel.getLoanAmount();
        this.square = pledgeHouseModel.getSquare();
        this.propertyCardId = pledgeHouseModel.getPropertyCardId();
        this.estateRegisterId = pledgeHouseModel.getEstateRegisterId();
        this.authenticAct = pledgeHouseModel.getAuthenticAct();
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
