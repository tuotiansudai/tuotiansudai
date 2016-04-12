package com.ttsd.special.services;

import com.ttsd.special.dto.InvestTopResponse;
import com.ttsd.special.dto.InvestTopStatPeriod;

public interface InvestmentTopService {
    public InvestTopResponse queryInvestTopResponse(InvestTopStatPeriod period);
    public InvestTopResponse queryInvestTopResponseNoCache(InvestTopStatPeriod period);
}
