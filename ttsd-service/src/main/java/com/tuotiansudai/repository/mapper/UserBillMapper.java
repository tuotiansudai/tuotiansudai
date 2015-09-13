package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBillMapper {

    void create(UserBillModel userBillModel);

    UserBillModel findByLoginName(String loginName);

    List<UserBillModel> findUserBills(@Param(value = "userBillBusinessType") String userBillBusinessType,@Param(value = "currentPage") int currentPage,@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime);

    int findUserBillsCount(@Param(value = "userBillBusinessType") String userBillBusinessType,@Param(value = "startTime") String startTime,@Param(value = "endTime") String endTime);
}
