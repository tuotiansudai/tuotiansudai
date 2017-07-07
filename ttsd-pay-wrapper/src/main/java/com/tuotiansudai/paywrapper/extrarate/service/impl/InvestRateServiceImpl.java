package com.tuotiansudai.paywrapper.extrarate.service.impl;

import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.model.InvestExtraRateModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class InvestRateServiceImpl implements InvestRateService {


    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Override
    @Transactional
    public void updateExtraRateData(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception {
        long amount = actualInterest - actualFee;

        AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE,investExtraRateModel.getLoginName(), investExtraRateModel.getId(), amount, UserBillBusinessType.EXTRA_RATE, null, null);

//        amountTransfer.transferInBalance(;
        String detail = MessageFormat.format(SystemBillDetailTemplate.EXTRA_RATE_DETAIL_TEMPLATE.getTemplate(),
                investExtraRateModel.getLoginName(), String.valueOf(investExtraRateModel.getInvestId()));
        systemBillService.transferOut(investExtraRateModel.getId(), amount, SystemBillBusinessType.EXTRA_RATE, detail);
        this.updateInvestExtraRate(investExtraRateModel);
    }

    private void updateInvestExtraRate(InvestExtraRateModel investExtraRateModel){
        investExtraRateModel.setActualRepayDate(new Date());
        investExtraRateModel.setStatus(RepayStatus.COMPLETE);
        investExtraRateMapper.updateActualRepayDateStatus(investExtraRateModel);
    }

    public void updateInvestExtraRate(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee, long amount) {
        investExtraRateModel.setActualInterest(actualInterest);
        investExtraRateModel.setActualFee(actualFee);
        investExtraRateModel.setRepayAmount(amount);
        investExtraRateMapper.update(investExtraRateModel);
    }

}
