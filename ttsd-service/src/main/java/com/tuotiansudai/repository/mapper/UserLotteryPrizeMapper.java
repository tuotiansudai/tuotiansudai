package com.tuotiansudai.repository.mapper;


import org.springframework.stereotype.Repository;

@Repository
public interface UserLotteryPrizeMapper {

    void create(UserLotteryPrizeModel userLotteryPrizeModel);

    void update(UserLotteryPrizeModel userLotteryPrizeModel);
}
