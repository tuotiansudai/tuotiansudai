package com.tuotiansudai.point.service;


import com.tuotiansudai.point.dto.UserPointPrizeDto;

import java.util.List;

public interface PointLotteryService {

    String pointLottery(String loginName);

    void imitateLottery();

    void getLotteryOnceChance(String loginName);

    List<UserPointPrizeDto> findAllDrawLottery();

    List<UserPointPrizeDto> findMyDrawLottery(String loginName);

}
