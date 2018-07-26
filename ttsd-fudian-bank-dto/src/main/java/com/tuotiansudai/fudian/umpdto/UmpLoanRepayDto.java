package com.tuotiansudai.fudian.umpdto;


import java.util.List;

public class UmpLoanRepayDto extends UmpBaseDto {

    private long loanId;

    private long loanRepayId;

    private long amount;

    private boolean isNormalRepay;

    private UmpRepayFeeDto umpRepayFeeDto;

    private List<UmpInvestRepayDto> umpInvestRepayDtoList;

    private List<UmpCouponRepayDto> umpCouponRepayDtoList;

    private List<UmpExtraRateRepayDto> umpExtraRateRepayDtoList;

    public UmpLoanRepayDto() {
    }

    public UmpLoanRepayDto(String loginName, String payUserId, long loanId, long loanRepayId, long amount, boolean isNormalRepay, List<UmpInvestRepayDto> umpInvestRepayDtoList, List<UmpCouponRepayDto> umpCouponRepayDtoList, List<UmpExtraRateRepayDto> umpExtraRateRepayDtoList) {
        super(loginName, payUserId);
        this.loanId = loanId;
        this.loanRepayId = loanRepayId;
        this.amount = amount;
        this.isNormalRepay = isNormalRepay;
        this.umpInvestRepayDtoList = umpInvestRepayDtoList;
        this.umpCouponRepayDtoList = umpCouponRepayDtoList;
        this.umpExtraRateRepayDtoList = umpExtraRateRepayDtoList;
        this.umpRepayFeeDto = new UmpRepayFeeDto(loanId, loanRepayId, amount - umpInvestRepayDtoList.stream().mapToLong(dto -> dto.getCorpus() + dto.getInterest() + dto.getDefaultFee() - dto.getFee()).sum());
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

    public boolean getIsNormalRepay() {
        return isNormalRepay;
    }

    public UmpRepayFeeDto getUmpRepayFeeDto() {
        return umpRepayFeeDto;
    }

    public List<UmpInvestRepayDto> getUmpInvestRepayDtoList() {
        return umpInvestRepayDtoList;
    }

    public List<UmpCouponRepayDto> getUmpCouponRepayDtoList() {
        return umpCouponRepayDtoList;
    }

    public List<UmpExtraRateRepayDto> getUmpExtraRateRepayDtoList() {
        return umpExtraRateRepayDtoList;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && loanId > 0
                && loanRepayId > 0
                && amount > 0
                && umpInvestRepayDtoList != null && umpInvestRepayDtoList.size() > 0
                && amount >= umpInvestRepayDtoList.stream().mapToLong(dto -> dto.getCorpus() + dto.getInterest() + dto.getDefaultFee()).sum();
    }
}
