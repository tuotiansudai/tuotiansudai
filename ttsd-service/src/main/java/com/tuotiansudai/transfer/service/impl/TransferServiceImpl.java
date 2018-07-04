package com.tuotiansudai.transfer.service.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransferServiceImpl implements TransferService {

    static Logger logger = Logger.getLogger(TransferServiceImpl.class);

    private BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    private LoanMapper loanMapper;

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

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Override
    public BankAsyncMessage transferPurchase(InvestDto investDto) throws InvestException {

        UserModel userModel = userMapper.findByLoginName(investDto.getLoginName());
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(investDto.getLoginName());
        LoanModel loanModel = loanMapper.findById(Long.parseLong(investDto.getLoanId()));
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(investDto.getTransferApplicationId()));
        investDto.setAmount(String.valueOf(transferApplicationModel.getTransferAmount()));

        InvestModel transferrerModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
        InvestModel investModel = generateInvestModel(investDto, transferApplicationModel, transferrerModel);

        return bankWrapperClient.loanCreditInvest(
                transferApplicationModel.getId(),
                investModel.getId(),
                investDto.getSource(),
                investDto.getLoginName(),
                userModel.getMobile(),
                bankAccountModel.getBankUserName(),
                bankAccountModel.getBankAccountNo(),
                transferApplicationModel.getInvestAmount(),
                transferApplicationModel.getTransferAmount(),
                transferApplicationModel.getTransferFee(),
                transferrerModel.getBankOrderNo(),
                transferrerModel.getBankOrderDate(),
                loanModel.getLoanTxNo());
    }

    private InvestModel generateInvestModel(InvestDto investDto, TransferApplicationModel transferApplicationModel, InvestModel transferrerModel) throws InvestException{
        this.checkTransferPurchase(investDto);
        double rate = membershipPrivilegePurchaseService.obtainServiceFee(investDto.getLoginName());
        InvestModel investModel = new InvestModel(IdGenerator.generate(),
                transferApplicationModel.getLoanId(),
                transferApplicationModel.getTransferInvestId(),
                investDto.getLoginName(), transferrerModel.getAmount(),
                rate, false, transferrerModel.getInvestTime(),
                investDto.getSource(),
                investDto.getChannel()
        );
        investMapper.create(investModel);
        return investModel;
    }

    private void checkTransferPurchase(InvestDto investDto) throws InvestException {
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(investDto.getLoginName());

        long loanId = Long.parseLong(investDto.getLoanId());
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(investDto.getTransferApplicationId()));

        if(transferApplicationModel == null || transferApplicationModel.getStatus() !=TransferStatus.TRANSFERRING){
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new InvestException(InvestExceptionType.LOAN_NOT_FOUND);
        }

        if (transferApplicationModel.getLoginName().equals(investDto.getLoginName()) || loan.getAgentLoginName().equals(investDto.getLoginName())) {
            throw new InvestException(InvestExceptionType.INVESTOR_IS_LOANER);
        }

        long investAmount = Long.parseLong(investDto.getAmount());

        if (bankAccountModel.getBalance() < investAmount) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        if (LoanStatus.REPAYING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

    }

    @Override
    public BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findAllTransferApplicationPaginationList(List<TransferStatus> transferStatus, double rateStart, double rateEnd, Integer index, Integer pageSize) {
        int count = transferApplicationMapper.findCountAllTransferApplicationPagination(transferStatus, rateStart, rateEnd);
        List<TransferApplicationRecordView> items = transferApplicationMapper.findAllTransferApplicationPaginationList(transferStatus, rateStart, rateEnd, (index - 1) * pageSize, pageSize);
        List<TransferApplicationPaginationItemDataDto> itemDataDtoList = Lists.transform(items, transferApplicationRecordView -> {
            TransferApplicationPaginationItemDataDto transferApplicationPaginationItemDataDto = new TransferApplicationPaginationItemDataDto(transferApplicationRecordView);
            LoanModel loanModel = loanMapper.findById(transferApplicationRecordView.getLoanId());
            InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationRecordView.getTransferInvestId(), loanModel.getPeriods());
            Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
            transferApplicationPaginationItemDataDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
            return transferApplicationPaginationItemDataDto;
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
        TransferApplicationDetailDto transferApplicationDetailDto = convertModelToDto(transferApplicationModel, loginName);
        transferApplicationDetailDto.setStatus(true);
        return transferApplicationDetailDto;
    }


    public List<TransferInvestRepayDataDto> getUserTransferInvestRepay(long investId) {
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);

        return investRepayModels.stream()
                .map(investRepayModel -> new TransferInvestRepayDataDto(
                        investRepayModel.getRepayDate(),
                        investRepayModel.getExpectedInterest() + investRepayModel.getCorpus(),
                        investRepayModel.getStatus()
                )).collect(Collectors.toList());
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
        } else {
            transferApplicationRecodesDto.setStatus(false);
        }
        return transferApplicationRecodesDto;
    }

    @Override
    public List<TransferApplicationModel> getTransferApplicaationByTransferInvestId(long transferApplicationId) {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferApplicationId);
        return transferApplicationMapper.findByTransferInvestId(transferApplicationModel.getTransferInvestId(), Lists.newArrayList(TransferStatus.SUCCESS, TransferStatus.TRANSFERRING));
    }

    @Override
    public BasePaginationDataDto<TransferableInvestPaginationItemDataView> generateTransferableInvest(String loginName, Integer index, Integer pageSize) {
        long count = investMapper.findWebCountTransferableApplicationPaginationByLoginName(loginName);
        List<TransferableInvestView> items = Lists.newArrayList();
        items = investMapper.findWebTransferableApplicationPaginationByLoginName(loginName, (index - 1) * pageSize, pageSize);
        if (count > 0) {
            int totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
            index = index > totalPages ? totalPages : index;
            items = investMapper.findWebTransferableApplicationPaginationByLoginName(loginName, (index - 1) * pageSize, pageSize);
        }
        List<TransferableInvestPaginationItemDataView> records = Lists.transform(items, input -> {
            TransferableInvestPaginationItemDataView transferableInvestPaginationItemDataView = new TransferableInvestPaginationItemDataView(input);
            transferableInvestPaginationItemDataView.setTransferStatus(input.getTransferStatus().getDescription());
            transferableInvestPaginationItemDataView.setLastRepayDate(loanRepayMapper.findLastRepayDateByLoanId(input.getLoanId()));
            LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(input.getLoanId());
            if (loanRepayModel != null) {
                int leftPeriod = investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(input.getInvestId(), loanRepayModel.getPeriod());
                transferableInvestPaginationItemDataView.setLeftPeriod(leftPeriod);
                LoanModel loanModel = loanMapper.findById(input.getLoanId());
                InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(input.getInvestId(), loanModel.getPeriods());
                Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
                transferableInvestPaginationItemDataView.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
            }
            return transferableInvestPaginationItemDataView;
        });
        UnmodifiableIterator<TransferableInvestPaginationItemDataView> filter = Iterators.filter(records.iterator(), input -> {
            TransferRuleModel transferRuleModel = transferRuleMapper.find();
            return transferRuleModel.isMultipleTransferEnabled() || (!transferRuleModel.isMultipleTransferEnabled() && transferApplicationMapper.findByInvestId(input.getInvestId()) == null);
        });
        BasePaginationDataDto<TransferableInvestPaginationItemDataView> baseDto = new BasePaginationDataDto(index, pageSize, count, Lists.newArrayList(filter));
        baseDto.setStatus(true);

        return baseDto;
    }

    private TransferApplicationDetailDto convertModelToDto(TransferApplicationModel transferApplicationModel, String loginName) {
        TransferApplicationDetailDto transferApplicationDetailDto = new TransferApplicationDetailDto();
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        InvestRepayModel investRepayModel = transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getInvestId(), transferApplicationModel.getPeriod()) : investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), transferApplicationModel.getPeriod());
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
        InvestRepayModel currentInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), loanModel.getPeriods());
        Date repayDate = currentInvestRepayModel == null ? new Date() : currentInvestRepayModel.getRepayDate() == null ? new Date() : currentInvestRepayModel.getRepayDate();
        transferApplicationDetailDto.setLeftDays(CalculateLeftDays.calculateTransferApplicationLeftDays(repayDate));
        transferApplicationDetailDto.setDueDate(investRepayMapper.findByInvestIdAndPeriod(transferApplicationModel.getTransferInvestId(), loanModel.getPeriods()).getRepayDate());
        transferApplicationDetailDto.setNextRefundDate(investRepayModel.getRepayDate());
        transferApplicationDetailDto.setLoanType(loanModel.getType().getRepayType());
        transferApplicationDetailDto.setDeadLine(transferApplicationModel.getDeadline());
        String beforeDeadLine;
        long countdown = 0;
        Date now = new Date();
        if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS) {
            beforeDeadLine = "已转让";
        } else if (transferApplicationModel.getStatus() == TransferStatus.CANCEL) {
            beforeDeadLine = "已取消";
        } else {
            if (now.before(transferApplicationModel.getDeadline())) {
                long seconds = (transferApplicationModel.getDeadline().getTime() - now.getTime()) / 1000;
                countdown = seconds;
                int days = (int) (seconds / (60 * 60 * 24));
                int hours = (int) ((seconds % (60 * 60 * 24)) / (60 * 60));
                int minutes = (int) ((seconds % (60 * 60)) / 60);
                beforeDeadLine = MessageFormat.format("{0}天{1}小时{2}分", days, hours, minutes);
            } else {
                beforeDeadLine = "已过期";
            }
        }
        transferApplicationDetailDto.setCountdown(countdown);
        transferApplicationDetailDto.setBeforeDeadLine(beforeDeadLine);
        transferApplicationDetailDto.setTransferStatus(transferApplicationModel.getStatus());
        transferApplicationDetailDto.setTransferrer(randomUtils.encryptMobile(loginName, transferApplicationModel.getLoginName(), transferApplicationModel.getTransferInvestId()));
        long investId = transferApplicationModel.getStatus() == TransferStatus.SUCCESS ? transferApplicationModel.getInvestId() : transferApplicationModel.getTransferInvestId();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investId);
        transferApplicationDetailDto.setExpecedInterest(AmountConverter.convertCentToString(InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels, investFeeRate)));
        if (transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(loginName);
            InvestModel investModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setBalance(bankAccountModel != null ? AmountConverter.convertCentToString(bankAccountModel.getBalance()) : "0.00");
        } else if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS) {
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setInvestId(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setTransferTime(transferApplicationModel.getTransferTime());
        }

        long nextExpectedFee = new BigDecimal(investRepayModel.getExpectedInterest()).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investFeeRate)).longValue();
        long nextExpectedInterest = investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - nextExpectedFee;
        if (transferApplicationModel.getPeriod() == loanModel.getPeriods()) {
            nextExpectedInterest += investRepayMapper.findByInvestIdAndPeriod(investId, transferApplicationModel.getPeriod()).getCorpus();
        }
        transferApplicationDetailDto.setNextExpecedInterest(AmountConverter.convertCentToString(nextExpectedInterest));
        transferApplicationDetailDto.setActivityRate(loanModel.getActivityRate() * 100);
        return transferApplicationDetailDto;
    }
}
