package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.service.ConsoleHomeService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsoleHomeServiceImpl implements ConsoleHomeService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RechargeMapper rechargeMapper;

    @Autowired
    WithdrawMapper withdrawMapper;

    @Autowired
    InvestMapper investMapper;

    @Override
    public int userToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return userMapper.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public int user7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return userMapper.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public int user30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return userMapper.findAllUserCount(null, null, null, startTime, null, null, null, null, null);
    }

    @Override
    public long rechargeToday_Loaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long recharge7Days_Loaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long recharge30Days_Loaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long rechargeToday_NotLoaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    @Override
    public long recharge7Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    @Override
    public long recharge30Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    private long getRechargeNotLoaner(Date startTime) {
        long sumRecharge = rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, null, startTime, null);
        long sumRechargeLoaner = rechargeMapper.findSumRechargeAmount(null, null, null, RechargeStatus.SUCCESS, null, Role.LOANER, startTime, null);
        return sumRecharge - sumRechargeLoaner;
    }

    @Override
    public long withdrawToday_Loaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long withdraw7Days_Loaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long withdraw30Days_Loaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, Role.LOANER, startTime, null);
    }

    @Override
    public long withdrawToday_NotLoaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    @Override
    public long withdraw7Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    @Override
    public long withdraw30Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    private long getWithdrawNotLoaner(Date startTime) {
        long sumWithdraw = withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, null, startTime, null);
        long sumWithdrawLoaner = withdrawMapper.findSumWithdrawAmount(null, null, WithdrawStatus.SUCCESS, null, Role.LOANER, startTime, null);
        return sumWithdraw - sumWithdrawLoaner;
    }

    @Override
    public long investToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    @Override
    public long invest7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    @Override
    public long invest30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    @Override
    public long getSumInvestAmount() {
        return investMapper.sumInvestAmount(null, null, null, null, null, null, null, InvestStatus.SUCCESS, null);
    }
}
