package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.repository.mapper.BankUserBillMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.BankUserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserBillPaginationView;
import com.tuotiansudai.util.CalculateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleUserBillService {

    static Logger logger = Logger.getLogger(ConsoleUserBillService.class);

    private final BankUserBillMapper bankUserBillMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    public ConsoleUserBillService(BankUserBillMapper bankUserBillMapper) {
        this.bankUserBillMapper = bankUserBillMapper;
    }

    public List<BankUserBillModel> findUserFunds(Role role, BankUserBillBusinessType businessType, BillOperationType operationType, String mobile, Date startTime, Date endTime, int index, int pageSize) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = CalculateUtil.calculateMaxDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }
        return bankUserBillMapper.findUserBills(null, mobile, businessType==null?null:Lists.newArrayList(businessType), operationType, formattedStartTime, formattedEndTime, (index - 1) * pageSize, pageSize, role);
    }

    public long findUserFundsCount(Role role, BankUserBillBusinessType businessType, BillOperationType operationType, String mobile, Date startTime, Date endTime) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = CalculateUtil.calculateMaxDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }

        return bankUserBillMapper.countBills(null, mobile, businessType==null?null:Lists.newArrayList(businessType), operationType, formattedStartTime, formattedEndTime, role);
    }

    public List<UserBillPaginationView> findUserFunds(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String mobile, Date startTime, Date endTime, int index, int pageSize) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = CalculateUtil.calculateMaxDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }

        return userBillMapper.findUserFunds(userBillBusinessType, userBillOperationType, mobile, formattedStartTime, formattedEndTime, (index - 1) * pageSize, pageSize);
    }

    public int findUserFundsCount(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String mobile, Date startTime, Date endTime) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = CalculateUtil.calculateMaxDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }
        return userBillMapper.findUserFundsCount(userBillBusinessType, userBillOperationType, mobile, formattedStartTime, formattedEndTime);
    }

}
