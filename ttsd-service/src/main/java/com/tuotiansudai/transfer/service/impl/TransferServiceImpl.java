package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService{

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

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

    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findAllTransferApplicationPaginationList(TransferStatus status, double rateStart, double rateEnd, Integer index, Integer pageSize) {

        int count = transferApplicationMapper.findCountAllTransferApplicationPagination(status, rateStart, rateEnd);
        List<TransferApplicationRecordDto> items = Lists.newArrayList();
        if(count > 0){
            int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findAllTransferApplicationPaginationList(status, rateStart, rateEnd, (index - 1) * pageSize, pageSize);

        }
        List<TransferApplicationPaginationItemDataDto> itemDataDtoList = Lists.transform(items, new Function<TransferApplicationRecordDto, TransferApplicationPaginationItemDataDto>() {
            @Override
            public TransferApplicationPaginationItemDataDto apply(TransferApplicationRecordDto transferApplicationRecordDto) {
                TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto(transferApplicationRecordDto);
                return transferApplicationPaginationItemDataDto;
            }
        });

        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> dto = new BasePaginationDataDto(index, pageSize, count, itemDataDtoList);
        dto.setStatus(true);
        return dto;
    }

    @Override
    public int findCountAllTransferApplicationPaginationList(TransferStatus status, double rateStart, double tateEnd) {
        return transferApplicationMapper.findCountAllTransferApplicationPagination(status, rateStart, tateEnd);
    }

}
