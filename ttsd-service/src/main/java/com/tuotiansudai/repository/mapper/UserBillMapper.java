package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBillModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserBillMapper {

    void create(UserBillModel userBillModel);

    List<UserBillModel> findUserBills(Map<String, Object> params);

    int findUserBillsCount(Map<String, Object> params);
    List<UserBillModel> findByLoginName(String loginName);

    long findSumRewardByLoginName(String loginName);
}
