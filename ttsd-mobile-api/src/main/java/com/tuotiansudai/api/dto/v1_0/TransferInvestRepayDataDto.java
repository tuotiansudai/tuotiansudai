package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.util.AmountConverter;

import java.text.SimpleDateFormat;

public class TransferInvestRepayDataDto extends BaseResponseDataDto {

    private String repayDate;
    private String expectedInterest;
    private String status;

    public TransferInvestRepayDataDto() {
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
