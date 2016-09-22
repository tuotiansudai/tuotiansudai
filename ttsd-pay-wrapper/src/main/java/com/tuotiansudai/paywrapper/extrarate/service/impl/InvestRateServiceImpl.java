package com.tuotiansudai.paywrapper.extrarate.service.impl;

import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.paywrapper.extrarate.service.InvestRateService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.InvestExtraRateMapper;
import com.tuotiansudai.repository.model.InvestExtraRateModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.AmountTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class InvestRateServiceImpl implements InvestRateService {

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Override
    @Transactional
    public void updateExtraRateData(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee) throws Exception {
        long amount = actualInterest - actualFee;
        amountTransfer.transferInBalance(investExtraRateModel.getLoginName(), investExtraRateModel.getId(), amount, UserBillBusinessType.EXTRA_RATE, null, null);
        String detail = MessageFormat.format(SystemBillDetailTemplate.EXTRA_RATE_DETAIL_TEMPLATE.getTemplate(),
                investExtraRateModel.getLoginName(), String.valueOf(investExtraRateModel.getInvestId()));
        systemBillService.transferOut(investExtraRateModel.getId(), amount, SystemBillBusinessType.EXTRA_RATE, detail);
        updateInvestExtraRate(investExtraRateModel, actualInterest, actualFee, amount);
    }

    private void updateInvestExtraRate(InvestExtraRateModel investExtraRateModel, long actualInterest, long actualFee, long amount) {
        investExtraRateModel.setActualInterest(actualInterest);
        investExtraRateModel.setActualFee(actualFee);
        investExtraRateModel.setRepayAmount(amount);
        investExtraRateModel.setActualRepayDate(new Date());
        investExtraRateMapper.update(investExtraRateModel);
    }

}
