package com.ttsd.api.dao;

import com.esoft.jdp2p.repay.model.InvestRepay;

import java.util.List;

public interface MobileAppInvestRepayListDao {

    Integer getUserInvestRepayTotalCount(String userId, String[] status);

    List<InvestRepay> getUserInvestRepayList(Integer index, Integer pageSize, String userId, String[] status, String orderBy);
}
