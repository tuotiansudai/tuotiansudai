package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.UserLotteryTimeModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLotteryTimeMapper {

    void create(UserLotteryTimeModel userLotteryTimeModel);

    void update(UserLotteryTimeModel userLotteryTimeModel);

}
