package com.ttsd.api.dao;

import com.ttsd.api.dto.ReferrerInvestResponseDataDto;

import java.util.List;

public interface MobileAppReferrerInvestListDao {

    Integer getTotalCount(String ReferrerId);

    List<ReferrerInvestResponseDataDto> getReferrerInvestList(Integer index, Integer pageSize, String referrerId);

    double getRewardTotalMoney(String referrerId);

    String getQueryCondition(String queryType);


}
