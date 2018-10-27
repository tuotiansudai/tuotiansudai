package com.tuotiansudai.transfer.service.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.service.TransferService;
import com.tuotiansudai.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private RiskEstimateMapper riskEstimateMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${risk.estimate.limit.key}")
    private String riskEstimateLimitKey;

    @Override
    public BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException {
        this.checkTransferPurchase(investDto);
        return payWrapperClient.purchase(investDto);
    }

    @Override
    public BaseDto<PayDataDto> noPasswordTransferPurchase(InvestDto investDto) throws InvestException {
        investDto.setNoPassword(true);
        this.checkTransferPurchase(investDto);
        return payWrapperClient.noPasswordPurchase(investDto);
    }

    private void checkTransferPurchase(InvestDto investDto) throws InvestException {
        AccountModel accountModel = accountMapper.findByLoginName(investDto.getLoginName());

        long loanId = Long.parseLong(investDto.getLoanId());
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(investDto.getTransferApplicationId()));
        if (transferApplicationModel.getLoginName().equals(investDto.getLoginName())) {
            throw new InvestException(InvestExceptionType.INVESTOR_IS_LOANER);
        }
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new InvestException(InvestExceptionType.LOAN_NOT_FOUND);
        }
        long transferAmount = transferApplicationModel.getTransferAmount();

        if (accountModel.getBalance() < transferAmount) {
            throw new InvestException(InvestExceptionType.NOT_ENOUGH_BALANCE);
        }

        if (investDto.isNoPassword() && !accountModel.isNoPasswordInvest()) {
            throw new InvestException(InvestExceptionType.PASSWORD_INVEST_OFF);
        }

        InvestModel transferInvestModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
        if (!transferInvestModel.isOverdueTransfer() && LoanStatus.REPAYING != loan.getStatus()) {
            throw new InvestException(InvestExceptionType.ILLEGAL_LOAN_STATUS);
        }

        //没有进行风险评估
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);
        RiskEstimateModel riskEstimateModel = riskEstimateMapper.findByLoginName(investDto.getLoginName());
        if (riskEstimateModel == null) {
            throw new InvestException(InvestExceptionType.RISK_ESTIMATE_UNUSABLE);
        }
        //项目风险等级不适合
        if (riskEstimateModel.getEstimate().getLower() < loanDetailsModel.getEstimate().getLower()) {
            throw new InvestException(InvestExceptionType.RISK_ESTIMATE_LEVEL_OVER);
        }
        //总投资金额超出风险投资限额
        long amount = investMapper.sumUsedFund(investDto.getLoginName());
        if ((amount + transferAmount) > AmountConverter.convertStringToCent(redisWrapperClient.hget(riskEstimateLimitKey, riskEstimateModel.getEstimate().name()))) {
            throw new InvestException(InvestExceptionType.RISK_ESTIMATE_AMOUNT_OVER);
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
    public TransferApplicationModel findLastTransfersByTransferInvestId(long transferInvestId) {
        List<TransferApplicationModel> list=transferApplicationMapper.findLastTransfersByTransferInvestId(transferInvestId);
        return (list == null || list.size() == 0)?null:list.get(0) ;
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
            if (input.isOverdueTransfer()){
                List<InvestRepayModel> overdueInvestRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(input.getInvestId()).stream().filter(model -> model.getStatus() == RepayStatus.OVERDUE).collect(Collectors.toList());
                transferableInvestPaginationItemDataView.setLeftPeriod(1);
                transferableInvestPaginationItemDataView.setLeftDays("0");
                transferableInvestPaginationItemDataView.setNextRepayDate(overdueInvestRepayModels.get(overdueInvestRepayModels.size() - 1).getRepayDate());
                transferableInvestPaginationItemDataView.setNextRepayAmount(AmountConverter.convertCentToString(overdueInvestRepayModels.stream().mapToLong(model-> model.getCorpus() + model.getExpectedInterest() + model.getDefaultInterest() + model.getOverdueInterest() - model.getExpectedFee() - model.getDefaultFee() - model.getOverdueFee()).sum()));
            }
            LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(input.getLoanId());
            if (!input.isOverdueTransfer() && loanRepayModel != null) {
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
        InvestModel transferInvestModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
        transferApplicationDetailDto.setExpecedInterest(AmountConverter.convertCentToString(transferInvestModel.isOverdueTransfer() ? 0 : InterestCalculator.calculateTransferInterest(transferApplicationModel, investRepayModels, investFeeRate)));
        if (transferApplicationModel.getStatus() == TransferStatus.TRANSFERRING) {
            AccountModel accountModel = accountMapper.findByLoginName(loginName);
            InvestModel investModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setBalance(accountModel != null ? AmountConverter.convertCentToString(accountModel.getBalance()) : "0.00");
        } else if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS) {
            InvestModel investModel = investMapper.findById(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setLoginName(randomUtils.encryptMobile(loginName, investModel.getLoginName(), investModel.getId()));
            transferApplicationDetailDto.setInvestId(transferApplicationModel.getInvestId());
            transferApplicationDetailDto.setTransferTime(transferApplicationModel.getTransferTime());
        }

        long nextExpectedFee = new BigDecimal(investRepayModel.getExpectedInterest()).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(investFeeRate)).longValue();
        long nextExpectedInterest = investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() - nextExpectedFee - investRepayModel.getDefaultInterest();
        if (transferApplicationModel.getPeriod() == loanModel.getPeriods()) {
            InvestRepayModel lastInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investId, transferApplicationModel.getPeriod());
            nextExpectedInterest += lastInvestRepayModel.getCorpus() + lastInvestRepayModel.getOverdueInterest() - lastInvestRepayModel.getOverdueFee();
        }
        transferApplicationDetailDto.setNextExpecedInterest(AmountConverter.convertCentToString(nextExpectedInterest));
        transferApplicationDetailDto.setActivityRate(loanModel.getActivityRate() * 100);
        return transferApplicationDetailDto;
    }
}
