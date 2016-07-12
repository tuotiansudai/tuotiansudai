package com.tuotiansudai.transfer.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.mapper.TransferRuleMapper;
import com.tuotiansudai.transfer.repository.model.*;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

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
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private TransferRuleMapper transferRuleMapper;


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
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(investDto.getTransferInvestId()));
        if (transferApplicationModel.getLoginName().equals(investDto.getLoginName())) {
            throw new InvestException(InvestExceptionType.APPLICATION_IS_HIS_OWN);
        }
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
        List<TransferApplicationRecordDto> items = transferApplicationMapper.findAllTransferApplicationPaginationList(transferStatus, rateStart, rateEnd, (index - 1) * pageSize, pageSize);

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
        List<InvestRepayModel> investRepayModels = transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getInvestId()) : investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getTransferInvestId());
        if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS && transferApplicationModel.getInvestId() != null) {
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationRecodesDto.setTransferApplicationReceiver(randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId()));
            transferApplicationRecodesDto.setReceiveAmount(AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()));
            transferApplicationRecodesDto.setSource(investModel.getSource());
            transferApplicationRecodesDto.setExpecedInterest(AmountConverter.convertCentToString(InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels)));
            transferApplicationRecodesDto.setInvestAmount(AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()));
            transferApplicationRecodesDto.setTransferTime(transferApplicationModel.getTransferTime());
            transferApplicationRecodesDto.setStatus(true);
        }
        else{
            transferApplicationRecodesDto.setStatus(false);
        }
        return transferApplicationRecodesDto;
    }
    @Override
    public List<TransferApplicationModel> getTransferApplicaationByTransferInvestId(long transferApplicationId){
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        return  transferApplicationMapper.findByTransferInvestId(transferApplicationModel.getTransferInvestId(), Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING));
    }

    @Override
    public BasePaginationDataDto<TransferableInvestPaginationItemDataDto> generateTransferableInvest(String loginName, Integer index, Integer pageSize) {
        long count = investMapper.findWebCountTransferableApplicationPaginationByLoginName(loginName);
        List<TransferableInvestView> items = Lists.newArrayList();
        items = investMapper.findWebTransferableApplicationPaginationByLoginName(loginName, (index-1) * pageSize, pageSize);
        if(count > 0){
            int totalPages = (int) ((count % pageSize > 0 || count == 0)? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findWebTransferableApplicationPaginationByLoginName(loginName, (index-1) * pageSize, pageSize);

        }
        List<TransferableInvestPaginationItemDataDto> records = Lists.transform(items, new Function<TransferableInvestView, TransferableInvestPaginationItemDataDto>() {
            @Override
            public TransferableInvestPaginationItemDataDto apply(TransferableInvestView input) {
                TransferableInvestPaginationItemDataDto transferableInvestPaginationItemDataDto = new TransferableInvestPaginationItemDataDto(input);
                transferableInvestPaginationItemDataDto.setTransferStatus(input.getTransferStatus().getDescription());
                transferableInvestPaginationItemDataDto.setLastRepayDate(loanRepayMapper.findLastRepayDateByLoanId(input.getLoanId()));
                LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(input.getLoanId());
                if (loanRepayModel != null) {
                    int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(input.getInvestId(), loanRepayModel.getPeriod());
                    transferableInvestPaginationItemDataDto.setLeftPeriod(leftPeriod);
                }
                return transferableInvestPaginationItemDataDto;
            }
        });
        UnmodifiableIterator<TransferableInvestPaginationItemDataDto> filter = Iterators.filter(records.iterator(), new Predicate<TransferableInvestPaginationItemDataDto>() {
            @Override
            public boolean apply(TransferableInvestPaginationItemDataDto input) {
                TransferRuleModel transferRuleModel = transferRuleMapper.find();
                return transferRuleModel.isMultipleTransferEnabled() || (!transferRuleModel.isMultipleTransferEnabled() && transferApplicationMapper.findByInvestId(input.getInvestId()) == null) ;
            }
        });
        BasePaginationDataDto<TransferableInvestPaginationItemDataDto> baseDto = new BasePaginationDataDto(index,pageSize,count,Lists.newArrayList(filter));
        baseDto.setStatus(true);

        return baseDto;
    }


    private TransferApplicationDetailDto convertModelToDto(TransferApplicationModel transferApplicationModel, String loginNme, int showLoginNameLength) {
        TransferApplicationDetailDto transferApplicationDetailDto = new TransferApplicationDetailDto();
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());

        InvestRepayModel investRepayModel = transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getInvestId(), transferApplicationModel.getPeriod()):investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), transferApplicationModel.getPeriod());
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
        transferApplicationDetailDto.setNextExpecedInterest(AmountConverter.convertCentToString(investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - investRepayModel.getExpectedFee()));
        transferApplicationDetailDto.setLoanType(loanModel.getType().getRepayType());
        transferApplicationDetailDto.setDeadLine(transferApplicationModel.getDeadline());
        transferApplicationDetailDto.setTransferStatus(transferApplicationModel.getStatus());
        transferApplicationDetailDto.setTransferrer(randomUtils.encryptMobile(loginNme, transferApplicationModel.getLoginName(), transferApplicationModel.getTransferInvestId()));
        List<InvestRepayModel> investRepayModels = transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getInvestId()) : investRepayMapper.findByInvestIdAndPeriodAsc(transferApplicationModel.getTransferInvestId());
        if (transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            AccountModel accountModel = accountMapper.findByLoginName(loginNme);
            InvestModel investModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginNme, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setBalance(accountModel != null ? AmountConverter.convertCentToString(accountModel.getBalance()) : "0.00");
            transferApplicationDetailDto.setExpecedInterest(AmountConverter.convertCentToString(InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels)));
        }
        else if(transferApplicationModel.getStatus() == TransferStatus.SUCCESS){
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginNme, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setInvestId(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setTransferTime(transferApplicationModel.getTransferTime());
        }
        return transferApplicationDetailDto;
    }

}
