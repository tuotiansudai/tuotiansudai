package com.tuotiansudai.service;

import org.springframework.stereotype.Service;

public interface ConsoleHomeService {

    int getRegisterUserToday();

    int getRegisterUser7Days();

    int getRegisterUser30Days();

    long getSumRechargeAmountToday();

    long getSumRechargeAmount7Days();

    long getSumRechargeAmount30Days();

    long getSumWithdrawAmountToday();

    long getSumWithdrawAmount7Days();

    long getSumWithdrawAmount30Days();

    long getSumInvestAmountToday();

    long getSumInvestAmount7Days();

    long getSumInvestAmount30Days();

    long getSumInvestAmount();

}
