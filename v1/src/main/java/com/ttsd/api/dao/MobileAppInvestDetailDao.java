package com.ttsd.api.dao;

import com.esoft.jdp2p.invest.model.Invest;

public interface MobileAppInvestDetailDao {
    /**
     * 查询指定用户的指定投资情况
     * @param investId
     * @param userId
     * @return
     */
    Invest getInvest(String investId, String userId);
}
