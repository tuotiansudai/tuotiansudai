package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponInvestService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.InvestNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNopwdMapper;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferNopwdRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.ProjectTransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferResponseModel;
import com.tuotiansudai.paywrapper.service.InvestAchievementService;
import com.tuotiansudai.paywrapper.service.InvestService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AutoInvestMonthPeriod;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class InvestServiceImpl implements InvestService {

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private CouponInvestService couponInvestService;

    @Autowired
    private InvestAchievementService investAchievementService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.auto.invest.interval.milliseconds}")
    private int autoInvestIntervalMilliseconds;

    @Value(value = "${web.newbie.invest.limit}")
    private int newbieInvestLimit;

    @Value("#{'${loan.raising.complete.notify.mobiles}'.split('\\|')}")
    private List<String> loanRaisingCompleteNotifyMobileList;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Override
    @Transactional
    public BaseDto<PayFormDataDto> invest(InvestDto dto) {
        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        String loginName = dto.getLoginName();
        double rate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        InvestModel investModel = new InvestModel(IdGenerator.generate(), Long.parseLong(dto.getLoanId()), null, AmountConverter.convertStringToCent(dto.getAmount()), dto.getLoginName(), new Date(), dto.getSource(), dto.getChannel(), rate);
        investMapper.create(investModel);

        logger.info(MessageFormat.format("[Invest Request Data] user={0}, loan={1}, invest={2}, amount={3}, userCoupon={4}, source={5}",
                dto.getLoginName(),
                dto.getLoanId(),
                String.valueOf(investModel.getId()),
                dto.getAmount(),
                CollectionUtils.isNotEmpty(dto.getUserCouponIds()) ? Joiner.on(",").join(dto.getUserCouponIds()) : "",
                dto.getSource()));

        try {
            ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newInvestRequest(
                    dto.getLoanId(),
                    String.valueOf(investModel.getId()),
                    accountModel.getPayUserId(),
                    String.valueOf(investModel.getAmount()), dto.getSource());
            BaseDto<PayFormDataDto> generateFormData = payAsyncClient.generateFormData(ProjectTransferMapper.class, requestModel);
            couponInvestService.invest(investModel.getId(), dto.getUserCouponIds());

            redisWrapperClient.lpush(InvestService.INVEST_CHECK_QUEUE_KEY, String.valueOf(investModel.getId()));

            return generateFormData;
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
            BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage(e.getMessage());
            baseDto.setData(payFormDataDto);
            return baseDto;
        }
    }

    private BaseDto<PayDataDto> invokeNoPassword(long loanId, long amount, String loginName, Source source, String channel, List<Long> userCouponIds, String prize) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        double rate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        InvestModel investModel = new InvestModel(IdGenerator.generate(), loanId, null, amount, loginName, new Date(), source, channel, rate);

        try {
            investModel.setNoPasswordInvest(true);
            investMapper.create(investModel);
            if (CollectionUtils.isNotEmpty(userCouponIds)) {
                couponInvestService.invest(investModel.getId(), userCouponIds);
            }

            logger.info(MessageFormat.format("[No Password Invest Request Data] user={0}, loan={1}, invest={2}, amount={3}, userCoupon={4}, source={5}",
                    loginName,
                    String.valueOf(loanId),
                    String.valueOf(investModel.getId()),
                    String.valueOf(amount),
                    CollectionUtils.isNotEmpty(userCouponIds) ? Joiner.on(",").join(userCouponIds) : "",
                    source));

        } catch (Exception e) {
            logger.error("create no password invest model failed", e);
            payDataDto.setMessage(e.getLocalizedMessage());
            return baseDto;
        }

        try {
            ProjectTransferNopwdRequestModel requestModel = ProjectTransferNopwdRequestModel.newInvestNopwdRequest(
                    String.valueOf(loanId),
                    String.valueOf(investModel.getId()),
                    accountModel.getPayUserId(),
                    String.valueOf(amount));

            redisWrapperClient.lpush(InvestService.INVEST_CHECK_QUEUE_KEY, String.valueOf(investModel.getId()));

            ProjectTransferNopwdResponseModel responseModel = paySyncClient.send(
                    ProjectTransferNopwdMapper.class,
                    requestModel,
                    ProjectTransferNopwdResponseModel.class);
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setExtraValues(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("order_id", String.valueOf(investModel.getId()))
                    .build()));
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (PayException e) {
            logger.error(e.getLocalizedMessage(), e);
            investModel.setStatus(InvestStatus.FAIL);
            investMapper.update(investModel);
            payDataDto.setMessage(e.getLocalizedMessage());
        }
        return baseDto;
    }

    /**
     * 投资回调接口，记录请求入库
     *
     * @param paramsMap
     * @param originalQueryString
     * @return
     */
    @Override
    @Transactional
    public String investCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                InvestNotifyRequestMapper.class,
                InvestNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        if (callbackRequest.isSuccess()) {
            mqWrapperClient.sendMessage(MessageQueue.InvestCallback, String.valueOf(callbackRequest.getOrderId()));
        }

        return callbackRequest.getResponseData();
    }

    @Override
    @Transactional
    public BaseDto<PayDataDto> asyncInvestCallback(long orderId) {
        InvestModel investModel = investMapper.lockById(orderId);
        BaseDto<PayDataDto> dto = new BaseDto<>(new PayDataDto(true));

        if (investModel == null) {
            logger.error(MessageFormat.format("invest(project_transfer) callback order is not exist (orderId = {0})", String.valueOf(orderId)));
            return dto;
        }
        if (investModel.getStatus() == InvestStatus.SUCCESS) {
            logger.warn(MessageFormat.format("invest callback process fail, because this invest has already succeed. (orderId = {0})", String.valueOf(orderId)));
            return dto;
        }

        String loginName = investModel.getLoginName();

        long loanId = investModel.getLoanId();
        long successInvestAmountTotal = investMapper.sumSuccessInvestAmount(loanId);
        LoanModel loanModel = loanMapper.findById(loanId);

        boolean isOverInvest = successInvestAmountTotal + investModel.getAmount() > loanModel.getLoanAmount();
        if (isOverInvest) {
            // 超投
            infoLog("over_invest", String.valueOf(orderId), investModel.getAmount(), loginName, loanId);
            // 超投返款处理
            overInvestPaybackProcess(orderId, investModel, loginName, loanId);
        } else {
            // 投资成功
            infoLog("invest_success", String.valueOf(orderId), investModel.getAmount(), loginName, loanId);

            // 投资成功，冻结用户资金，更新投资状态为success
            ((InvestService) AopContext.currentProxy()).investSuccess(investModel);

            if (successInvestAmountTotal + investModel.getAmount() == loanModel.getLoanAmount()) {
                // 满标，改标的状态 RECHECK
                checkLoanRaisingComplete(loanId);
            }
        }

        return dto;
    }

    /**
     * 超投处理：返款、记录userBill、更新投资状态为失败
     *
     * @param orderId
     * @param investModel
     * @param loginName
     * @param loanId
     */
    private boolean overInvestPaybackProcess(long orderId, InvestModel investModel, String loginName, long loanId) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        boolean paybackSuccess = false;

        String newOrderId = orderId + "X" + System.currentTimeMillis();

        ProjectTransferRequestModel requestModel = ProjectTransferRequestModel.newOverInvestPaybackRequest(
                String.valueOf(loanId), newOrderId, accountModel.getPayUserId(), String.valueOf(investModel.getAmount()));

        try {
            ProjectTransferResponseModel responseModel = paySyncClient.send(
                    ProjectTransferMapper.class,
                    requestModel,
                    ProjectTransferResponseModel.class);

            if (responseModel.isSuccess()) {
                // 超投返款成功
                infoLog("pay_back_success", newOrderId, investModel.getAmount(), loginName, loanId);
                paybackSuccess = true;
            } else {
                // 联动优势返回返款失败，但是标记此条请求已经处理完成，记录日志，在异步notify中进行投资成功处理
                errorLog("pay_back_fail", newOrderId, investModel.getAmount(), loginName, loanId);
            }
        } catch (PayException e) {
            // 调用umpay时出现异常(可能已经返款成功了)。发短信通知管理员
            fatalLog("pay_back_PayException", newOrderId, investModel.getAmount(), loginName, loanId, e);
        } catch (Exception e) {
            // 所有其他异常，包括数据库链接，网络异常，记录日志，发短信通知管理员，抛出异常，事务回滚。
            fatalLog("pay_back_other_exceptions", newOrderId, investModel.getAmount(), loginName, loanId, e);
            throw e;
        }

        if (!paybackSuccess) {
            // 如果返款失败，则记录本次投资为 超投返款失败
            investModel.setStatus(InvestStatus.OVER_INVEST_PAYBACK_FAIL);
            investMapper.update(investModel);
        }
        return paybackSuccess;
    }

    @Override
    public List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return autoInvestPlanMapper.findEnabledPlanByPeriod(period.getPeriodValue(), cal.getTime());
    }

    @Override
    public BaseDto<PayDataDto> noPasswordInvest(InvestDto dto) {
        return this.invokeNoPassword(Long.parseLong(dto.getLoanId()), AmountConverter.convertStringToCent(dto.getAmount()), dto.getLoginName(), dto.getSource(), dto.getChannel(), dto.getUserCouponIds(), dto.getZeroShoppingPrize());
    }

    @Override
    public void autoInvest(long loanId) {
        logger.info("auto invest start , loanId : " + loanId);
        LoanModel loanModel = loanMapper.findById(loanId);
        if (LoanStatus.RAISING != loanModel.getStatus()) {
            logger.info("can not auto invest, because loan status is not raising , loanId : " + loanId);
            return;
        }
        List<AutoInvestPlanModel> autoInvestPlanModels = this.findValidPlanByPeriod(AutoInvestMonthPeriod.generateFromLoanPeriod(loanModel.getType().getLoanPeriodUnit(), loanModel.getPeriods()));
        for (AutoInvestPlanModel autoInvestPlanModel : autoInvestPlanModels) {
            try {
                // recheck loan status
                loanModel = loanMapper.findById(loanId);
                if (LoanStatus.RAISING != loanModel.getStatus()) {
                    logger.info("auto invest was stop, because loan status is not raising , loanId : " + loanId);
                    return;
                }
                if (ActivityType.NEWBIE == loanModel.getActivityType() && !canInvestNewbieLoan(autoInvestPlanModel.getLoginName())) {
                    logger.info("auto invest was skip,because newbie is invested by new investor");
                    continue;
                }
                long availableLoanAmount = loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanId);
                if (availableLoanAmount <= 0) {
                    logger.info("auto invest was stop, because loan was full , loanId : " + loanId);
                    return;
                }
                long autoInvestCount = investMapper.countAutoInvest(loanId, autoInvestPlanModel.getLoginName());
                if (autoInvestCount >= 1) {
                    logger.info("auto invest was skip, because user [" + autoInvestPlanModel.getLoginName() + "] has auto-invest-ed on this loan : " + loanId);
                    continue;
                }
                long availableSelfLoanAmount = loanModel.getMaxInvestAmount() - investMapper.sumSuccessInvestAmountByLoginName(loanId, autoInvestPlanModel.getLoginName(), true);
                if (availableSelfLoanAmount <= 0) {
                    logger.info("auto invest was skip, because amount that user [" + autoInvestPlanModel.getLoginName() + "] has invested was reach max-invest-amount , loanId : " + loanId);
                    continue;
                }
                long autoInvestAmount = this.calculateAutoInvestAmount(autoInvestPlanModel, NumberUtils.min(availableLoanAmount, availableSelfLoanAmount, loanModel.getMaxInvestAmount()), loanModel.getInvestIncreasingAmount(), loanModel.getMinInvestAmount());
                if (autoInvestAmount == 0) {
                    logger.info("auto invest was skip, because loan amount is not match user's auto-invest setting [" + autoInvestPlanModel.getLoginName() + "] , loanId : " + loanId);
                    continue;
                }
                BaseDto<PayDataDto> baseDto = this.invokeNoPassword(loanId, autoInvestAmount, autoInvestPlanModel.getLoginName(), Source.AUTO, null, null, null);
                if (!baseDto.isSuccess()) {
                    logger.info(MessageFormat.format("auto invest failed auto invest plan id is {0} and invest amount is {1} and loanId id {2}", autoInvestPlanModel.getId(), autoInvestAmount, loanId));
                }
            } catch (Exception e) {
                logger.error("an error has occur on auto-invest of loan " + loanId + " :" + e.getLocalizedMessage(), e);
                continue;
            }

            if (autoInvestIntervalMilliseconds >= 0) {
                try {
                    Thread.sleep(autoInvestIntervalMilliseconds);
                } catch (InterruptedException e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    private boolean canInvestNewbieLoan(String loginName) {
        int newbieInvestCount = investMapper.sumSuccessInvestCountByLoginName(loginName);
        return newbieInvestLimit == 0 || newbieInvestCount < newbieInvestLimit;
    }

    private long calculateAutoInvestAmount(AutoInvestPlanModel autoInvestPlanModel, long availableLoanAmount, long investIncreasingAmount, long minLoanInvestAmount) {
        long availableAmount = accountMapper.findByLoginName(autoInvestPlanModel.getLoginName()).getBalance() - autoInvestPlanModel.getRetentionAmount();
        long maxInvestAmount = autoInvestPlanModel.getMaxInvestAmount();
        long minInvestAmount = autoInvestPlanModel.getMinInvestAmount();
        long returnAmount = 0;
        if (availableLoanAmount < minInvestAmount) {
            return returnAmount;
        }
        if (availableAmount >= maxInvestAmount) {
            returnAmount = maxInvestAmount;
        } else if (availableAmount < maxInvestAmount && availableAmount >= minInvestAmount) {
            returnAmount = availableAmount;
        }
        if (returnAmount >= availableLoanAmount) {
            returnAmount = availableLoanAmount;
        }
        if (returnAmount < minLoanInvestAmount) {
            return 0L;
        }
        long autoInvestMoney = returnAmount - (returnAmount - minLoanInvestAmount) % investIncreasingAmount;
        return autoInvestMoney < NumberUtils.max(minInvestAmount, minLoanInvestAmount) ? 0L : autoInvestMoney;
    }

    /**
     * 投资成功处理：冻结资金＋更新invest状态
     *
     * @param investModel
     */
    @Override
    @Transactional
    public void investSuccess(InvestModel investModel) {
        // 冻结资金
        AmountTransferMessage atm = new AmountTransferMessage(TransferType.FREEZE, investModel.getLoginName(), investModel.getId(), investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS, null, null);
        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

        // 改invest 本身状态为投资成功
        investModel.setStatus(InvestStatus.SUCCESS);
        //设置交易时间
        investModel.setTradingTime(new Date());
        investMapper.update(investModel);

        this.investAchievementService.awardAchievement(investModel);

        //投资成功后发送消息
        this.publishInvestSuccessMessage(investModel);
    }

    /**
     * umpay 超投返款的回调
     *
     * @param paramsMap
     * @param queryString
     * @return
     */
    @Override
    public String overInvestPaybackCallback(Map<String, String> paramsMap, String queryString) {

        logger.info("into over_invest_payback_callback, queryString: " + queryString);

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
            AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_FREEZE, investModel.getLoginName(), investModel.getId(), investModel.getAmount(), UserBillBusinessType.OVER_INVEST_PAYBACK, null, null);
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
        } else {
            // 返款失败，当作投资成功处理
            errorLog("pay_back_notify_fail,take_as_invest_success", orderIdStr, investModel.getAmount(), loginName, investModel.getLoanId());

            ((InvestService) AopContext.currentProxy()).investSuccess(investModel);

            long loanId = investModel.getLoanId();
            checkLoanRaisingComplete(loanId);
        }

        return callbackRequest.getResponseData();
    }

    private void publishInvestSuccessMessage(InvestModel investModel) {
        //Title:恭喜您成功投资{0}元
        //Content:尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！
        String title = MessageFormat.format(MessageEventType.INVEST_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(investModel.getAmount()));
        String content = MessageFormat.format(MessageEventType.INVEST_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(investModel.getAmount()));
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.INVEST_SUCCESS,
                Lists.newArrayList(investModel.getLoginName()),
                title,
                content,
                investModel.getId()
        ));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()),
                PushSource.ALL,
                PushType.INVEST_SUCCESS,
                title,
                AppUrl.MESSAGE_CENTER_LIST));
        try {

            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(),
                    WeChatMessageType.INVEST_SUCCESS,
                    investModel.getId()
            ));
        } catch (Exception e) {
            logger.error("invest success wechat message notify send fail", e);
        }

        InvestInfo investInfo = new InvestInfo();
        LoanDetailInfo loanDetailInfo = new LoanDetailInfo();
        UserInfo userInfo = new UserInfo();

        UserModel userModel = userMapper.findByLoginName(investModel.getLoginName());
        userInfo.setLoginName(userModel.getLoginName());
        userInfo.setUserName(userModel.getUserName());
        userInfo.setMobile(userModel.getMobile());
        investInfo.setInvestId(investModel.getId());
        investInfo.setLoginName(investModel.getLoginName());
        investInfo.setAmount(investModel.getAmount());
        investInfo.setStatus(investModel.getStatus().name());
        investInfo.setTransferStatus(investModel.getTransferStatus().name());

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        loanDetailInfo.setLoanId(investModel.getLoanId());
        loanDetailInfo.setDuration(loanModel.getProductType().getDuration());
        loanDetailInfo.setActivityType(loanModel.getActivityType().name());
        if (loanDetailsModel != null) {
            loanDetailInfo.setActivity(loanDetailsModel.isActivity());
            loanDetailInfo.setActivityDesc(loanDetailsModel.getActivityDesc());
        }
        try {
            mqWrapperClient.publishMessage(MessageTopic.InvestSuccess, new InvestSuccessMessage(investInfo, loanDetailInfo, userInfo));

        } catch (JsonProcessingException e) {
            // 记录日志，发短信通知管理员
            fatalLog("[MQ] invest success, but send mq message fail", String.valueOf(investInfo.getInvestId()), investInfo.getAmount(), investInfo.getLoginName(), investModel.getLoanId(), e);
        }

    }

    private void checkLoanRaisingComplete(long loanId) {
        // 改标的状态为满标 RECHECK
        loanMapper.updateStatus(loanId, LoanStatus.RECHECK);
        // 更新筹款完成时间
        loanMapper.updateRaisingCompleteTime(loanId, new Date());

        try {
            // 发送满标提醒
            sendLoanRaisingCompleteNotify(loanId);
        } catch (Exception e) {
            logger.error("send loan raising complete notify failed.", e);
        }

        DelayMessageDeliveryJobCreator.createAutoLoanOutDelayJob(jobManager, loanId);
    }

    private void sendLoanRaisingCompleteNotify(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM月dd日");

        String loanRaisingStartDate = sdfDate.format(loanModel.getFundraisingStartTime());

        String loanName = loanModel.getName();

        long loanAmount = loanModel.getLoanAmount();
        String loanAmountStr; // 单位：万
        if (loanAmount % 1000000 == 0)
            loanAmountStr = String.valueOf(loanAmount / 1000000);
        else
            loanAmountStr = String.valueOf((double) (loanAmount / 10000) / 100);

        String loanDuration = String.valueOf(loanModel.getDuration());

        LoanerDetailsModel loanerModel = loanerDetailsMapper.getByLoanId(loanId);
        String loanerName = loanerModel == null ? "" : loanerModel.getUserName();

        UserModel agentModel = userMapper.findByLoginName(loanModel.getAgentLoginName());
        String agentUserName = agentModel == null ? "" : agentModel.getUserName();

        SimpleDateFormat sdfTime = new SimpleDateFormat("HH点mm分");
        String loanRaisingCompleteTime = sdfTime.format(loanModel.getRaisingCompleteTime());

        logger.info("will send loan raising complete notify, loanId:" + loanId);

        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_LOAN_RAISING_COMPLETE_NOTIFY_TEMPLATE, loanRaisingCompleteNotifyMobileList,
                Lists.newArrayList(loanRaisingStartDate, loanDuration, loanAmountStr, loanRaisingCompleteTime, loanerName, agentUserName)));

        mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(null, WeChatMessageType.LOAN_COMPLETE, loanId));


    }

    private void infoLog(String msg, String orderId, long amount, String loginName, long loanId) {
        logger.info(msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId);
    }

    private void errorLog(String msg, String orderId, long amount, String loginName, long loanId) {
        errorLog(msg, orderId, amount, loginName, loanId, null);
    }

    private void errorLog(String msg, String orderId, long amount, String loginName, long loanId, Throwable e) {
        logger.error(msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId, e);
    }

    private void fatalLog(String msg, String orderId, long amount, String loginName, long loanId, Throwable e) {
        String errMsg = msg + ",orderId:" + orderId + ",LoginName:" + loginName + ",amount:" + amount + ",loanId:" + loanId;
        fatalLog(errMsg, e);
    }

    private void fatalLog(String errMsg, Throwable e) {
        logger.fatal(errMsg, e);
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("sent invest fatal sms message");
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("投资业务错误。详细信息：{0}", errMsg));
    }
}
