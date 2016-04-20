package com.tuotiansudai.transfer.service.impl;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransferServiceImpl implements TransferService{

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseDto<PayDataDto> transferNoPasswordPurchase(InvestDto investDto) throws InvestException {
        investDto.setNoPassword(true);
        this.checkTransferPurchase(investDto);
        return payWrapperClient.noPasswordPurchase(investDto);
    }

    @Override
    public BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException{
        this.checkTransferPurchase(investDto);
        return payWrapperClient.purchase(investDto);
    }

    private void checkTransferPurchase(InvestDto investDto) throws InvestException {
        long loanId = Long.parseLong(investDto.getLoanId());
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new InvestException(InvestExceptionType.LOAN_NOT_FOUND);
        }
        long investAmount = AmountConverter.convertStringToCent(investDto.getAmount());

        AccountModel accountModel = accountMapper.findByLoginName(investDto.getLoginName());
        if (accountModel.getBalance() < investAmount) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        if (investDto.isNoPassword() && !accountModel.isNoPasswordInvest()) {
            throw new InvestException(InvestExceptionType.PASSWORD_INVEST_OFF);
        }

        if (LoanStatus.RAISING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

    }
}
