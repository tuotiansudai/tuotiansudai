package com.ttsd.special.services;

public interface ActivityRewardService {
    boolean payActivityReward(String orderId,String userId,double reward,String detail);

    boolean transferMerToUser(String orderId, String accountId, double reward);
}
