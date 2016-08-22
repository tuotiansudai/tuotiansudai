package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.UserLotteryPrizeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserLotteryPrizeMapper {

    void create(UserLotteryPrizeModel userLotteryPrizeModel);

    void update(UserLotteryPrizeModel userLotteryPrizeModel);
}
