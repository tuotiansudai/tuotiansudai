package com.tuotiansudai.console.service;

public interface ConsoleHomeService {

    int userToday();

    int user7Days();

    int user30Days();

    long rechargeToday_Loaner();

    long recharge7Days_Loaner();

    long recharge30Days_Loaner();

    long rechargeToday_NotLoaner();

    long recharge7Days_NotLoaner();

    long recharge30Days_NotLoaner();

    long withdrawToday_Loaner();

    long withdraw7Days_Loaner();

    long withdraw30Days_Loaner();

    long withdrawToday_NotLoaner();

    long withdraw7Days_NotLoaner();

    long withdraw30Days_NotLoaner();

    long investToday();

    long invest7Days();

    long invest30Days();

    long getSumInvestAmount();

}
