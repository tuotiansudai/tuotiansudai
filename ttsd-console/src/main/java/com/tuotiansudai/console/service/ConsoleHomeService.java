package com.tuotiansudai.console.service;

import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.enums.RechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConsoleHomeService {

    @Autowired
    UserMapperConsole userMapperConsole;

    @Autowired
    BankRechargeMapper bankRechargeMapper;

    @Autowired
    BankWithdrawMapper bankWithdrawMapper;

    @Autowired
    InvestMapper investMapper;

    public int userToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null, null, null, null);
    }

    public int user7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null, null, null, null);
    }

    public int user30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return userMapperConsole.findAllUserCount(null, null, null, startTime, null, null, null, null, null, null, null, null);
    }

    public long rechargeToday_Loaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return bankRechargeMapper.findSumRechargeAmount(Role.LOANER, null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    public long recharge7Days_Loaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return bankRechargeMapper.findSumRechargeAmount(Role.LOANER, null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    public long recharge30Days_Loaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return bankRechargeMapper.findSumRechargeAmount(Role.LOANER, null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    public long rechargeToday_NotLoaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    public long recharge7Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    public long recharge30Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return getRechargeNotLoaner(startTime);
    }

    private long getRechargeNotLoaner(Date startTime) {
        return bankRechargeMapper.findSumRechargeAmount(Role.INVESTOR, null, null, null, RechargeStatus.SUCCESS, null, startTime, null);
    }

    public long withdrawToday_Loaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return bankWithdrawMapper.sumWithdrawAmount(Role.LOANER, null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    public long withdraw7Days_Loaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return bankWithdrawMapper.sumWithdrawAmount(Role.LOANER, null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    public long withdraw30Days_Loaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return bankWithdrawMapper.sumWithdrawAmount(Role.LOANER, null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    public long withdrawToday_NotLoaner() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    public long withdraw7Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    public long withdraw30Days_NotLoaner() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return getWithdrawNotLoaner(startTime);
    }

    private long getWithdrawNotLoaner(Date startTime) {
        return bankWithdrawMapper.sumWithdrawAmount(Role.INVESTOR, null, null, WithdrawStatus.SUCCESS, null, startTime, null);
    }

    public long investToday() {
        Date startTime = DateTime.now().withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    public long invest7Days() {
        Date startTime = DateTime.now().minusDays(6).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    public long invest30Days() {
        Date startTime = DateTime.now().minusDays(29).withTimeAtStartOfDay().toDate();
        return investMapper.sumInvestAmount(null, null, null, null, null, startTime, new Date(), InvestStatus.SUCCESS, null);
    }

    public long getSumInvestAmount() {
        return investMapper.sumInvestAmount(null, null, null, null, null, null, null, InvestStatus.SUCCESS, null);
    }
}
