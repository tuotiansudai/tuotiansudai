package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.UserBillBusinessType;
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

    List<UserBillModel> findByOrderIdAndBusinessType(@Param("orderId") Long orderId,
                                               @Param("userBillBusinessType") UserBillBusinessType userBillBusinessType);

    long findSumRewardByLoginName(String loginName);

    List<UserBillPaginationView> findUserFunds(@Param("userBillBusinessType") UserBillBusinessType userBillBusinessType,
                                               @Param("userBillOperationType") UserBillOperationType userBillOperationType,
                                               @Param("mobile") String mobile,
                                               @Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("startLimit") Integer startLimit,
                                               @Param("endLimit") Integer endLimit);

    int findUserFundsCount(@Param("userBillBusinessType") UserBillBusinessType userBillBusinessType,
                           @Param("userBillOperationType") UserBillOperationType userBillOperationType,
                           @Param("mobile") String mobile,
                           @Param("startTime") Date startTime,
                           @Param("endTime") Date endTime);

    long findUserSumInterest(@Param("endDate") Date endDate);
}
