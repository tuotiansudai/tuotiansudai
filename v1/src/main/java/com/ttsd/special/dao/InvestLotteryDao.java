package com.ttsd.special.dao;

import com.ttsd.special.model.InvestLottery;
import com.ttsd.special.model.InvestLotteryType;

import java.util.List;

public interface InvestLotteryDao {
     List<InvestLottery> findInvestLotteryByType(InvestLotteryType type);

     void updateInvestLottery(InvestLottery investLottery);
}
