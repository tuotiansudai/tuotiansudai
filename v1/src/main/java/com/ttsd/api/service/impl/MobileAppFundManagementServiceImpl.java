package com.ttsd.api.service.impl;

import com.esoft.archer.user.service.UserBillService;
import com.esoft.jdp2p.statistics.controller.BillStatistics;
import com.esoft.jdp2p.statistics.controller.InvestStatistics;
import com.esoft.jdp2p.user.service.impl.RechargeStatistics;
import com.esoft.jdp2p.user.service.impl.WithdrawStatistics;
import com.ttsd.api.dto.BaseResponseDto;
import com.ttsd.api.dto.FundManagementResponseDataDto;
import com.ttsd.api.dto.ReturnMessage;
import com.ttsd.api.service.MobileAppFundManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class MobileAppFundManagementServiceImpl implements MobileAppFundManagementService {
    @Autowired
    private UserBillService userBillService;
    @Autowired
    private RechargeStatistics rechargeStatistics;
    @Autowired
    private WithdrawStatistics withdrawStatistics;
    @Autowired
    private InvestStatistics investStatistics;
    @Autowired
    private BillStatistics billStatistics;

    @Override
    public BaseResponseDto queryFundByUserId(String userId) {
        double receivedCorpus = investStatistics.getReceivedCorpus(userId);
        double receivedInterest = investStatistics.getReceivedInterest(userId);
        double receivableCorpus = investStatistics.getReceivableCorpus(userId);
        double receivableInterest = investStatistics.getReceivableInterest(userId);
        double accountBalance = billStatistics.getBalanceByUserId(userId);
        double availableMoney = billStatistics.getBalanceByUserId(userId);
        double frozenMoney = userBillService.getFrozenMoney(userId);
        double paidRechargeMoney = rechargeStatistics.getPaidRechargeMoney(userId);
        double successWithdrawMoney = withdrawStatistics.getSuccessWithdrawMoney(userId);


        BigDecimal accountBalanceBig = new BigDecimal(accountBalance);
        BigDecimal frozenMoneyBig = new BigDecimal(frozenMoney);
        BigDecimal receivableCorpusBig = new BigDecimal(receivableCorpus);
        BigDecimal receivableInterestBig = new BigDecimal(receivableInterest);
        BigDecimal receivedCorpusBig = new BigDecimal(receivedCorpus);
        BigDecimal receivedInterestBig = new BigDecimal(receivedInterest);
        double totalAssets = accountBalanceBig.add(frozenMoneyBig)
                .add(receivableCorpusBig)
                .add(receivableInterestBig)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        double totalInvestment = receivedCorpusBig.add(receivableCorpusBig)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        double expectedTotalInterest = receivedInterestBig.add(receivableInterestBig)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();

        double receivableCorpusInterest = receivableCorpusBig.add(receivableInterestBig)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();


        FundManagementResponseDataDto fundManagementResponseDataDto = new FundManagementResponseDataDto();
        fundManagementResponseDataDto.setTotalAssets("" + totalAssets);
        fundManagementResponseDataDto.setTotalInvestment("" + totalInvestment);
        fundManagementResponseDataDto.setExpectedTotalInterest("" + expectedTotalInterest);
        fundManagementResponseDataDto.setReceivedCorpus("" + receivedCorpus);
        fundManagementResponseDataDto.setReceivedInterest("" + receivedInterest);
        fundManagementResponseDataDto.setReceivableCorpus("" + receivableCorpus);
        fundManagementResponseDataDto.setReceivableInterest("" + receivableInterest);
        fundManagementResponseDataDto.setReceivableCorpusInterest("" + receivableCorpusInterest);
        fundManagementResponseDataDto.setAccountBalance("" + accountBalance);
        fundManagementResponseDataDto.setAvailableMoney("" + availableMoney);
        fundManagementResponseDataDto.setFrozenMoney("" + frozenMoney);
        fundManagementResponseDataDto.setPaidRechargeMoney("" + paidRechargeMoney);
        fundManagementResponseDataDto.setSuccessWithdrawMoney("" + successWithdrawMoney);
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(fundManagementResponseDataDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());

        return baseResponseDto;
    }
}
