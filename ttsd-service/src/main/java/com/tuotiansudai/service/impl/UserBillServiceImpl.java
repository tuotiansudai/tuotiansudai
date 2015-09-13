package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.UserBillService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/9/10.
 */
@Service
public class UserBillServiceImpl implements UserBillService {

    static Logger logger = Logger.getLogger(UserBillServiceImpl.class);

    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    public List<UserBillDto> findUserBills(String userBillBusinessType,int currentPage,String startTime,String endTime) {
        startTime += " 00:00:00";
        endTime += " 23:59:59";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        List<UserBillDto> userBillDtos = Lists.newArrayList();
        List<UserBillModel> userBillModels = userBillMapper.findUserBills(userBillBusinessType,currentPage,startTime,endTime);
        for (UserBillModel userBillModel:userBillModels) {
            UserBillDto userBillDto = new UserBillDto();
            userBillDto.setCreatedTime(simpleDateFormat.format(userBillModel.getCreatedTime()));
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
    public int findUserBillsCount(String userBillBusinessType,String startTime,String endTime){
        return userBillMapper.findUserBillsCount(userBillBusinessType,startTime,endTime);
    }

}
