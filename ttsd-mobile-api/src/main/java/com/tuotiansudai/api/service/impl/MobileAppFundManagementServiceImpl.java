package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.FundManagementResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppFundManagementService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.InvestRepayService;
import com.tuotiansudai.service.RechargeService;
import com.tuotiansudai.service.WithdrawService;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppFundManagementServiceImpl implements MobileAppFundManagementService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private RechargeService rechargeService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private InvestRepayService investRepayService;


    public BaseResponseDto queryFundByUserId(String userId) {
        AccountModel accountModel = accountMapper.findByLoginName(userId);
        long accountBalance = 0L;
        long frozenMoney = 0L;
        if(accountModel != null){
            accountBalance = accountModel.getBalance();
            frozenMoney = accountModel.getFreeze();
        }
        long paidRechargeMoney = rechargeService.sumSuccessRechargeAmount(userId);
        long successWithdrawMoney = withdrawService.sumSuccessWithdrawAmount(userId);
        long receivableCorpus = investRepayService.findSumRepayingCorpusByLoginName(userId);
        long receivableInterest = investRepayService.findSumRepayingInterestByLoginName(userId);
        long receivedInterest = investRepayService.findSumRepaidInterestByLoginName(userId);
        long receivedCorpus = investRepayService.findSumRepaidCorpusByLoginName(userId);
        //资产总额＝账户余额 ＋ 冻结金额 ＋ 应收本金 ＋ 应收利息
        long totalAssets = accountBalance + frozenMoney + receivableCorpus + receivableInterest;
        //累计投资额 = 已收本金 ＋ 应收本金
        long totalInvestment = receivableCorpus + receivedCorpus;
        //预期总收益 = 已收利息 ＋ 应收利息
        long expectedTotalInterest = receivableInterest + receivedInterest;
        //待收本息 = 应收利息 ＋ 应收本金
        long receivableCorpusInterest = receivableInterest + receivableCorpus;

        FundManagementResponseDataDto fundManagementResponseDataDto = new FundManagementResponseDataDto();
        fundManagementResponseDataDto.setAccountBalance(AmountUtil.convertCentToString(accountBalance));
        fundManagementResponseDataDto.setFrozenMoney(AmountUtil.convertCentToString(frozenMoney));
        fundManagementResponseDataDto.setAvailableMoney(AmountUtil.convertCentToString(accountBalance));
        fundManagementResponseDataDto.setPaidRechargeMoney(AmountUtil.convertCentToString(paidRechargeMoney));
        fundManagementResponseDataDto.setSuccessWithdrawMoney(AmountUtil.convertCentToString(successWithdrawMoney));
        fundManagementResponseDataDto.setTotalAssets(AmountUtil.convertCentToString(totalAssets));
        fundManagementResponseDataDto.setTotalInvestment(AmountUtil.convertCentToString(totalInvestment));
        fundManagementResponseDataDto.setExpectedTotalInterest(AmountUtil.convertCentToString(expectedTotalInterest));
        fundManagementResponseDataDto.setReceivedInterest(AmountUtil.convertCentToString(receivedInterest));
        fundManagementResponseDataDto.setReceivedCorpus(AmountUtil.convertCentToString(receivedCorpus));
        fundManagementResponseDataDto.setReceivableInterest(AmountUtil.convertCentToString(receivableInterest));
        fundManagementResponseDataDto.setReceivableCorpus(AmountUtil.convertCentToString(receivableCorpus));
        fundManagementResponseDataDto.setReceivableCorpusInterest(AmountUtil.convertCentToString(receivableCorpusInterest));

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(fundManagementResponseDataDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }
}
