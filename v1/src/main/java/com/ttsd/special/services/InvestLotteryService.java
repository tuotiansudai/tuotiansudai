package com.ttsd.special.services;

import com.esoft.archer.user.model.User;

/**
 * Created by Administrator on 2015/9/9.
 */
public interface InvestLotteryService {

    void insertIntoInvestLottery(String investId);

    void updateInvestLotteryGranted(long id,boolean granted);

    boolean WinningPersonIncome(String orderId, double bonus, String particUserId, User user);
}
