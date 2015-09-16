package com.ttsd.special.services;

import com.ttsd.special.dto.LotteryPrizeResponseDto;
import com.ttsd.special.model.InvestLotteryType;

/**
 * Created by Administrator on 2015/9/9.
 */
public interface InvestLotteryService {

    public void insertIntoInvestLottery(String investId);

    LotteryPrizeResponseDto getLotteryPrize(InvestLotteryType investLotteryType);

}
