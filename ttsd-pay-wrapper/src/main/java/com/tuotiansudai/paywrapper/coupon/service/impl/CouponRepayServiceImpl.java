package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.CouponRepayNotifyRequestMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferWithNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.InterestCalculator;
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

@Service
public class CouponRepayServiceImpl implements CouponRepayService {

    static Logger logger = Logger.getLogger(CouponRepayServiceImpl.class);

    public static final List<CouponType> COUPON_TYPE_LIST = Lists.newArrayList(CouponType.NEWBIE_COUPON,
            CouponType.INVEST_COUPON,
            CouponType.INTEREST_COUPON,
            CouponType.BIRTHDAY_COUPON);

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

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
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private CouponRepayNotifyRequestMapper couponRepayNotifyRequestMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.coupon.repay.notify.process.batch.size}")
    private int couponRepayProcessListSize;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "COUPON_REPAY:{0}";

    @Override
    public void sendCouponRepayAmount(long loanRepayId, String payUserId, CouponRepayModel couponRepayModel, LoanRepayModel loanRepayModel, UserCouponModel userCouponModel, long transferAmount) throws Exception {
            try {
                String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
                TransferWithNotifyRequestModel requestModel = TransferWithNotifyRequestModel.newCouponRepayRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(couponRepayModel.getId()), String.valueOf(new Date().getTime())),
                        payUserId,
                        String.valueOf(transferAmount));

                String statusString = redisWrapperClient.hget(redisKey, String.valueOf(couponRepayModel.getId()));
                if (Strings.isNullOrEmpty(statusString) || SyncRequestStatus.FAILURE.equals(SyncRequestStatus.valueOf(statusString))) {
                    redisWrapperClient.hset(redisKey, String.valueOf(couponRepayModel.getId()), SyncRequestStatus.SENT.name());
                    logger.info(MessageFormat.format("[Coupon Repay loanRepayId {0}] couponRepayModel.id ({1}) send payback request",
                            String.valueOf(loanRepayId), String.valueOf(couponRepayModel.getId())));

                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                    boolean isPaySuccess = responseModel.isSuccess();
                    redisWrapperClient.hset(redisKey, String.valueOf(couponRepayModel.getId()), isPaySuccess ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
                    logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer status is {2}\n" +
                                    "[Coupon Repay loanRepayId {3}] couponRepayModel.id ({4}) payback response is {5}",
                            String.valueOf(loanRepayModel.getId()), String.valueOf(userCouponModel.getId()),
                            String.valueOf(isPaySuccess), String.valueOf(loanRepayId),
                            String.valueOf(couponRepayModel.getId()), String.valueOf(isPaySuccess)));
                }

                logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1})",
                        String.valueOf(loanRepayModel.getId()),
                        String.valueOf(userCouponModel.getId())));
            } catch (PayException e) {
                logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer is failed\n" +
                                "[Coupon Repay loanRepayId {2}] couponRepayModel.id ({3}) payback throw exception",
                        String.valueOf(loanRepayModel.getId()), String.valueOf(userCouponModel.getId()),
                        String.valueOf(loanRepayId), String.valueOf(couponRepayModel.getId()), e));
            }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateCouponRepay(long loanId) {
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        if (CollectionUtils.isEmpty(successInvestModels)) {
            logger.error(MessageFormat.format("(invest record is exist (loanId = {0}))", String.valueOf(loanId)));
            return;
        }
        LoanModel loanModel = loanMapper.findById(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.getPeriods();
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);

        for (int period = 1; period <= totalPeriods; period++) {
            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration);
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
                    if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && period > 1) {
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
                        logger.error(e.getLocalizedMessage(), e);
                    }
                }
            }
            lastRepayDate = currentRepayDate;
        }
    }


    @Override
    public String couponRepayCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CouponRepayNotifyRequestMapper.class,
                CouponRepayNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        mqWrapperClient.sendMessage(MessageQueue.CouponRepayCallback, String.valueOf(callbackRequest.getId()));
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncCouponRepayCallback(long notifyRequestId) {
        CouponRepayNotifyRequestModel model = couponRepayNotifyRequestMapper.findById(notifyRequestId);

        if (updateCouponRepayNotifyRequestStatus(model)) {
            try {
                this.processOneCallback(model);
            } catch (Exception e) {
                fatalLog("coupon repay callback, processOneCallback error. couponRepayId:" + model.getOrderId() + ", id:" + model.getId(), e);
            }
        }

        BaseDto<PayDataDto> asyncCouponRepayNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncCouponRepayNotifyDto.setData(baseDataDto);

        return asyncCouponRepayNotifyDto;
    }

    private boolean updateCouponRepayNotifyRequestStatus(CouponRepayNotifyRequestModel model) {
        if(model == null){
            logger.info("CouponRepayNotifyRequestModel is null");
            return false;
        }
        try {
            couponRepayNotifyRequestMapper.updateStatus(model.getId(), NotifyProcessStatus.DONE);
        } catch (Exception e) {
            fatalLog("update_coupon_repay_notify_status_fail, orderId:" + model.getOrderId() + ",id:" + model.getId(), e);
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public void processOneCallback(CouponRepayNotifyRequestModel callbackRequestModel) {

        long couponRepayId = Long.parseLong(callbackRequestModel.getOrderId().split("X")[0]);
        CouponRepayModel couponRepayModel = couponRepayMapper.findById(couponRepayId);
        InvestModel investModel = investMapper.findById(couponRepayModel.getInvestId());
        LoanRepayModel loanRepayModel = loanRepayMapper.findByLoanIdAndPeriod(investModel.getLoanId(), couponRepayModel.getPeriod());
        CouponModel couponModel = couponMapper.findById(couponRepayModel.getCouponId());

        boolean isAdvanced = new DateTime(couponRepayModel.getActualRepayDate()).withTimeAtStartOfDay().isBefore(new DateTime(couponRepayModel.getRepayDate()).withTimeAtStartOfDay());
        try {
            if (!couponRepayModel.getStatus().equals(RepayStatus.COMPLETE)) {

                redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayModel.getId())), String.valueOf(couponRepayId), SyncRequestStatus.SUCCESS.name());

                //this.updateCouponRepayAfterCallback(investModel.getId(), couponRepayModel, isAdvanced);

                amountTransfer.transferInBalance(couponRepayModel.getLoginName(),
                        couponRepayModel.getUserCouponId(),
                        couponRepayModel.getActualInterest(),
                        couponModel.getCouponType().getUserBillBusinessType(), null, null);

                amountTransfer.transferOutBalance(couponRepayModel.getLoginName(),
                        couponRepayModel.getUserCouponId(),
                        couponRepayModel.getActualFee(),
                        UserBillBusinessType.INVEST_FEE, null, null);

                String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                        couponModel.getCouponType().getName(),
                        String.valueOf(couponRepayModel.getUserCouponId()),
                        String.valueOf(couponRepayModel.getId()),
                        String.valueOf(couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()));

                systemBillService.transferOut(couponRepayModel.getUserCouponId(), (couponRepayModel.getActualInterest() - couponRepayModel.getActualFee()), SystemBillBusinessType.COUPON, detail);

                logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill and system bill is success",
                        String.valueOf(couponRepayModel.getId()),
                        String.valueOf(couponRepayModel.getUserCouponId())));
            }
        } catch (Exception e) {
            redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayModel.getId())), String.valueOf(couponRepayId), SyncRequestStatus.FAILURE.name());

            logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill is failed",
                    String.valueOf(couponRepayModel.getId()),
                    String.valueOf(couponRepayModel.getUserCouponId())), e);
            fatalLog("coupon repay processOneCallback error. currentLoanRepayModelId:" + couponRepayModel.getId(), e);
        }

    }

    private void fatalLog(String errMsg, Throwable e) {
        logger.fatal(errMsg, e);
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("sent coupon repay fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(MessageFormat.format("还款时优惠券发放业务错误。详细信息：{0}", errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }

}