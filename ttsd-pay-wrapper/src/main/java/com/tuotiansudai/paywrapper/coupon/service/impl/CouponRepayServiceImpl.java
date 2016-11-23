package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
import com.tuotiansudai.job.CouponRepayNotifyCallbackJob;
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
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CouponRepayServiceImpl implements CouponRepayService {

    static Logger logger = Logger.getLogger(CouponRepayServiceImpl.class);

    public static final List<CouponType> COUPON_TYPE_LIST = Lists.newArrayList(CouponType.NEWBIE_COUPON,
            CouponType.INVEST_COUPON,
            CouponType.INTEREST_COUPON,
            CouponType.BIRTHDAY_COUPON);

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

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

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.coupon.repay.notify.process.batch.size}")
    private int couponRepayProcessListSize;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "COUPON_REPAY:{0}";

    @Override
    public void repay(long loanRepayId, boolean isAdvanced) {
        logger.debug(MessageFormat.format("[Coupon Repay {0}] coupon repay is starting...", String.valueOf(loanRepayId)));
        String redisKey = MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayId));
        LoanRepayModel currentLoanRepayModel = this.loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId(), COUPON_TYPE_LIST);

        for (UserCouponModel userCouponModel : userCouponModels) {
            logger.debug(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) repay is starting...", String.valueOf(loanRepayId), String.valueOf(userCouponModel.getId())));

            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && currentLoanRepayModel.getPeriod() > 1) {
                continue;
            }

            InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
            if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS || investModel.getTransferStatus() == TransferStatus.SUCCESS) {
                logger.error(MessageFormat.format("[Coupon Repay {0}] invest({1}) is nonexistent or not success or has transferred",
                        String.valueOf(loanRepayId),
                        investModel == null ? "null" : String.valueOf(investModel.getId())));
                continue;
            }
            CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), currentLoanRepayModel.getPeriod());

            if (couponRepayModel == null) {
                logger.error(MessageFormat.format("Coupon Repay loanRepayId:{0},userCouponId:{1},period:{2} is nonexistent",
                        currentLoanRepayModel.getLoanId(),
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
            this.updateCouponRepay(actualInterest, actualFee, investModel.getId(), couponRepayModel, currentLoanRepayModel.getId(), isAdvanced);

            if (transferAmount > 0) {
                try {
                    TransferWithNotifyRequestModel requestModel = TransferWithNotifyRequestModel.newCouponRepayRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(couponRepayModel.getId()), String.valueOf(new Date().getTime())),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
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
                                String.valueOf(currentLoanRepayModel.getId()), String.valueOf(userCouponModel.getId()),
                                String.valueOf(isPaySuccess), String.valueOf(loanRepayId),
                                String.valueOf(couponRepayModel.getId()), String.valueOf(isPaySuccess)));
                    }

                    logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1})",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId())));
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer is failed\n" +
                            "[Coupon Repay loanRepayId {2}] couponRepayModel.id ({3}) payback throw exception",
                    String.valueOf(currentLoanRepayModel.getId()), String.valueOf(userCouponModel.getId()),
                    String.valueOf(loanRepayId), String.valueOf(couponRepayModel.getId()), e));
                }
            }

        }

        logger.info(MessageFormat.format("[Coupon Repay {0}] coupon repay is async send success", String.valueOf(loanRepayId)));
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
                        logger.debug(MessageFormat.format("coupon repay is exist (user coupon id = {0})", userCouponModel.getId()));
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

    private void updateCouponRepay(long actualInterest, long actualFee, long investId, final CouponRepayModel couponRepayModel, long loanRepayId, boolean isAdvanced) {
        couponRepayModel.setActualInterest(actualInterest);
        couponRepayModel.setActualFee(actualFee);
        couponRepayModel.setRepayAmount(actualInterest - actualFee);
        couponRepayModel.setActualRepayDate(new Date());
        couponRepayMapper.update(couponRepayModel);
        if (isAdvanced) {
            List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
            for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                    advancedCouponRepayModel.setActualRepayDate(new Date());
                    couponRepayMapper.update(advancedCouponRepayModel);
                    logger.info(MessageFormat.format("[Advance Repay {0}] update other REPAYING coupon repay({1}) status to WAIT_PAY",
                            String.valueOf(loanRepayId), String.valueOf(advancedCouponRepayModel.getId())));
                }
            }
        }

    }

    private void updateCouponRepayRepayStatus(long investId, final CouponRepayModel couponRepayModel, long loanRepayId, boolean isAdvanced) {
        couponRepayModel.setStatus(RepayStatus.COMPLETE);
        couponRepayMapper.update(couponRepayModel);
        if (isAdvanced) {
            List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
            for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                    advancedCouponRepayModel.setStatus(RepayStatus.COMPLETE);
                    couponRepayMapper.update(advancedCouponRepayModel);
                    logger.info(MessageFormat.format("[Advance Repay {0}] update other REPAYING coupon repay({1}) status to COMPLETE",
                            String.valueOf(loanRepayId), String.valueOf(advancedCouponRepayModel.getId())));
                }
            }
        }

    }

    @Override
    public String couponRepayCallback(Map<String, String> paramsMap, String originalQueryString){
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                originalQueryString,
                CouponRepayNotifyRequestMapper.class,
                CouponRepayNotifyRequestModel.class);
        if (callbackRequest == null) {
            return null;
        }
        redisWrapperClient.incr(CouponRepayNotifyCallbackJob.COUPON_REPAY_JOB_TRIGGER_KEY);
        return callbackRequest.getResponseData();
    }

    @Override
    public BaseDto<PayDataDto> asyncCouponRepayCallback() {
        List<CouponRepayNotifyRequestModel> todoList = couponRepayNotifyRequestMapper.getTodoList(couponRepayProcessListSize);

        todoList.stream().filter(model -> updateCouponRepayNotifyRequestStatus(model)).forEach(model -> {
            try {
                this.processOneCallback(model);
            } catch (Exception e) {
                fatalLog("coupon repay callback, processOneCallback error. couponRepayId:" + model.getOrderId(), e);
            }
        });

        BaseDto<PayDataDto> asyncCouponRepayNotifyDto = new BaseDto<>();
        PayDataDto baseDataDto = new PayDataDto();
        baseDataDto.setStatus(true);
        asyncCouponRepayNotifyDto.setData(baseDataDto);

        return asyncCouponRepayNotifyDto;
    }

    private boolean updateCouponRepayNotifyRequestStatus(CouponRepayNotifyRequestModel model) {
        try {
            redisWrapperClient.decr(CouponRepayNotifyCallbackJob.COUPON_REPAY_JOB_TRIGGER_KEY);
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

                this.updateCouponRepayRepayStatus(investModel.getId(), couponRepayModel, loanRepayModel.getId(), isAdvanced);

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
            redisWrapperClient.hset(MessageFormat.format(REPAY_REDIS_KEY_TEMPLATE, String.valueOf(loanRepayModel.getId())), String.valueOf(couponRepayId),  SyncRequestStatus.FAILURE.name());

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