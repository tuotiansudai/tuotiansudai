package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class InvestRepayDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "期数", example = "1")
    private int period;

    @ApiModelProperty(value = "预计还款日", example = "2000-01-01")
    private String repayDate;

    @ApiModelProperty(value = "实际还款日", example = "2000-01-01")
    private String actualRepayDate;

    @ApiModelProperty(value = "应收金额", example = "1000")
    private String expectedInterest;    //expectedBenefit

    @ApiModelProperty(value = "实收金额", example = "1000")
    private String actualInterest;  //actualBenefit

    @ApiModelProperty(value = "回款状态", example = "REPAYING")
    private String status;

    @ApiModelProperty(value = "转让状态", example = "true")
    private boolean isTransferred;

    public InvestRepayDataDto() {
    }

    public InvestRepayDataDto(InvestRepayModel investRepayModel, long totalExpectedInterest, long totalActualInterest) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.period = investRepayModel.getPeriod();
        this.repayDate = simpleDateFormat.format(investRepayModel.getRepayDate());
        if (null != investRepayModel.getActualRepayDate()) {
            this.actualRepayDate = simpleDateFormat.format(investRepayModel.getActualRepayDate());
        } else {
            this.actualRepayDate = "";
        }
        this.expectedInterest = AmountConverter.convertCentToString(investRepayModel.getCorpus() + totalExpectedInterest);
        this.actualInterest = AmountConverter.convertCentToString(totalActualInterest);
        this.status = investRepayModel.getStatus().name();
        this.isTransferred = investRepayModel.isTransferred();
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(String actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTransferred() {
        return isTransferred;
    }

    public void setIsTransferred(boolean isTransferred) {
        this.isTransferred = isTransferred;
    }
}
