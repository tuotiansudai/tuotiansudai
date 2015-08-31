package com.ttsd.special.dao;

import com.ttsd.special.dto.InvestTopItem;

import java.util.Date;
import java.util.List;

public interface InvestmentTopDao {
    public List<InvestTopItem> StatInvestmentTop(Date beginTime, Date endTime);
}
