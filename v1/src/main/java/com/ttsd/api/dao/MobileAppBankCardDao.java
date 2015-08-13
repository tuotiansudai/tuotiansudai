package com.ttsd.api.dao;

import com.esoft.jdp2p.bankcard.model.BankCard;

/**
 * Created by tuotian on 15/8/7.
 */
public interface MobileAppBankCardDao {
    /**
     * @function 查询绑卡或签约状态
     * @param userId 绑卡或签约用户ID
     * @param operationType 操作类型
     * @return int
     */
    int queryBindAndSginStatus(String userId,String operationType);
}
