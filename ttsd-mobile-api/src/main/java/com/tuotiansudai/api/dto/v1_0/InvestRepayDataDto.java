package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class InvestRepayDataDto extends BaseResponseDataDto {

    private int period;
    private String repayDate;
    private String actualRepayDate;
    private String expectedInterest;    //expectedBenefit
    private String actualInterest;  //actualBenefit
    private String status;
    private boolean isTransferred;

    public InvestRepayDataDto() {
    }

    public InvestRepayDataDto(InvestRepayModel investRepayModel, CouponRepayModel couponRepayModel) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.period = investRepayModel.getPeriod();
        this.repayDate = simpleDateFormat.format(investRepayModel.getRepayDate());
        if (null != investRepayModel.getActualRepayDate()) {
            this.actualRepayDate = simpleDateFormat.format(investRepayModel.getActualRepayDate());
        } else {
            this.actualRepayDate = "";
        }
        if (null != couponRepayModel) {
            this.expectedInterest = AmountConverter.convertCentToString(investRepayModel.getExpectedInterest() + couponRepayModel.getExpectedInterest());
            this.actualInterest = AmountConverter.convertCentToString(investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getCorpus() + couponRepayModel.getActualInterest() - couponRepayModel.getActualFee());
        } else {
            this.expectedInterest = AmountConverter.convertCentToString(investRepayModel.getExpectedInterest());
            this.actualInterest = AmountConverter.convertCentToString(investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getCorpus());
        }

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
