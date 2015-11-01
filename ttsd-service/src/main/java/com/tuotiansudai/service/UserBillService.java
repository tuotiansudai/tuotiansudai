package com.tuotiansudai.service;

import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
public interface UserBillService {

    List<UserBillDto> findUserBills(List<UserBillBusinessType> userBillBusinessType,int currentPage,Date startTime,Date endTime,int pageSize);

    int findUserBillsCount(List<UserBillBusinessType> userBillBusinessType,Date startTime,Date endTime);

    List<UserBillModel> findUserFunds(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime,int currentPage,int pageSize);

    int findUserFundsCount(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime);
}
