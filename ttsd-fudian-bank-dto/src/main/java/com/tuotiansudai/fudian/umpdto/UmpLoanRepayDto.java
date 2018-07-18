package com.tuotiansudai.fudian.umpdto;


import java.util.List;

public class UmpLoanRepayDto extends UmpBaseDto{

    private long loanId;

    private long loanRepayId;

    private long amount;

    private boolean isAdvanceRepay;

    private UmpLoanRepayFeeDto umpLoanRepayFeeDto;

    private List<UmpInvestRepayDto> umpInvestRepayDtos;

    private List<UmpCouponRepayDto> umpCouponRepayDtos;

    private List<UmpExtraRateRepayDto> umpExtraRateRepayDtos;

    public UmpLoanRepayDto() {
    }

    public UmpLoanRepayDto(String loginName, String payUserId, long loanId, long loanRepayId, long amount, boolean isAdvanceRepay, UmpLoanRepayFeeDto umpLoanRepayFeeDto, List<UmpInvestRepayDto> umpInvestRepayDtos, List<UmpCouponRepayDto> umpCouponRepayDtos, List<UmpExtraRateRepayDto> umpExtraRateRepayDtos) {
        super(loginName, payUserId);
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.amount = amount;
        this.isAdvanceRepay = isAdvanceRepay;
        this.umpLoanRepayFeeDto = umpLoanRepayFeeDto;
        this.umpInvestRepayDtos = umpInvestRepayDtos;
        this.umpCouponRepayDtos = umpCouponRepayDtos;
        this.umpExtraRateRepayDtos = umpExtraRateRepayDtos;
    }

    public long getLoanId() {
        return loanId;
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isAdvanceRepay() {
        return isAdvanceRepay;
    }

    public UmpLoanRepayFeeDto getUmpLoanRepayFeeDto() {
        return umpLoanRepayFeeDto;
    }

    public List<UmpInvestRepayDto> getUmpInvestRepayDtos() {
        return umpInvestRepayDtos;
    }

    public List<UmpCouponRepayDto> getUmpCouponRepayDtos() {
        return umpCouponRepayDtos;
    }

    public List<UmpExtraRateRepayDto> getUmpExtraRateRepayDtos() {
        return umpExtraRateRepayDtos;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && loanRepayId > 0
                && amount > 0;
    }
}
