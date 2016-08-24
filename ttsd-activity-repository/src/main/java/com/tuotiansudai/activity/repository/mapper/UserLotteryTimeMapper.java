package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.UserLotteryTimeView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLotteryTimeMapper {

    List<UserLotteryTimeView> findUserLotteryTimeViews(@Param(value = "mobile") String mobile,
                                                       @Param(value = "index") Integer index,
                                                       @Param(value = "pageSize") Integer pageSize);

    int findUserLotteryTimeCountModels(@Param(value = "mobile") String mobile);
}
