package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.transfer.repository.model.TransferApplicationRecordDto;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

    private final static String REDIS_KEY_TEMPLATE = "webmobile:{0}:{1}:showinvestorname";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private RandomUtils randomUtils;


    @Override
    public BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException{
        this.checkTransferPurchase(investDto);
        return payWrapperClient.purchase(investDto);
    }

    @Override
    public BaseDto<PayDataDto> noPasswordTransferPurchase(InvestDto investDto) throws InvestException{
        investDto.setNoPassword(true);
        this.checkTransferPurchase(investDto);
        return payWrapperClient.noPasswordPurchase(investDto);
    }

    private void checkTransferPurchase(InvestDto investDto) throws InvestException {
        long loanId = Long.parseLong(investDto.getLoanId());
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new InvestException(InvestExceptionType.LOAN_NOT_FOUND);
        }
        long investAmount = Long.parseLong(investDto.getAmount());

        AccountModel accountModel = accountMapper.findByLoginName(investDto.getLoginName());
        if (accountModel.getBalance() < investAmount) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        if (investDto.isNoPassword() && !accountModel.isNoPasswordInvest()) {
            throw new InvestException(InvestExceptionType.PASSWORD_INVEST_OFF);
        }

        if (LoanStatus.REPAYING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

    }

    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findAllTransferApplicationPaginationList(List<TransferStatus> transferStatus, double rateStart, double rateEnd, Integer index, Integer pageSize) {

        int count = transferApplicationMapper.findCountAllTransferApplicationPagination(transferStatus, rateStart, rateEnd);
        List<TransferApplicationRecordDto> items = Lists.newArrayList();
        if (count > 0) {
            int totalPages = count % pageSize > 0 ? count / pageSize + 1 : count / pageSize;
            index = index > totalPages ? totalPages : index;
            items = transferApplicationMapper.findAllTransferApplicationPaginationList(transferStatus, rateStart, rateEnd, (index - 1) * pageSize, pageSize);

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
    public int findCountAllTransferApplicationPaginationList(List<TransferStatus> transferStatus, double rateStart, double tateEnd) {
        return transferApplicationMapper.findCountAllTransferApplicationPagination(transferStatus, rateStart, tateEnd);
    }

    @Override
    public TransferApplicationDetailDto getTransferApplicationDetailDto(long transferApplicationId, String loginName, int showLoginNameLength) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        if (transferApplicationModel == null) {
            return null;
        }
        TransferApplicationDetailDto transferApplicationDetailDto = convertModelToDto(transferApplicationModel, loginName, showLoginNameLength);
        transferApplicationDetailDto.setStatus(true);
        return transferApplicationDetailDto;
    }

    @Override
    public TransferApplicationRecodesDto getTransferee(long TransferApplicationId, String loginName) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(TransferApplicationId);
        TransferApplicationRecodesDto transferApplicationRecodesDto = new TransferApplicationRecodesDto();
        if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS && transferApplicationModel.getInvestId() != null) {
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationRecodesDto.setTransferApplicationReceiver(randomUtils.encryptLoginName(loginName, investModel.getLoginName(),6,investModel.getId()));
            transferApplicationRecodesDto.setReceiveAmount(AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()));
            transferApplicationRecodesDto.setSource(investModel.getSource());
            transferApplicationRecodesDto.setExpecedInterest(AmountConverter.convertCentToString(calculateExpectedInterest(transferApplicationModel)));
            transferApplicationRecodesDto.setInvestAmount(AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
            transferApplicationRecodesDto.setTransferTime(transferApplicationModel.getTransferTime());
            transferApplicationRecodesDto.setStatus(true);
        }
        else{
            transferApplicationRecodesDto.setStatus(false);
        }
        return transferApplicationRecodesDto;
    }


    private TransferApplicationDetailDto convertModelToDto(TransferApplicationModel transferApplicationModel, String loginNme, int showLoginNameLength) {
        TransferApplicationDetailDto transferApplicationDetailDto = new TransferApplicationDetailDto();
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), transferApplicationModel.getPeriod());
        transferApplicationDetailDto.setId(transferApplicationModel.getId());
        transferApplicationDetailDto.setTransferInvestId(transferApplicationModel.getTransferInvestId());
        transferApplicationDetailDto.setName(transferApplicationModel.getName());
        transferApplicationDetailDto.setLoanName(loanModel.getName());
        transferApplicationDetailDto.setLoanId(loanModel.getId());
        transferApplicationDetailDto.setLoanType(loanModel.getType().getName());
        transferApplicationDetailDto.setProductType(loanModel.getProductType());
        transferApplicationDetailDto.setTransferAmount(AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()));
        transferApplicationDetailDto.setInvestAmount(AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
        transferApplicationDetailDto.setBaseRate(loanModel.getBaseRate() * 100);
        transferApplicationDetailDto.setLeftPeriod(transferApplicationModel.getLeftPeriod());
        transferApplicationDetailDto.setDueDate(investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(),loanModel.getPeriods()).getRepayDate());
        transferApplicationDetailDto.setNextRefundDate(investRepayModel.getRepayDate());
        transferApplicationDetailDto.setNextExpecedInterest(AmountConverter.convertCentToString(investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee()));
        transferApplicationDetailDto.setLoanType(loanModel.getType().getName());
        transferApplicationDetailDto.setDeadLine(transferApplicationModel.getDeadline());
        transferApplicationDetailDto.setTransferStatus(transferApplicationModel.getStatus());
        if (transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            AccountModel accountModel = accountMapper.findByLoginName(loginNme);
            InvestModel investModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptLoginName(loginNme, investModel.getLoginName(), showLoginNameLength, investModel.getId()));
            transferApplicationDetailDto.setBalance(accountModel != null ? AmountConverter.convertCentToString(accountModel.getBalance()) : "0.00");
            transferApplicationDetailDto.setExpecedInterest(AmountConverter.convertCentToString(calculateExpectedInterest(transferApplicationModel)));
        }
        else if(transferApplicationModel.getStatus() == TransferStatus.SUCCESS){
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptLoginName(loginNme, investModel.getLoginName(), showLoginNameLength, investModel.getId()));
            transferApplicationDetailDto.setInvestId(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setTransferTime(transferApplicationModel.getTransferTime());
        }
        return transferApplicationDetailDto;
    }

    private long calculateExpectedInterest(TransferApplicationModel transferApplicationModel) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getTransferInvestId());
        long totalExpectedInterestAmount = 0;
        for (int i = transferApplicationModel.getPeriod() - 1; i < investRepayModels.size(); i++) {
            totalExpectedInterestAmount += investRepayModels.get(i).getCorpus() + investRepayModels.get(i).getExpectedInterest() - investRepayModels.get(i).getExpectedFee();
        }
        return totalExpectedInterestAmount;
    }

}
