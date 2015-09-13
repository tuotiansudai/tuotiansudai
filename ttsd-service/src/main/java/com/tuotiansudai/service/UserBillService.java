package com.tuotiansudai.service;

import com.tuotiansudai.dto.UserBillDto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
public interface UserBillService {

    List<UserBillDto> findUserBills(String userBillBusinessType,int currentPage,String startTime,String endTime);

    int findUserBillsCount(String userBillBusinessType,String startTime,String endTime);
}
