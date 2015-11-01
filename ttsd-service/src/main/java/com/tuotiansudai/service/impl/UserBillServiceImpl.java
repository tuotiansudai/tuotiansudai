package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.UserBillService;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/10.
 */
@Service
public class UserBillServiceImpl implements UserBillService {

    static Logger logger = Logger.getLogger(UserBillServiceImpl.class);

    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    public List<UserBillDto> findUserBills(List<UserBillBusinessType> userBillBusinessType,int currentPage,Date startTime,Date endTime,int pageSize) {
        Map<String, Object> params = new HashMap<String, Object>();
        DateTime dateTime = new DateTime(endTime);
        dateTime = dateTime.plusHours(23).plusMinutes(59);
        params.put("userBillBusinessType",userBillBusinessType);
        params.put("currentPage",(currentPage-1)*pageSize);
        params.put("startTime", startTime);
        params.put("endTime",dateTime);
        params.put("pageSize",pageSize);
        List<UserBillDto> userBillDtos = Lists.newArrayList();
        List<UserBillModel> userBillModels = userBillMapper.findUserBills(params);
        for (UserBillModel userBillModel:userBillModels) {
            UserBillDto userBillDto = new UserBillDto();
            userBillDto.setCreatedTime(userBillModel.getCreatedTime());
            userBillDto.setBusinessType(userBillModel.getBusinessType().getDescription());
            long income = 0;
            long cost = 0;
            if (UserBillOperationType.TI_BALANCE==userBillModel.getOperationType()) {
                income = userBillModel.getAmount();
            }
            if (UserBillOperationType.TO_BALANCE==userBillModel.getOperationType() || UserBillOperationType.TO_FREEZE==userBillModel.getOperationType()) {
                cost = userBillModel.getAmount();
            }
            userBillDto.setIncome(AmountUtil.convertCentToString(income));
            userBillDto.setCost(AmountUtil.convertCentToString(cost));
            userBillDto.setFreeze(AmountUtil.convertCentToString(userBillModel.getFreeze()));
            userBillDto.setBalance(AmountUtil.convertCentToString(userBillModel.getBalance()));
            userBillDto.setId(userBillModel.getId());
            userBillDtos.add(userBillDto);
        }
        return userBillDtos;
    }

    @Override
    public int findUserBillsCount(List<UserBillBusinessType> userBillBusinessType,Date startTime,Date endTime){
        Map<String, Object> params = new HashMap<String, Object>();
        DateTime dateTime = new DateTime(endTime);
        dateTime = dateTime.plusHours(23).plusMinutes(59);
        params.put("userBillBusinessType",userBillBusinessType);
        params.put("startTime", startTime);
        params.put("endTime",dateTime);
        return userBillMapper.findUserBillsCount(params);
    }

    @Override
    public List<UserBillModel> findUserFunds(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime,int currentPage,int pageSize) {
        return userBillMapper.findUserFunds(userBillBusinessType,userBillOperationType,loginName,startTime,endTime,(currentPage - 1) * pageSize,pageSize);
    }

    @Override
    public int findUserFundsCount(UserBillBusinessType userBillBusinessType,UserBillOperationType userBillOperationType,String loginName,Date startTime,Date endTime) {
        return userBillMapper.findUserFundsCount(userBillBusinessType,userBillOperationType,loginName,startTime,endTime);
    }

}
