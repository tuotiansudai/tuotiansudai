package com.tuotiansudai.paywrapper.extrarate.service.impl;


import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.extrarate.service.ExtraRateService;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
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
    private IdGenerator idGenerator;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

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
                this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
            }
        }
    }

    private void sendExtraRateAmount(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) {
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
        if (amount <= 0) {
            return;
        }
        long orderId = idGenerator.generate();
        try {
            TransferRequestModel requestModel = TransferRequestModel.newRequest(String.valueOf(orderId), accountModel.getPayUserId(), String.valueOf(amount));
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            if (responseModel.isSuccess()) {
                this.updateExtraRateData(investExtraRateModel, investModel, accountModel, actualInterest, actualFee, amount, orderId);
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("extra rate is failed, investId={0} loginName={1} amount={3}",
                    String.valueOf(investModel.getId()),
                    investModel.getLoginName(),
                    String.valueOf(investModel.getAmount())), e);
            return;
        }
    }

    private void updateExtraRateData(InvestExtraRateModel investExtraRateModel, InvestModel investModel, AccountModel accountModel, long actualInterest, long actualFee, long amount, long orderId) throws AmountTransferException {
        amountTransfer.transferInBalance(accountModel.getLoginName(), orderId, amount, UserBillBusinessType.EXTRA_RATE, null, null);
        String detail = MessageFormat.format(SystemBillDetailTemplate.EXTRA_RATE_DETAIL_TEMPLATE.getTemplate(),
                investModel.getLoginName(), String.valueOf(investModel.getId()));
        systemBillService.transferOut(orderId, amount, SystemBillBusinessType.REFERRER_REWARD, detail);
        investExtraRateModel.setActualInterest(actualInterest);
        investExtraRateModel.setActualFee(actualFee);
        investExtraRateModel.setRepayAmount(amount);
        investExtraRateModel.setActualRepayDate(new Date());
        investExtraRateMapper.update(investExtraRateModel);
    }

    @Override
    public void advanceRepay(long loanRepayId) {
        LoanRepayModel currentLoanRepay = loanRepayMapper.findById(loanRepayId);
        long loanId = currentLoanRepay.getLoanId();
        List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanId);
        for (InvestExtraRateModel investExtraRateModel : investExtraRateModels) {
            InvestModel investModel = investMapper.findById(investExtraRateModel.getInvestId());
            MembershipModel membershipModel = userMembershipEvaluator.evaluate(investModel.getLoginName());
            long actualInterest = 0;
            long actualFee = new BigDecimal(actualInterest).multiply(new BigDecimal(membershipModel.getFee())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            this.sendExtraRateAmount(investExtraRateModel, actualInterest, actualFee);
        }
    }

}
