package com.tuotiansudai.repository.model;

import com.tuotiansudai.dto.LoanCreatePledgeEnterpriseRequestDto;

public class PledgeEnterpriseModel extends AbstractPledgeDetail {
    private String guarantee;

    public PledgeEnterpriseModel() {
    }

    public PledgeEnterpriseModel(long loanId, LoanCreatePledgeEnterpriseRequestDto pledgeDetailsDto) {
        super(loanId, pledgeDetailsDto.getPledgeLocation(), pledgeDetailsDto.getEstimateAmount());
        this.guarantee = pledgeDetailsDto.getGuarantee();
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }
}
