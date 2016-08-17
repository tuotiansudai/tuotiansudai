package com.tuotiansudai.console.service;


import com.tuotiansudai.repository.model.UserLotteryTimeModel;

import java.util.List;

public interface UserLotteryService {

    List<UserLotteryTimeModel> findUserLotteryTimeModels(String mobile,Integer index,Integer pageSize);

    int findUserLotteryTimeCountModels(String mobile);
}
