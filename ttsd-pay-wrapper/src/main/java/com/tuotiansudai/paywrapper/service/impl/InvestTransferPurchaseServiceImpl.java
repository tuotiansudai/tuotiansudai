package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SmsFatalNotifyDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestTransferPurchaseService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class InvestTransferPurchaseServiceImpl implements InvestTransferPurchaseService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> purchase(InvestDto investDto) {
        //TODO: verify transfer

        String transferee = investDto.getLoginName();
        AccountModel transfereeAccount = accountMapper.findByLoginName(transferee);
        long transferInvestId = Long.parseLong(investDto.getTransferInvestId());

        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        dto.setData(payFormDataDto);

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByTransferInvestId(transferInvestId, TransferStatus.TRANSFERRING);
        if (transferApplicationModel == null || transferApplicationModel.getTransferAmount() > transfereeAccount.getBalance()) {
            return dto;
        }

        InvestModel transferInvestModel = investMapper.findById(transferInvestId);

        InvestModel investModel = new InvestModel(idGenerator.generate(),
                transferInvestModel.getLoanId(),
                transferInvestId,
                transferInvestModel.getAmount(),
                transferee,
                investDto.getSource(),
                null);

        investMapper.create(investModel);

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newInvestTransferRequest(
                    String.valueOf(investModel.getLoanId()),
                    String.valueOf(investModel.getId()),
                    transfereeAccount.getPayUserId(),
                    String.valueOf(transferApplicationModel.getTransferAmount()),
                    investDto.getSource());
            return payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
        } catch (PayException e) {
            logger.error(MessageFormat.format("{0} purchase transfer(transferApplicationId={1}) is failed", transferee, String.valueOf(transferApplicationModel.getId())), e);
        }

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postPurchase(long investId) throws AmountTransferException {
        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null || investModel.getStatus() != InvestStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("transfer is failed, invest(investId={0}) is null or status is not WAIT_PAY", String.valueOf(investId)));
            return;
        }

        InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
        if (transferInvestModel == null || transferInvestModel.getTransferStatus() != TransferStatus.TRANSFERRING) {
            logger.error(MessageFormat.format("transfer is failed, transfer invest(transferInvestId={0}) is null or transfer status is not TRANSFERRING",
                    String.valueOf(investModel.getTransferInvestId() != null ? investModel.getTransferInvestId() : null)));
            return;
        }

        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByTransferInvestId(investModel.getTransferInvestId(), TransferStatus.TRANSFERRING);
        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING) {
            logger.error(MessageFormat.format("transfer is failed, transfer application(investId={0}) is null or transfer status is not TRANSFERRING",
                    String.valueOf(transferApplicationModel != null ? transferApplicationModel.getId() : null)));
            return;
        }

        // update transferee invest status
        investMapper.updateStatus(investId, InvestStatus.SUCCESS);
        // generate transferee balance
        amountTransfer.transferOutBalance(investModel.getLoginName(), investId, transferApplicationModel.getTransferAmount(), UserBillBusinessType.INVEST_TRANSFER_IN, null, null);

        // update transferrer invest transfer status
        investMapper.updateTransferStatus(transferInvestModel.getId(), TransferStatus.SUCCESS);

        // update transfer application
        transferApplicationModel.setInvestId(investModel.getId());
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setTransferTime(investModel.getCreatedTime());
        transferApplicationMapper.update(transferApplicationModel);

        try {
            this.updateInvestRepay(transferApplicationModel);
        } catch (Exception e) {
            logger.error(MessageFormat.format("update invest repay failed when post purchase(transferApplicationId={0})", String.valueOf(transferApplicationModel.getId())), e);
            this.sendFatalNotify(MessageFormat.format("债权转让更新回款计划失败(transferApplicationId={0})", String.valueOf(transferApplicationModel.getId())));
        }

        this.transferPayback(transferInvestModel, transferApplicationModel);

        this.transferFee(transferInvestModel, transferApplicationModel);
    }

    private void transferFee(InvestModel transferInvestModel, TransferApplicationModel transferApplicationModel) {
        long transferApplicationId = transferApplicationModel.getId();
        // transfer fee
        long transferFee = transferApplicationModel.getTransferFee();

        try {
            ProjectTransferRequestModel feeRequestModel = ProjectTransferRequestModel.newTransferFeeRequest(String.valueOf(transferInvestModel.getLoanId()),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(transferApplicationId), String.valueOf(new Date().getTime())),
                    String.valueOf(transferFee));

            ProjectTransferResponseModel feeResponseModel = this.paySyncClient.send(ProjectTransferMapper.class, feeRequestModel, ProjectTransferResponseModel.class);
            if (feeResponseModel.isSuccess()) {
                systemBillService.transferIn(transferApplicationId, transferFee, SystemBillBusinessType.TRANSFER_FEE,
                        MessageFormat.format(SystemBillDetailTemplate.TRANSFER_FEE_DETAIL_TEMPLATE.getTemplate(), transferInvestModel.getLoginName(), String.valueOf(transferApplicationId), String.valueOf(transferFee)));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("transfer fee is failed (transferApplicationId={0})", String.valueOf(transferApplicationId)), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("债权转让收取服务费失败(transferApplicationId={0})", String.valueOf(transferApplicationId)));
        }
    }

    private void transferPayback(InvestModel transferInvestModel, TransferApplicationModel transferApplicationModel) {
        long transferApplicationId = transferApplicationModel.getId();
        // transfer fee
        long transferFee = transferApplicationModel.getTransferFee();

        // transferrer payback amount
        long paybackAmount = transferApplicationModel.getTransferAmount() - transferFee;

        try {
            ProjectTransferRequestModel paybackRequestModel = ProjectTransferRequestModel.newInvestTransferPaybackRequest(String.valueOf(transferInvestModel.getLoanId()),
                    MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(transferApplicationId), String.valueOf(new Date().getTime())),
                    accountMapper.findByLoginName(transferInvestModel.getLoginName()).getPayUserId(),
                    String.valueOf(paybackAmount));

            ProjectTransferResponseModel paybackResponseModel = this.paySyncClient.send(ProjectTransferMapper.class, paybackRequestModel, ProjectTransferResponseModel.class);
            if (paybackResponseModel.isSuccess()) {
                amountTransfer.transferInBalance(transferInvestModel.getLoginName(), transferApplicationId, transferApplicationModel.getTransferAmount(), UserBillBusinessType.INVEST_TRANSFER_OUT, null, null);
                amountTransfer.transferOutBalance(transferInvestModel.getLoginName(), transferApplicationId, transferFee, UserBillBusinessType.TRANSFER_FEE, null, null);
            }
        } catch (PayException | AmountTransferException e) {
            logger.error(MessageFormat.format("transfer payback is failed (transferApplicationId={0})", String.valueOf(transferApplicationId)), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("债权转让返款失败(transferApplicationId={0})", String.valueOf(transferApplicationId)));
        }
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }

    private void updateInvestRepay(TransferApplicationModel transferApplicationModel) {
        long transferInvestId = transferApplicationModel.getTransferInvestId();
        long investId = transferApplicationModel.getInvestId();
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());

        final int transferBeginWithPeriod = transferApplicationModel.getPeriod();
        boolean isTransferInterest = transferApplicationModel.isTransferInterest();

        List<InvestRepayModel> transferrerTransferredInvestRepayModels = Lists.newArrayList(Iterables.filter(investRepayMapper.findByInvestIdAndPeriodAsc(transferInvestId), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getPeriod() >= transferBeginWithPeriod;
            }
        }));

        List<InvestRepayModel> transfereeInvestRepayModels = Lists.newArrayList();
        for (InvestRepayModel transferrerTransferredInvestRepayModel : transferrerTransferredInvestRepayModels) {
            InvestRepayModel transfereeInvestRepayModel = new InvestRepayModel(idGenerator.generate(),
                    investId,
                    transferrerTransferredInvestRepayModel.getPeriod(),
                    transferrerTransferredInvestRepayModel.getCorpus(),
                    transferrerTransferredInvestRepayModel.getExpectedInterest(),
                    transferrerTransferredInvestRepayModel.getExpectedFee(),
                    transferrerTransferredInvestRepayModel.getRepayDate(),
                    transferrerTransferredInvestRepayModel.getStatus());

            transferrerTransferredInvestRepayModel.setExpectedInterest(0);
            transferrerTransferredInvestRepayModel.setExpectedFee(0);
            transferrerTransferredInvestRepayModel.setCorpus(0);
            transferrerTransferredInvestRepayModel.setTransferred(true);
            transferrerTransferredInvestRepayModel.setStatus(RepayStatus.COMPLETE);

            if (transferrerTransferredInvestRepayModel.getPeriod() == transferBeginWithPeriod && !isTransferInterest) {
                DateTime transferTime = new DateTime(transferApplicationModel.getTransferTime()).withTimeAtStartOfDay();
                DateTime firstDateOfCurrentPeriod = loanModel.getType().getInterestInitiateType() == InterestInitiateType.INTEREST_START_AT_INVEST ?
                        new DateTime(investMapper.findById(transferInvestId).getCreatedTime()).withTimeAtStartOfDay() :
                        new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay();
                InvestRepayModel lastInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(transferInvestId, transferBeginWithPeriod - 1);
                firstDateOfCurrentPeriod = lastInvestRepayModel != null ? new DateTime(lastInvestRepayModel.getRepayDate()).plusDays(1).withTimeAtStartOfDay() : firstDateOfCurrentPeriod;

                int currentPeriodPassedDays = Days.daysBetween(firstDateOfCurrentPeriod, transferTime).getDays();
                long transferrerInterest = InterestCalculator.calculateInterest(loanModel, currentPeriodPassedDays * transferApplicationModel.getInvestAmount());
                transferrerTransferredInvestRepayModel.setExpectedInterest(transferrerInterest);
                transferrerTransferredInvestRepayModel.setExpectedFee(new BigDecimal(transferrerInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue());

                int currentPeriodRemainingDays = Days.daysBetween(firstDateOfCurrentPeriod, new DateTime(transferrerTransferredInvestRepayModel.getRepayDate()).plusDays(1).withTimeAtStartOfDay()).getDays() - currentPeriodPassedDays;
                long transfereeInterest = InterestCalculator.calculateInterest(loanModel, currentPeriodRemainingDays * transferApplicationModel.getInvestAmount());
                transfereeInvestRepayModel.setExpectedInterest(transfereeInterest);
                transfereeInvestRepayModel.setExpectedFee(new BigDecimal(transfereeInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(loanModel.getInvestFeeRate())).longValue());
                transfereeInvestRepayModel.setStatus(RepayStatus.REPAYING);
            }
            investRepayMapper.update(transferrerTransferredInvestRepayModel);

            transfereeInvestRepayModels.add(transfereeInvestRepayModel);
        }

        investRepayMapper.create(transfereeInvestRepayModels);
    }
}
