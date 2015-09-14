package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestSource;

public class InvestDto extends ProjectTransferDto{
    private InvestSource investSource;

    public InvestSource getInvestSource() {
        return investSource;
    }

    public void setInvestSource(InvestSource investSource) {
        this.investSource = investSource;
    }
}
