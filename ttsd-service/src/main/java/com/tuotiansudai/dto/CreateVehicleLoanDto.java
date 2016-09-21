package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanerDetailsModel;
import com.tuotiansudai.repository.model.PledgeVehicleModel;
import com.tuotiansudai.util.AmountConverter;

public class CreateVehicleLoanDto extends AbstractCreateLoanDto {
    private String brand;
    private String model;

    public CreateVehicleLoanDto() {
    }

    public CreateVehicleLoanDto(LoanModel loanModel, LoanDetailsModel loanDetailsModel, LoanerDetailsModel loanerDetailsModel,
                                PledgeVehicleModel pledgeVehicleModel) {
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
        this.activityDesc = loanDetailsModel.getActivityDesc();
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
        this.pledgeLocation = pledgeVehicleModel.getPledgeLocation();
        this.estimateAmount = pledgeVehicleModel.getEstimateAmount();
        this.pledgeLoanAmount = pledgeVehicleModel.getLoanAmount();
        this.brand = pledgeVehicleModel.getBrand();
        this.model = pledgeVehicleModel.getModel();
    }

    public AbstractPledgeDetailsDto getPledgeDetailsDto() {
        return new PledgeVehicleDto(id, pledgeLocation, estimateAmount, pledgeLoanAmount, brand, model);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
