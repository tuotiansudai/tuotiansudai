package com.ttsd.api.dao;

import com.ttsd.api.dto.InvestDto;

import java.util.List;

public interface InvestListDao {
    String getLimitCondition(String index, String pageSize);

    boolean isHasNextPage(Integer index, Integer pageSize);

    List<InvestDto> getInvestList(Integer index,Integer pageSize);
}
