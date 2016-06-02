package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.job.InvestTransferCallbackJob;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.InvestTransferNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.InvestNotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestTransferPurchaseService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InvestTransferPurchaseServiceImpl implements InvestTransferPurchaseService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    private final static String REPAY_ORDER_ID_SEPARATOR = "X";

    private final static String REPAY_ORDER_ID_TEMPLATE = "{0}" + REPAY_ORDER_ID_SEPARATOR + "{1}";

    @Autowired
    private IdGenerator idGenerator;

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
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private InvestTransferNotifyRequestMapper investTransferNotifyRequestMapper;

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.invest.notify.process.batch.size}")
    private int investProcessListSize;

    @Override
    @Transactional
    public BaseDto<PayDataDto> noPasswordPurchase(InvestDto investDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        String loginName = investDto.getLoginName();
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        long transferInvestId = Long.parseLong(investDto.getTransferInvestId());
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferInvestId);
        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING || transferApplicationModel.getTransferAmount() > accountModel.getBalance()) {
            return baseDto;
        }
        InvestModel transferrerModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
        InvestModel investModel = new InvestModel(idGenerator.generate(),
                transferApplicationModel.getLoanId(),
                transferApplicationModel.getTransferInvestId(),
                transferrerModel.getAmount(),
                loginName,
                transferrerModel.getInvestTime(),
                investDto.getSource(),
                investDto.getChannel());

        investMapper.create(investModel);

        ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newPurchaseNopwdRequest(String.valueOf(investModel.getLoanId()),
                String.valueOf(investModel.getId()),
                accountModel.getPayUserId(),
                String.valueOf(transferApplicationModel.getTransferAmount()));

        try {

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            investModel.setStatus(InvestStatus.FAIL);
            investMapper.update(investModel);
            payDataDto.setStatus(false);
            payDataDto.setMessage(e.getLocalizedMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

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
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferInvestId);
        if (transferApplicationModel == null || transferApplicationModel.getStatus() != TransferStatus.TRANSFERRING || transferApplicationModel.getTransferAmount() > transfereeAccount.getBalance()) {
            return dto;
        }
        InvestModel transferrerModel = investMapper.findById(transferApplicationModel.getTransferInvestId());
        InvestModel investModel = new InvestModel(idGenerator.generate(),
                transferApplicationModel.getLoanId(),
                transferApplicationModel.getTransferInvestId(),
                transferrerModel.getAmount(),
                transferee,
                transferrerModel.getInvestTime(), investDto.getSource(),
                investDto.getChannel());

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
    public String purchaseCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                InvestTransferNotifyRequestMapper.class,
                InvestNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        redisWrapperClient.incr(InvestTransferCallbackJob.INVEST_TRANSFER_JOB_TRIGGER_KEY);
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncPurchaseCallback() {
        List<InvestNotifyRequestModel> todoList = investTransferNotifyRequestMapper.getTodoList(investProcessListSize);

        for (InvestNotifyRequestModel model : todoList) {
            logger.info(MessageFormat.format("[Invest Transfer Callback {0}] starting...", model.getOrderId()));
            if (updateInvestTransferNotifyRequestStatus(model)) {
                try {
                    processOneCallback(model);
                } catch (Exception e) {
                    String errMsg = MessageFormat.format("invest callback, processOneCallback error. investId:{0}", model.getOrderId());
                    logger.error(errMsg, e);
                    sendFatalNotify(MessageFormat.format("债权转让投资回调处理错误。{0},{1}", environment, errMsg));
                }
            }
        }

        BaseDto<PayDataDto> asyncInvestNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncInvestNotifyDto.setData(baseDataDto);

        return asyncInvestNotifyDto;
    }

    /**
     * umpay 债权转让超投返款的回调
     *
     * @param paramsMap
     * @param queryString
     * @return
     */
    @Override
    public String overInvestTransferPaybackCallback(Map<String, String> paramsMap, String queryString) {

        logger.debug("into over_invest_transfer_payback_callback, queryString: " + queryString);

        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        String orderIdOri = callbackRequest.getOrderId();
        String orderIdStr = orderIdOri == null ? "" : orderIdOri.split("X")[0];
        long orderId = Long.parseLong(orderIdStr);

        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest callback notify order is not exist (orderId = {0})", orderId));
            return null;
        }

        String loginName = investModel.getLoginName();
        if (callbackRequest.isSuccess()) {
            // 返款成功
            // 改 invest 本身状态为超投返款
            investModel.setStatus(InvestStatus.OVER_INVEST_PAYBACK);
            investMapper.update(investModel);
        } else {
            // 返款失败，发报警短信，手动干预
            String errMsg = MessageFormat.format("invest transfer pay back notify fail. orderId:{0}, amount:{1}, loginName:{2}, loanId:{3}.", orderIdStr, investModel.getAmount(), loginName, investModel.getLoanId());
            logger.error(errMsg);
            sendFatalNotify(errMsg);
        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void postPurchase(long investId) throws AmountTransferException {
        InvestModel investModel = investMapper.findById(investId);
        InvestModel transferInvestModel = investMapper.findById(investModel.getTransferInvestId());
        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getTransferInvestId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
        TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);

        // update transferee invest status
        investModel.setStatus(InvestStatus.SUCCESS);
        investMapper.update(investModel);
        logger.info(MessageFormat.format("[Invest Transfer Callback {0}] update invest status to SUCCESS", String.valueOf(investId)));

        // generate transferee balance
        amountTransfer.transferOutBalance(investModel.getLoginName(), investId, transferApplicationModel.getTransferAmount(), UserBillBusinessType.INVEST_TRANSFER_IN, null, null);
        logger.info(MessageFormat.format("[Invest Transfer Callback {0}] update transferee balance and user bill", String.valueOf(investId)));

        // update transferrer invest transfer status
        investMapper.updateTransferStatus(transferInvestModel.getId(), TransferStatus.SUCCESS);
        logger.info(MessageFormat.format("[Invest Transfer Callback {0}] update transferrer invest transfer status to SUCCESS", String.valueOf(investId)));

        // update transfer application
        transferApplicationModel.setInvestId(investModel.getId());
        transferApplicationModel.setStatus(TransferStatus.SUCCESS);
        transferApplicationModel.setTransferTime(investModel.getCreatedTime());
        transferApplicationMapper.update(transferApplicationModel);
        logger.info(MessageFormat.format("[Invest Transfer Callback {0}] update transfer application", String.valueOf(investId)));

        try {
            this.updateInvestRepay(transferApplicationModel);
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] update invest repay failed", String.valueOf(investModel.getId())), e);
            this.sendFatalNotify(MessageFormat.format("债权转让({0})更新回款计划失败", String.valueOf(investId)));
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
                logger.error(MessageFormat.format("[Invest Transfer Callback {0}] transfer fee is success", String.valueOf(transferApplicationModel.getInvestId())));
            }
        } catch (PayException e) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] transfer fee is failed", String.valueOf(transferApplicationModel.getInvestId())), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("债权转让({0})服务费收取失败", String.valueOf(transferApplicationModel.getInvestId())));
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
                logger.error(MessageFormat.format("[Invest Transfer Callback {0}] transfer payback transferrer is success", String.valueOf(transferApplicationModel.getInvestId())));
            }
        } catch (PayException | AmountTransferException e) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] transfer payback transferrer is failed", String.valueOf(transferApplicationModel.getInvestId())), e);
            //sms notify
            this.sendFatalNotify(MessageFormat.format("债权转让(0)返款转让人失败", String.valueOf(transferApplicationModel.getInvestId())));
        }
    }

    private void updateInvestRepay(TransferApplicationModel transferApplicationModel) {
        long transferInvestId = transferApplicationModel.getTransferInvestId();
        long investId = transferApplicationModel.getInvestId();

        final int transferBeginWithPeriod = transferApplicationModel.getPeriod();

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

            investRepayMapper.update(transferrerTransferredInvestRepayModel);

            transfereeInvestRepayModels.add(transfereeInvestRepayModel);
        }

        investRepayMapper.create(transfereeInvestRepayModels);
    }

    private boolean updateInvestTransferNotifyRequestStatus(InvestNotifyRequestModel model) {
        try {
            redisWrapperClient.decr(InvestTransferCallbackJob.INVEST_TRANSFER_JOB_TRIGGER_KEY);
            investTransferNotifyRequestMapper.updateStatus(model.getId(), InvestNotifyProcessStatus.DONE);
            logger.info(MessageFormat.format("[Invest Transfer Callback {0}] decrease request count and update request status to DONE", model.getOrderId()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] update request status is failed", model.getOrderId()), e);
            this.sendFatalNotify(MessageFormat.format("债权转让投资({0})回调状态更新错误", model.getOrderId()));
            return false;
        }
        return true;
    }

    private void processOneCallback(InvestNotifyRequestModel callbackRequestModel) throws AmountTransferException {
        long investId = Long.parseLong(callbackRequestModel.getOrderId());
        InvestModel investModel = investMapper.findById(investId);
        if (investModel == null) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer callback order is not exist", callbackRequestModel.getOrderId()));
            return;
        }
        if (investModel.getStatus() != InvestStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer status({1}) is not WAIT_PAY", callbackRequestModel.getOrderId(), investModel.getStatus()));
            return;
        }

        if (callbackRequestModel.isSuccess()) {
            List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getTransferInvestId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
            if (CollectionUtils.isEmpty(transferApplicationModels)) {
                logger.error(MessageFormat.format("[Invest Transfer Callback {0}] transfer application is not exist", String.valueOf(investId)));
                return;
            }

            TransferApplicationModel transferApplicationModel = transferApplicationModels.get(0);

            if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS) {
                logger.info(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer is over invest", String.valueOf(investId)));
                this.overInvestPaybackProcess(transferApplicationModel, investModel);
            } else {
                logger.info(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer is success", String.valueOf(investId)));
                ((InvestTransferPurchaseService) AopContext.currentProxy()).postPurchase(investId);
            }
        } else {
            // 失败的话：更新 invest 状态为投资失败
            logger.info(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer callback is failed", String.valueOf(investId)));
            investModel.setStatus(InvestStatus.FAIL);
            investMapper.update(investModel);
        }
    }


    /**
     * 超投处理：返款、更新投资状态为失败
     *
     * @param investModel
     */
    private void overInvestPaybackProcess(TransferApplicationModel transferApplicationModel, InvestModel investModel) {
        long transferAmount = transferApplicationModel.getTransferAmount();
        long investId = investModel.getId();

        try {
            String overInvestPaybackOrderId = MessageFormat.format(REPAY_ORDER_ID_TEMPLATE, String.valueOf(investId), String.valueOf(System.currentTimeMillis()));
            AccountModel accountModel = accountMapper.findByLoginName(investModel.getLoginName());
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.overInvestPaybackRequest(String.valueOf(investModel.getLoanId()),
                    overInvestPaybackOrderId,
                    accountModel.getPayUserId(),
                    String.valueOf(transferAmount));
            ProjectTransferResponseModel responseModel = paySyncClient.send(ProjectTransferMapper.class,
                    requestModel,
                    ProjectTransferResponseModel.class);

            if (responseModel.isSuccess()) {
                // 超投返款成功
                logger.info(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer over invest payback({1}) is success", String.valueOf(investId), String.valueOf(transferAmount)));
                return;
            }
        } catch (Exception e) {
            // 所有其他异常，包括数据库链接，网络异常，记录日志，发短信通知管理员，抛出异常，事务回滚。
            logger.error(e.getLocalizedMessage() ,e);
        }

        // 联动优势返回返款失败，但是标记此条请求已经处理完成，记录日志，在异步notify中进行投资成功处理
        logger.error(MessageFormat.format("[Invest Transfer Callback {0}] invest transfer over invest payback({1}) is failed", String.valueOf(investId), String.valueOf(transferAmount)));
        // 如果返款失败，则记录本次投资为 超投返款失败
        investModel.setStatus(InvestStatus.OVER_INVEST_PAYBACK_FAIL);
        investMapper.update(investModel);
        sendFatalNotify(MessageFormat.format("债权转让购买({0})超投返款失败", String.valueOf(investId)));
    }

    private void sendFatalNotify(String message) {
        SmsFatalNotifyDto fatalNotifyDto = new SmsFatalNotifyDto(message);
        smsWrapperClient.sendFatalNotify(fatalNotifyDto);
    }
}
