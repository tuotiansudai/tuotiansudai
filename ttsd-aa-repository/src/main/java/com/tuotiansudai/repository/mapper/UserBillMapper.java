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

    List<UserBillModel> findUserBills(@Param(value = "userBillBusinessType") List<UserBillBusinessType> userBillBusinessType,
                                      @Param(value = "loginName") String loginName,
                                      @Param(value = "startTime") Date startTime,
                                      @Param(value = "endTime") Date endTime,
                                      @Param(value = "indexPage") int indexPage,
                                      @Param(value = "pageSize") int pageSize);

   int findUserBillsCount(@Param(value = "userBillBusinessType") List<UserBillBusinessType> userBillBusinessType,
                          @Param(value = "loginName") String loginName,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    List<UserBillModel> findByLoginName(String loginName);

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
}
