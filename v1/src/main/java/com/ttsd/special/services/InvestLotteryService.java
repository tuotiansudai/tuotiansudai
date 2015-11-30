package com.ttsd.special.services;

import com.ttsd.special.model.InvestLottery;

import java.util.List;
import com.ttsd.special.model.InvestLotteryType;

import com.ttsd.special.dto.LotteryPrizeResponseDto;
import com.ttsd.special.model.ReceiveStatus;

/**
 * Created by Administrator on 2015/9/9.
 */
public interface InvestLotteryService {

    void insertIntoInvestLottery(String investId);

    void updateInvestLotteryGranted(long id,ReceiveStatus receiveStatus);

    LotteryPrizeResponseDto getLotteryPrize(InvestLotteryType investLotteryType);

    boolean winningPersonIncome(String orderId, String money, String userId);

    List<InvestLottery> findInvestLotteryTops(int limit);

    int getRemainingTimes(String userName, InvestLotteryType type);

}
