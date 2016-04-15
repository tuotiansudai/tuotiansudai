package com.tuotiansudai.console.service.impl;

import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.service.ConsoleHomeService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.WithdrawStatus;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsoleHomeServiceImpl implements ConsoleHomeService {
    @Autowired
    UserMapperConsole userMapperConsole;

    @Autowired
    RechargeMapper rechargeMapper;

    @Autowired
    WithdrawMapper withdrawMapper;

    @Autowired
    InvestMapper investMapper;

    @Override
    public int getRegisterUserToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public int getRegisterUser7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public int getRegisterUser30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public long getSumRechargeAmountToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumRechargeAmount7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumRechargeAmount30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumWithdrawAmountToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumWithdrawAmount7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumWithdrawAmount30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    @Override
    public long getSumInvestAmountToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null,null,null,null,null,startTime,new Date(), InvestStatus.SUCCESS,null);
    }

    @Override
    public long getSumInvestAmount7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null,null,null,null,null,startTime,new Date(), InvestStatus.SUCCESS,null);
    }

    @Override
    public long getSumInvestAmount30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null,null,null,null,null,startTime,new Date(), InvestStatus.SUCCESS,null);
    }

    @Override
    public long getSumInvestAmount() {
        return investMapper.sumInvestAmount(null,null,null,null,null,null,null, InvestStatus.SUCCESS,null);
    }
}
