package com.tuotiansudai.paywrapper.loanout.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.CouponRepayService;
import com.tuotiansudai.paywrapper.repository.mapper.CouponRepayTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.CouponRepayTransferNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.LoanPeriodCalculator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CouponRepayServiceImpl implements CouponRepayService {

    private final static Logger logger = Logger.getLogger(CouponRepayServiceImpl.class);

    private static final List<CouponType> COUPON_TYPE_LIST = Lists.newArrayList(CouponType.NEWBIE_COUPON,
            CouponType.INVEST_COUPON,
            CouponType.INTEREST_COUPON,
            CouponType.BIRTHDAY_COUPON);

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.coupon.repay.notify.process.batch.size}")
    private int couponRepayProcessListSize;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "COUPON_REPAY:{0}";

    @Override
    public void repay(long loanRepayId, boolean isAdvanced) {
        logger.info(MessageFormat.format("[Coupon Repay {0}] coupon repay is starting...", String.valueOf(loanRepayId)));
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        LoanRepayModel currentLoanRepayModel = this.loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId(), COUPON_TYPE_LIST);

        for (UserCouponModel userCouponModel : userCouponModels) {
            logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) repay is starting...", String.valueOf(loanRepayId), String.valueOf(userCouponModel.getId())));

            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getPeriod() != null && currentLoanRepayModel.getPeriod() > couponModel.getPeriod()) {
                continue;
            }

            InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
            if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS || investModel.getTransferStatus() == TransferStatus.SUCCESS) {
                logger.warn(MessageFormat.format("[Coupon Repay {0}] invest({1}) is nonexistent or not success or has transferred",
                        String.valueOf(loanRepayId),
                        investModel == null ? "null" : String.valueOf(investModel.getId())));
                continue;
            }
            CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), currentLoanRepayModel.getPeriod());

            if (couponRepayModel == null) {
                logger.error(MessageFormat.format("Coupon Repay loanRepayId:{0},userCouponId:{1},period:{2} is nonexistent",
                        currentLoanRepayModel.getId(),
                        userCouponModel.getId(),
                        currentLoanRepayModel.getPeriod()));
                continue;
            }

            if (couponRepayModel.getStatus() == RepayStatus.COMPLETE) {
                logger.error(MessageFormat.format("Coupon Repay:{0} loanRepayId:{1},userCouponId:{2} status is COMPLETE",
                        couponRepayModel.getId(),
                        currentLoanRepayModel.getId(),
                        userCouponModel.getId(),
                        currentLoanRepayModel.getPeriod()));
                continue;
            }

            long investAmount = investModel.getAmount();
            long actualInterest = InterestCalculator.calculateCouponActualInterest(investAmount, couponModel, userCouponModel, loanModel, currentLoanRepayModel, loanRepayModels);
            if (actualInterest < 0) {
                continue;
            }
            long actualFee = (long) (actualInterest * investModel.getInvestFeeRate());
            long transferAmount = actualInterest - actualFee;
            logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) is {2}, repay amount is  {3}({4} - {5})",
                    String.valueOf(currentLoanRepayModel.getId()),
                    String.valueOf(userCouponModel.getId()),
                    couponModel.getCouponType().name(),
                    String.valueOf(transferAmount),
                    String.valueOf(actualInterest),
                    String.valueOf(actualFee)));

            userCouponModel.setActualInterest(userCouponModel.getActualInterest() + actualInterest);
            userCouponModel.setActualFee(userCouponModel.getActualFee() + actualFee);
            userCouponMapper.update(userCouponModel);

            this.updateCouponRepayBeforeCallback(actualInterest, actualFee, investModel.getId(), couponRepayModel, isAdvanced);

            if (transferAmount > 0) {
                try {
                    TransferRequestModel requestModel = TransferRequestModel.newCouponRepayRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE,
                            String.valueOf(couponRepayModel.getId()), String.valueOf(new Date().getTime())),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayAccountId(),
                            String.valueOf(transferAmount));

                    String statusString = redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()));
                    if (Strings.isNullOrEmpty(statusString) || SyncRequestStatus.FAILURE.equals(SyncRequestStatus.valueOf(statusString))) {
                        redisWrapperClient.hset(redisKey, String.valueOf(couponRepayModel.getId()), SyncRequestStatus.SENT.name());
                        logger.info(MessageFormat.format("[Coupon Repay loanRepayId {0}] couponRepayModel.id ({1}) send payback request",
                                String.valueOf(loanRepayId), String.valueOf(couponRepayModel.getId())));

                        TransferResponseModel responseModel = paySyncClient.send(CouponRepayTransferMapper.class, requestModel, TransferResponseModel.class);
                        boolean isPaySuccess = responseModel.isSuccess();
                        redisWrapperClient.hset(redisKey, String.valueOf(couponRepayModel.getId()), isPaySuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                        logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer status is {2}\n" +
                                        "[Coupon Repay loanRepayId {3}] couponRepayModel.id ({4}) payback response is {5}",
                                String.valueOf(currentLoanRepayModel.getId()),
                                String.valueOf(userCouponModel.getId()),
                                String.valueOf(isPaySuccess), String.valueOf(loanRepayId),
                                String.valueOf(couponRepayModel.getId()), String.valueOf(isPaySuccess)));
                    }

                    logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1})",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId())));
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer is failed\n" +
                                    "[Coupon Repay loanRepayId {2}] couponRepayModel.id ({3}) payback throw exception",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId()),
                            String.valueOf(loanRepayId),
                            String.valueOf(couponRepayModel.getId()), e));
                    sendSmsErrNotify(MessageFormat.format("{0}, 优惠券还款失败，Coupon Repay Id {1}", environment, String.valueOf(couponRepayModel.getId())));
                }
            } else {
                this.updateCouponRepayAfterCallback(investModel.getId(), couponRepayModel, isAdvanced);
            }
        }

        logger.info(MessageFormat.format("[Coupon Repay {0}] coupon repay is async send success", String.valueOf(loanRepayId)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateCouponRepay(long loanId) {
        boolean result = true;
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        if (CollectionUtils.isEmpty(successInvestModels)) {
            logger.error(MessageFormat.format("(invest record is exist (loanId = {0}))", String.valueOf(loanId)));
            return false;
        }

        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            logger.error(MessageFormat.format("[Generate_Coupon_Repay:] loanId:{0} 优惠券回款计划生成失败，标的不存在", String.valueOf(loanId)));
            return false;
        }

        int totalPeriods = LoanPeriodCalculator.calculateLoanPeriods(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        if (totalPeriods == 0) {
            logger.error(MessageFormat.format("[Generate_Coupon_Repay:] loanId:{0} recheckTime is null or deadline is null or recheckTime is after deadline", String.valueOf(loanId)));
            return false;
        }

        List<Integer> daysOfPerPeriod = LoanPeriodCalculator.calculateDaysOfPerPeriod(loanModel.getRecheckTime(), loanModel.getDeadline(), loanModel.getType());
        if (CollectionUtils.isEmpty(daysOfPerPeriod)) {
            logger.error(MessageFormat.format("[Generate_Coupon_Repay:] loanId:{0} 计算标的每期天数失败", String.valueOf(loanId)));
            return false;
        }

        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);

        for (int index = 0; index < totalPeriods; index++) {

            int period = index + 1; //当前计算的期数
            int currentPeriodDuration = daysOfPerPeriod.get(index); //当期的借款天数
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration); //当前回款时间

            for (InvestModel successInvestModel : successInvestModels) {
                List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessAndCouponTypeByInvestId(successInvestModel.getId(), COUPON_TYPE_LIST);
                for (UserCouponModel userCouponModel : userCouponModels) {
                    CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), period);
                    if (couponRepayModel != null) {
                        logger.info(MessageFormat.format("coupon repay is exist (user coupon id = {0})", userCouponModel.getId()));
                        continue;
                    }
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    if (couponModel == null) {
                        logger.error(MessageFormat.format("(coupon is not exist (couponId = {0}))", userCouponModel.getCouponId()));
                        continue;
                    }
                    if (couponModel.getPeriod() != null && couponModel.getPeriod() < period) {
                        continue;
                    }
                    long expectedCouponInterest = InterestCalculator.estimateCouponRepayExpectedInterest(successInvestModel,
                            loanModel, couponModel, currentRepayDate, lastRepayDate);
                    long expectedFee = new BigDecimal(expectedCouponInterest).setScale(0, BigDecimal.ROUND_DOWN)
                            .multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();
                    try {
                        couponRepayMapper.create(new CouponRepayModel(successInvestModel.getLoginName(),
                                couponModel.getId(),
                                userCouponModel.getId(),
                                successInvestModel.getId(),
                                expectedCouponInterest,
                                expectedFee,
                                period,
                                currentRepayDate.toDate()
                        ));
                        logger.info(MessageFormat.format("generate coupon repay is success, user={0}, userCouponId={1}, period={2}",
                                successInvestModel.getLoginName(),
                                String.valueOf(userCouponModel.getId()),
                                String.valueOf(period)));
                    } catch (Exception e) {
                        result = false;
                        logger.error(MessageFormat.format("generate coupon repay is fail, user={0}, userCouponId={1}, period={2}",
                                successInvestModel.getLoginName(),
                                String.valueOf(userCouponModel.getId()),
                                String.valueOf(period)));
                    }
                }
            }
            lastRepayDate = currentRepayDate;
        }
        return result;
    }

    private void updateCouponRepayBeforeCallback(long actualInterest, long actualFee, long investId, final CouponRepayModel couponRepayModel, boolean isAdvanced) {
        try {
            couponRepayModel.setActualInterest(actualInterest);
            couponRepayModel.setActualFee(actualFee);
            couponRepayModel.setRepayAmount(actualInterest - actualFee);
            couponRepayMapper.update(couponRepayModel);
            if (isAdvanced) {
                List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
                for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                    if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                        couponRepayMapper.update(advancedCouponRepayModel);
                        logger.info(MessageFormat.format("[Advance Repay] update other ActualRepayDate coupon repay({0})",
                                String.valueOf(advancedCouponRepayModel.getId())));
                    }
                }
            }
        } catch (Exception e) {
            fatalLog("updateCouponRepayBeforeCallback Exception. currentCouponRepayModelId:" + couponRepayModel.getId(), e);
        }

    }

    private void updateCouponRepayAfterCallback(long investId, final CouponRepayModel couponRepayModel, boolean isAdvanced) {
        try {
            couponRepayModel.setStatus(RepayStatus.COMPLETE);
            couponRepayModel.setActualRepayDate(new Date());
            couponRepayMapper.update(couponRepayModel);

            //更新 所有逾期的还款为 COMPLETE
            couponRepayMapper.findByUserCouponByInvestId(investId).stream()
                    .filter(model->model.getStatus() == RepayStatus.OVERDUE)
                    .forEach(model -> {
                        model.setActualRepayDate(new Date());
                        model.setStatus(RepayStatus.COMPLETE);
                        couponRepayMapper.update(model);
                        logger.info(MessageFormat.format("invest:{0}, currentCouponRepay:{1} update overdue coupon repay:{2} status to COMPLETE",
                                String.valueOf(investId), String.valueOf(couponRepayModel.getId()), String.valueOf(model.getId())));
                    });

            if (isAdvanced) {
                List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
                for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                    if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                        advancedCouponRepayModel.setStatus(RepayStatus.COMPLETE);
                        advancedCouponRepayModel.setActualRepayDate(new Date());
                        couponRepayMapper.update(advancedCouponRepayModel);
                        logger.info(MessageFormat.format("[Advance Repay] update other REPAYING coupon repay({0}) status to COMPLETE",
                                String.valueOf(advancedCouponRepayModel.getId())));
                    }
                }
            }
        } catch (Exception e) {
            fatalLog("updateCouponRepayAfterCallback Exception. currentCouponRepayModelId:" + couponRepayModel.getId(), e);
        }
    }

    @Override
    public String couponRepayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CouponRepayTransferNotifyRequestMapper.class,
                TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }


        long couponRepayId = Long.parseLong(callbackRequest.getOrderId().split("X")[0]);
        try {
            CouponRepayModel couponRepayModel = couponRepayMapper.findById(couponRepayId);
            InvestModel investModel = investMapper.findById(couponRepayModel.getInvestId());
            LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(investModel.getLoanId(), couponRepayModel.getPeriod());

            redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayModel.getId())), String.valueOf(couponRepayId),
                    callbackRequest.isSuccess() ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
        } catch (Exception e) {
            logger.error(MessageFormat.format("coupon repay callback exception {0}", String.valueOf(couponRepayId)), e);
        }

        if (callbackRequest.isSuccess()) {
            mqWrapperClient.sendMessage(MessageQueue.RepaySuccessCouponRepayCallback, String.valueOf(couponRepayId));
        } else {
            sendSmsErrNotify(MessageFormat.format("{0}, 优惠券还款，联动优势回调失败 CouponRepayId = {1}", environment, String.valueOf(couponRepayId)));
        }

        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncCouponRepayCallback(long couponRepayId) {
        CouponRepayModel couponRepayModel = couponRepayMapper.findById(couponRepayId);
        InvestModel investModel = investMapper.findById(couponRepayModel.getInvestId());
        CouponModel couponModel = couponMapper.findById(couponRepayModel.getCouponId());

        boolean isAdvanced = new DateTime(couponRepayModel.getActualRepayDate()).withTimeAtStartOfDay().isBefore(new DateTime(couponRepayModel.getRepayDate()).withTimeAtStartOfDay());

        try {
            if (couponRepayModel.getStatus() != RepayStatus.COMPLETE) {
                this.updateCouponRepayAfterCallback(investModel.getId(), couponRepayModel, isAdvanced);
                AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, couponRepayModel.getLoginName(),
                        couponRepayModel.getUserCouponId(),
                        couponRepayModel.getActualInterest(),
                        couponModel.getCouponType().getUserBillBusinessType(), null, null);

                AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, couponRepayModel.getLoginName(),
                        couponRepayModel.getUserCouponId(),
                        couponRepayModel.getActualFee(),
                        UserBillBusinessType.INVEST_FEE, null, null);

                inAtm.setNext(outAtm);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, inAtm);

                String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                        couponModel.getCouponType().getName(),
                        String.valueOf(couponRepayModel.getUserCouponId()),
                        String.valueOf(couponRepayModel.getId()),
                        String.valueOf(couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()));

                SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT, couponRepayModel.getUserCouponId(), (couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()), SystemBillBusinessType.COUPON, detail);
                mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);

                logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill and system bill is success",
                        String.valueOf(couponRepayModel.getId()),
                        String.valueOf(couponRepayModel.getUserCouponId())));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill is failed",
                    String.valueOf(couponRepayModel.getId()),
                    String.valueOf(couponRepayModel.getUserCouponId())), e);
            fatalLog("coupon repay processOneCallback error. currentLoanRepayModelId:" + couponRepayModel.getId(), e);
        }

        BaseDto<PayDataDto> asyncCouponRepayNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncCouponRepayNotifyDto.setData(baseDataDto);

        return asyncCouponRepayNotifyDto;
    }

    private void fatalLog(String errMsg, Throwable e) {
        logger.error(errMsg, e);
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("还款时优惠券发放业务错误。详细信息：{0}", errMsg));
    }


    public static void main(String args[]){
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4);
        list.stream().filter(i -> i > 2).forEach(i -> System.out.print(i));
    }
}