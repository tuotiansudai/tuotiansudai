package com.tuotiansudai.point.service;


public interface PointLotteryService {

    String pointLottery(String loginName);

    void getLotteryOnceChance(String loginName);

}
