package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;

import java.util.Date;
import java.util.List;

public interface UserBillService {

    BaseDto<BasePaginationDataDto> getUserBillData(int index,
                                                   int pageSize,
                                                   Date startTime,
                                                   Date endTime,
                                                   List<UserBillBusinessType> userBillBusinessType);


    long findSumRewardByLoginName(String loginName);

    List<UserBillModel> findUserFunds(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime,int currentPage,int pageSize);

    int findUserFundsCount(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime);
    
}
