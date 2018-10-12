package com.tuotiansudai.paywrapper.loanout.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.LoanService;
import com.tuotiansudai.paywrapper.repository.mapper.MerBindProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.MerUpdateProjectMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerBindProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.MerUpdateProjectRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerBindProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.MerUpdateProjectResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.UMPayRealTimeStatusService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.LoanPeriodCalculator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

@Service
public class LoanServiceImpl implements LoanService {

    private final static Logger logger = Logger.getLogger(LoanServiceImpl.class);

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR = "P";

    private final static String CANCEL_INVEST_PAY_BACK_ORDER_ID_TEMPLATE = "{0}" + CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR + "{1}";

    public final static String LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE = "LOAN_OUT_IDEMPOTENT_CHECK:{0}";

    private final static String DO_PAY_REQUEST = "DO_PAY_REQUEST";

    private final static String TRANSFER_OUT_FREEZE = "TRANSFER_OUT_FREEZE:INVEST:{0}";

    private final static String TRANSFER_IN_BALANCE = "TRANSFER_IN_BALANCE";

    private final static String LOAN_OUT_IN_PROCESS_KEY = "job:loan-out-in-process:";

    private final static String ALREADY_LOAN_OUT_RETURN_CODE = "0001";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private CouponInvestService couponInvestService;

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> createLoan(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        String payUserId = accountMapper.findByLoginName(loanModel.getAgentLoginName()).getPayUserId();
        MerBindProjectRequestModel merBindProjectRequestModel = new MerBindProjectRequestModel(
                payUserId,
                String.valueOf(loanModel.getLoanAmount()),
                String.valueOf(loanModel.getId()),
                String.valueOf(loanModel.getId()));

        MerUpdateProjectRequestModel merUpdateProjectPreheatRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()),
                LoanStatus.PREHEAT.getCode());

        MerUpdateProjectRequestModel merUpdateProjectRaisingRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()),
                LoanStatus.RAISING.getCode());
        try {
            MerBindProjectResponseModel createLoanResponseModel = paySyncClient.send(MerBindProjectMapper.class,
                    merBindProjectRequestModel,
                    MerBindProjectResponseModel.class);

            if (!createLoanResponseModel.isSuccess()) {
                payDataDto.setCode(createLoanResponseModel.getRetCode());
                payDataDto.setMessage(createLoanResponseModel.getRetMsg());
                return baseDto;
            }

            MerUpdateProjectResponseModel openLoanPhaseOneResponseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectPreheatRequestModel,
                    MerUpdateProjectResponseModel.class);

            if (!openLoanPhaseOneResponseModel.isSuccess()) {
                payDataDto.setCode(openLoanPhaseOneResponseModel.getRetCode());
                payDataDto.setMessage(openLoanPhaseOneResponseModel.getRetMsg());
                return baseDto;
            }

            MerUpdateProjectResponseModel openLoanPhaseTwoResponseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                    merUpdateProjectRaisingRequestModel,
                    MerUpdateProjectResponseModel.class);

            if (!openLoanPhaseTwoResponseModel.isSuccess()) {
                payDataDto.setCode(openLoanPhaseTwoResponseModel.getRetCode());
                payDataDto.setMessage(openLoanPhaseTwoResponseModel.getRetMsg());
                return baseDto;
            }

            payDataDto.setStatus(true);

        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseDto<PayDataDto> cancelLoan(long loanId) {
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            InvestDto investDto = new InvestDto();
            investDto.setLoanId(String.valueOf(loanId));
            investDto.setLoginName(investModel.getLoginName());
            investDto.setAmount(String.valueOf(investModel.getAmount()));
            try {
                if (this.cancelPayBack(investDto, investModel.getId()).isSuccess()) {
                    logger.info(investModel.getId() + " cancel payBack is success!");
                } else {
                    logger.info(investModel.getId() + " cancel payBack is fail!");
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        BaseDto<PayDataDto> baseDto = this.updateLoanStatus(loanId, LoanStatus.CANCEL);

        if (baseDto.getData() != null && baseDto.getData().getStatus()) {
            couponInvestService.cancelUserCoupon(loanId);
        }

        return baseDto;
    }

    @Transactional
    public BaseDto<PayDataDto> updateLoanStatus(long loanId, LoanStatus loanStatus) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel.getStatus() == loanStatus) {
            payDataDto.setStatus(true);
            return baseDto;
        }

        try {
            boolean updateSuccess = Strings.isNullOrEmpty(loanStatus.getCode());
            if (!updateSuccess) {
                MerUpdateProjectRequestModel merUpdateProjectRequestModel = new MerUpdateProjectRequestModel(String.valueOf(loanModel.getId()), loanStatus.getCode());

                MerUpdateProjectResponseModel responseModel = paySyncClient.send(MerUpdateProjectMapper.class,
                        merUpdateProjectRequestModel,
                        MerUpdateProjectResponseModel.class);
                updateSuccess = responseModel.isSuccess();
                payDataDto.setCode(responseModel.getRetCode());
                payDataDto.setMessage(responseModel.getRetMsg());
            }

            if (updateSuccess) {
                if (Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.CANCEL).contains(loanStatus)) {
                    loanModel.setRecheckTime(new Date());
                    loanModel.setPeriods(LoanPeriodCalculator.calculateLoanPeriods(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType()));
                }
                loanModel.setStatus(loanStatus);
                loanMapper.update(loanModel);
                payDataDto.setStatus(true);
            }
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return baseDto;
    }

    @Override
    public BaseDto<PayDataDto> loanOut(long loanId) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);

        if (redisWrapperClient.setnx(LOAN_OUT_IN_PROCESS_KEY + loanId, "1")) {
            try {
                ProjectTransferResponseModel umPayReturn = this.doLoanOut(loanId);
                payDataDto.setStatus(umPayReturn.isSuccess());
                payDataDto.setCode(umPayReturn.getRetCode());
                payDataDto.setMessage(umPayReturn.getRetMsg());
            } catch (PayException e) {
                payDataDto.setStatus(false);
                payDataDto.setMessage(e.getLocalizedMessage());
                logger.error(e.getLocalizedMessage(), e);
            } finally {
                redisWrapperClient.del(LOAN_OUT_IN_PROCESS_KEY + loanId);
            }

        } else {
            payDataDto.setStatus(true);
            payDataDto.setCode(BaseSyncResponseModel.SUCCESS_CODE);
            payDataDto.setMessage("some other loan out process is running.");
            logger.error("some other thread is loan-outing for this loan, loanId:" + loanId);
        }
        return baseDto;
    }

    private ProjectTransferResponseModel doLoanOut(long loanId) throws PayException {
        // 查找借款人
        LoanModel loan = loanMapper.findById(loanId);
        if (loan == null) {
            throw new PayException("loan is not exists [" + loanId + "]");
        }

        if (LoanStatus.REPAYING == loan.getStatus()) {
            logger.warn("loan has already been outed. [" + loanId + "]");
            ProjectTransferResponseModel umPayReturn = new ProjectTransferResponseModel();
            umPayReturn.setRetCode(ALREADY_LOAN_OUT_RETURN_CODE);
            umPayReturn.setRetMsg("放款失败：标的已经被自动放款。");
            return umPayReturn;
        }

        if (LoanStatus.RECHECK != loan.getStatus()) {
            throw new PayException(MessageFormat.format("loan{0} status{1} is not RECHECK, loan out is failed", String.valueOf(loanId), loan.getStatus().name()));
        }

        // 将已失效的出借记录状态置为失败
        investMapper.cleanWaitingInvest(loanId);

        // 查找所有出借成功的记录
        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        logger.info("标的放款：查找到" + successInvestList.size() + "条成功的出借，标的ID:" + loanId);

        // 计算出借总金额
        long investAmountTotal = computeInvestAmountTotal(successInvestList);
        if (investAmountTotal <= 0) {
            throw new PayException(MessageFormat.format("loan{0} out is failed, invest amount sum is less then 0", String.valueOf(loanId)));
        }

        BaseDto<PayDataDto> checkLoanAmount = umPayRealTimeStatusService.checkLoanAmount(loanId, investAmountTotal);

        if (!checkLoanAmount.getData().getStatus()) {
            throw new PayException(MessageFormat.format("标的(loanId={0})借款金额与出借金额不一致", String.valueOf(loanId)));
        }


        logger.info("[标的放款]：发起联动优势放款请求，标的ID:" + loanId + "，代理人:" + loan.getAgentLoginName() + "，放款金额:" + investAmountTotal);

        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        String beforeSendStatus = redisWrapperClient.hget(redisKey, DO_PAY_REQUEST);
        ProjectTransferResponseModel resp = new ProjectTransferResponseModel();
        if (Strings.isNullOrEmpty(beforeSendStatus) || beforeSendStatus.equals(SyncRequestStatus.FAILURE.name())) {
            try {
                redisWrapperClient.hset(redisKey, DO_PAY_REQUEST, SyncRequestStatus.SENT.name());
                ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanOutRequest(
                        String.valueOf(loanId), String.valueOf(loanId),
                        accountMapper.findByLoginName(loan.getAgentLoginName()).getPayUserId(),
                        String.valueOf(investAmountTotal));
                resp = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
                redisWrapperClient.hset(redisKey, DO_PAY_REQUEST, resp.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                return resp;
            } catch (PayException e) {
                logger.error(MessageFormat.format("[标的放款]:发起放款联动优势请求失败,标的ID : {0}", String.valueOf(loanId)), e);
                throw new PayException(MessageFormat.format("[标的放款]:发起放款联动优势请求失败,标的ID : {0}", String.valueOf(loanId)));
            }
        }

        resp.setRetMsg(MessageFormat.format("[标的放款]:发起放款联动优势请求重复,标的ID : {0}", String.valueOf(loanId)));
        logger.error(resp.getRetMsg());
        return resp;
    }

    private long computeInvestAmountTotal(List<InvestModel> investList) {
        long amount = 0L;
        for (InvestModel investModel : investList) {
            amount += investModel.getAmount();
        }
        return amount;
    }

    //将成功的出借人冻结金额转出
    private boolean processInvestFreezeAmountForLoanOut(List<InvestModel> investList, long loanId) {
        boolean result = true;
        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        for (InvestModel invest : investList) {
            String transferKey = MessageFormat.format(TRANSFER_OUT_FREEZE, String.valueOf(invest.getId()));
            try {
                String statusString = redisWrapperClient.hget(redisKey, transferKey);
                if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {
                    AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_FREEZE, invest.getLoginName(),
                            invest.getId(), invest.getAmount(), UserBillBusinessType.LOAN_SUCCESS, null, null);
                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                    redisWrapperClient.hset(redisKey, transferKey, SyncRequestStatus.SUCCESS.name());
                }
            } catch (Exception e) {
                result = false;
                redisWrapperClient.hset(redisKey, transferKey, SyncRequestStatus.FAILURE.name());
                logger.error(MessageFormat.format("[标的放款]: loanId({0}) transfer out freeze failed invest({1})", String.valueOf(loanId), String.valueOf(invest.getId())));
            }
        }
        return result;
    }

    // 把借款转给代理人账户
    private boolean processLoanAgentAccountForLoanOut(long loanId, String agentLoginName, long amount) {
        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        try {
            String statusString = redisWrapperClient.hget(redisKey, TRANSFER_IN_BALANCE);
            if (Strings.isNullOrEmpty(statusString) || statusString.equals(SyncRequestStatus.FAILURE.name())) {

                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, agentLoginName,
                        loanId, amount, UserBillBusinessType.LOAN_SUCCESS, null, null);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                redisWrapperClient.hset(redisKey, TRANSFER_IN_BALANCE, SyncRequestStatus.SUCCESS.name());
            }
        } catch (Exception e) {
            redisWrapperClient.hset(redisKey, TRANSFER_IN_BALANCE, SyncRequestStatus.FAILURE.name());
            logger.error(MessageFormat.format("[标的放款]: loanId({0}) transfer in agent account failed)", String.valueOf(loanId)));
            return false;
        }
        return true;
    }

    private BaseDto<PayDataDto> cancelPayBack(InvestDto dto, long investId) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());
        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newLoanCancelPayBackRequest(dto.getLoanId(),
                MessageFormat.format(CANCEL_INVEST_PAY_BACK_ORDER_ID_TEMPLATE, String.valueOf(investId), String.valueOf(System.currentTimeMillis())),
                accountModel.getPayUserId(), String.valueOf(dto.getAmount()));
        try {
            ProjectTransferResponseModel responseModel = paySyncClient.send(ProjectTransferMapper.class, requestModel, ProjectTransferResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            payDataDto.setStatus(false);
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String cancelPayBackCallback(Map<String, String> paramsMap, String queryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        String orderIdOri = callbackRequest.getOrderId();
        String orderIdStr = orderIdOri == null ? "" : orderIdOri.split(CANCEL_INVEST_PAY_BACK_ORDER_ID_SEPARATOR)[0];
        long orderId = Long.parseLong(orderIdStr);
        InvestModel investModel = investMapper.findById(orderId);
        if (investModel == null) {
            logger.error(MessageFormat.format("invest callback notify order is not exist (orderId = {0})", orderId));
            return null;
        }
        String loginName = investModel.getLoginName();
        if (callbackRequest.isSuccess()) {
            if (investMapper.findById(investModel.getId()).getStatus() != InvestStatus.CANCEL_INVEST_PAYBACK) {
                investModel.setStatus(InvestStatus.CANCEL_INVEST_PAYBACK);
                investMapper.update(investModel);
                AmountTransferMessage atm = new AmountTransferMessage(TransferType.UNFREEZE, loginName, orderId, investModel.getAmount(), UserBillBusinessType.CANCEL_INVEST_PAYBACK, null, null);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
            }
        }
        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String loanOutCallback(Map<String, String> paramsMap, String queryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);
        if (callbackRequest == null || Strings.isNullOrEmpty(callbackRequest.getOrderId())) {
            logger.error("[标的放款]: callback request parse failed or order id is empty");
            return null;
        }

        long loanId = Long.parseLong(callbackRequest.getOrderId());
        LoanModel loan = loanMapper.findById(loanId);

        if (loan == null) {
            logger.error(MessageFormat.format("[标的放款]: loan({0}) is not existed", callbackRequest.getOrderId()));
            return callbackRequest.getResponseData();
        }

        String redisKey = MessageFormat.format(LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        redisWrapperClient.hset(redisKey, DO_PAY_REQUEST, callbackRequest.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());

        if (!callbackRequest.isSuccess()) {
            return callbackRequest.getResponseData();
        }

        logger.info("[标的放款]: 放款回调,标的ID:" + loanId);

        List<InvestModel> successInvestList = investMapper.findSuccessInvestsByLoanId(loanId);
        long investAmountTotal = computeInvestAmountTotal(successInvestList);

        logger.debug("[标的放款]：更新标的状态，标的ID:" + loanId);
        BaseDto<PayDataDto> baseDto = this.updateLoanStatus(loanId, LoanStatus.REPAYING);
        if (!baseDto.getData().getStatus()) {
            this.fatalLog(loanId, "更新标的状态失败", null);
        }

        logger.debug("[标的放款]：处理该标的的所有出借的账务信息，标的ID:" + loanId);
        if (!this.processInvestFreezeAmountForLoanOut(successInvestList, loanId)) {
            this.fatalLog(loanId, "处理该标的的所有出借的账务信息失败", null);
        }

        logger.debug("[标的放款]：把借款转给代理人账户，标的ID:" + loanId);
        if (!this.processLoanAgentAccountForLoanOut(loanId, loan.getAgentLoginName(), investAmountTotal)) {
            this.fatalLog(loanId, "把借款转给代理人账户失败", null);
        }

        LoanOutSuccessMessage loanOutInfo = new LoanOutSuccessMessage(loanId);
        try {
            mqWrapperClient.publishMessage(MessageTopic.LoanOutSuccess, loanOutInfo);
            logger.info(MessageFormat.format("[标的放款]: 放款成功,发送MQ消息,标的ID:{0}", String.valueOf(loanId)));
        } catch (JsonProcessingException e) {
            // 记录日志，发短信通知管理员
            fatalLog(loanId, "发送MQ消息失败", e);
        }

        this.sendMessage(loanId);
        return callbackRequest.getResponseData();
    }

    private void fatalLog(long loanId, String errorMessage, Throwable e) {
        String errMsg = MessageFormat.format("loanId({0}), {1}", String.valueOf(loanId), errorMessage);
        logger.error(errMsg, e);
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("放款错误。详细信息：{0}",
                MessageFormat.format("{0},{1}", environment, errMsg)));
    }

    private void sendMessage(long loanId) {
        //Title:您出借的{0}已经满额放款，预期年化收益{1}%
        //Content:尊敬的用户，您出借的{0}项目已经满额放款，预期年化收益{1}%，快来查看收益吧。
        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        String title = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getTitleTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);
        String content = MessageFormat.format(MessageEventType.LOAN_OUT_SUCCESS.getContentTemplate(), loanModel.getName(), (loanModel.getBaseRate() + loanModel.getActivityRate()) * 100);

        List<String> loginNames = new ArrayList<>();
        Map<Long, String> investIdLoginNames = new HashMap<>();
        for (InvestModel investModel : investModels) {
            loginNames.add(investModel.getLoginName());
            investIdLoginNames.put(investModel.getId(), investModel.getLoginName());
        }

        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.LOAN_OUT_SUCCESS,
                title, content, investIdLoginNames));

        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(loginNames, PushSource.ALL, PushType.LOAN_OUT_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));

        mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(null, WeChatMessageType.LOAN_OUT_SUCCESS, loanId));
    }

}
