package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExtraRateServiceImpl implements ExtraRateService {

    static Logger logger = Logger.getLogger(ExtraRateServiceImpl.class);

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRateService investRateService;

    @Override
    public void transferPurchase(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
        InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(transferInvestModel.getId());
        if (investExtraRateModel != null) {
            investExtraRateModel.setTransfer(true);
            investExtraRateMapper.update(investExtraRateModel);
        }
    }

    @Override
    public void normalRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanId).getPeriod() == currentLoanRepay.getPeriod();
        if (isLastPeriod) {
            List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
            for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
                long actualInterest = investExtraRateModel.getExpectedInterest();
                long actualFee = investExtraRateModel.getExpectedFee();
                try {
                    this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Normal Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                            String.valueOf(loanRepayId),
                            String.valueOf(investExtraRateModel.getInvestId()),
                            investExtraRateModel.getLoginName(),
                            String.valueOf(investExtraRateModel.getAmount())), e);
                    continue;
                }
            }
        }
    }

    private void sendExtraRateAmount(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception {
        InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
        AccountModel accountModel = accountMapper.findByLoginName(investModel.getLoginName());
        if (accountModel == null) {
            logger.debug(MessageFormat.format("user has no account, investId={0} loginName={1} amount={3}",
                    String.valueOf(investModel.getId()),
                    investModel.getLoginName(),
                    String.valueOf(investModel.getAmount())));
            return;
        }
        long amount = actualInterest - actualFee;
        boolean isSuccess = false;
        if (amount > 0) {
            String orderId = investExtraRateModel.getInvestId() + "X" + System.currentTimeMillis();
            TransferRequestModel requestModel = TransferRequestModel.newRequest(orderId, accountModel.getPayUserId(), String.valueOf(amount));
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            isSuccess = responseModel.isSuccess();
        }
        if (isSuccess || amount == 0) {
            investRateService.updateExtraRateData(investExtraRateModel, actualInterest, actualFee);
        }
    }

    @Override
    public void advanceRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
        for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
            InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
            long actualInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, investExtraRateModel.getExtraRate(), investModel, new Date());
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            try {
                this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
            } catch (Exception e) {
                logger.error(MessageFormat.format("[Advance Repay {0}] extra rate is failed, investId={0} loginName={1} amount={3}",
                        String.valueOf(loanRepayId),
                        String.valueOf(investExtraRateModel.getInvestId()),
                        investExtraRateModel.getLoginName(),
                        String.valueOf(investExtraRateModel.getAmount())), e);
                continue;
            }
        }
    }

}
