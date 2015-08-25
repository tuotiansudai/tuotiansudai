package com.ttsd.special.services;

import com.ttsd.special.dto.InvestTopList;
import com.ttsd.special.dto.InvestTopStatPeriod;

public interface InvestmentTopService {
    public InvestTopList queryInvestTopListCache(InvestTopStatPeriod period);
    public InvestTopList queryInvestTopList(InvestTopStatPeriod period);
}
