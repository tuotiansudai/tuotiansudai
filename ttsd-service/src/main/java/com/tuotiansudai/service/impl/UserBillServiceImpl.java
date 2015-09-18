package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.UserBillService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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
    public List<UserBillDto> findUserBills(List<UserBillBusinessType> userBillBusinessType,int currentPage,Date startTime,Date endTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        DateTime dateTime = new DateTime(endTime);
        dateTime = dateTime.plusHours(23);
        dateTime = dateTime.plusMinutes(59);
        params.put("userBillBusinessType",userBillBusinessType);
        params.put("currentPage",(currentPage-1)*10);
        params.put("startTime",new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"));
        params.put("endTime",dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        List<UserBillDto> userBillDtos = Lists.newArrayList();
        List<UserBillModel> userBillModels = userBillMapper.findUserBills(params);
        for (UserBillModel userBillModel:userBillModels) {
            UserBillDto userBillDto = new UserBillDto();
            userBillDto.setCreatedTime(new DateTime(userBillModel.getCreatedTime()).toString("yyyy-MM-dd"));
            userBillDto.setBusinessType(userBillModel.getBusinessType());
            if (UserBillOperationType.TI_BALANCE.equals(userBillModel.getOperationType().name())) {
                userBillDto.setIncome(decimalFormat.format(userBillModel.getAmount() / 100D));
                userBillDto.setExpenditure("0.00");
            } else if (UserBillOperationType.TO_BALANCE.equals(userBillModel.getOperationType().name()) || UserBillOperationType.TO_FREEZE.equals(userBillModel.getOperationType().name())) {
                userBillDto.setIncome("0.00");
                userBillDto.setExpenditure(decimalFormat.format(userBillModel.getAmount() / 100D));
            } else {
                userBillDto.setIncome("0.00");
                userBillDto.setExpenditure("0.00");
            }
            userBillDto.setFreeze(decimalFormat.format(userBillModel.getFreeze() / 100D));
            userBillDto.setBalance(decimalFormat.format(userBillModel.getBalance() / 100D));
            userBillDto.setId(userBillModel.getId());
            userBillDtos.add(userBillDto);
        }
        return userBillDtos;
    }

    @Override
    public int findUserBillsCount(List<UserBillBusinessType> userBillBusinessType,Date startTime,Date endTime){
        Map<String, Object> params = new HashMap<String, Object>();
        DateTime dateTime = new DateTime(endTime);
        dateTime = dateTime.plusHours(23);
        dateTime = dateTime.plusMinutes(59);
        params.put("userBillBusinessType",userBillBusinessType);
        params.put("startTime",new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"));
        params.put("endTime",dateTime.toString("yyyy-MM-dd HH:mm:ss"));
        return userBillMapper.findUserBillsCount(params);
    }

}
