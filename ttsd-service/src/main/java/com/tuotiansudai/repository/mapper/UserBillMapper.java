package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserBillPaginationView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserBillMapper {

    void create(UserBillModel userBillModel);

    List<UserBillModel> findUserBills(Map<String, Object> params);

    int findUserBillsCount(Map<String, Object> params);

    List<UserBillModel> findByLoginName(String loginName);

    long findSumRewardByLoginName(String loginName);

    List<UserBillPaginationView> findUserFunds(@Param("userBillBusinessType") UserBillBusinessType userBillBusinessType,
                                               @Param("userBillOperationType") UserBillOperationType userBillOperationType,
                                               @Param("loginName") String loginName,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("startLimit") int startLimit,
                                               @Param("endLimit") int endLimit);

    int findUserFundsCount(@Param("userBillBusinessType") UserBillBusinessType userBillBusinessType,
                           @Param("userBillOperationType") UserBillOperationType userBillOperationType,
                           @Param("loginName") String loginName,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);
}
