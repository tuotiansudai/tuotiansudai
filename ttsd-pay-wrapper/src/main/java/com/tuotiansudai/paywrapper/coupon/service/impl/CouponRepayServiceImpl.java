package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

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
    private PaySyncClient paySyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Override
    public void repay(long loanRepayId) {
        logger.debug(MessageFormat.format("[Coupon Repay {0}] coupon repay is starting...", String.valueOf(loanRepayId)));
        LoanRepayModel currentLoanRepayModel = this.loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId(), COUPON_TYPE_LIST);

        for (UserCouponModel userCouponModel : userCouponModels) {
            logger.debug(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) repay is starting...", String.valueOf(loanRepayId), String.valueOf(userCouponModel.getId())));
            InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
            if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS || investModel.getTransferStatus() == TransferStatus.SUCCESS) {
                logger.error(MessageFormat.format("[Coupon Repay {0}] invest({1}) is nonexistent or not success or has transferred",
                        String.valueOf(loanRepayId),
                        investModel == null ? "null" : String.valueOf(investModel.getId())));
                continue;
            }
            CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), currentLoanRepayModel.getPeriod());

            if(couponRepayModel == null){
                logger.error(MessageFormat.format("Coupon Repay loanRepayId:{0},userCouponId:{1},period:{2} is nonexistent",
                                currentLoanRepayModel.getLoanId(),
                                userCouponModel.getId(),
                                currentLoanRepayModel.getPeriod()));
                continue;
            }

            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());

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

            boolean isPaySuccess = transferAmount == 0;
            if (transferAmount > 0) {
                try {
                    TransferRequestModel requestModel = TransferRequestModel.newRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(userCouponModel.getId()), String.valueOf(new Date().getTime())),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                            String.valueOf(transferAmount));
                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                    isPaySuccess = responseModel.isSuccess();
                    logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer status is {2}",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId()),
                            String.valueOf(isPaySuccess)));
                } catch (PayException e) {
                    logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) transfer is failed",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId())), e);
                }
            }

            if (isPaySuccess) {
                try {
                    userCouponModel.setActualInterest(userCouponModel.getActualInterest() + actualInterest);
                    userCouponModel.setActualFee(userCouponModel.getActualFee() + actualFee);
                    userCouponMapper.update(userCouponModel);

                    couponRepayModel.setActualInterest(actualInterest);
                    couponRepayModel.setActualFee(actualFee);
                    couponRepayModel.setActualRepayDate(currentLoanRepayModel.getActualRepayDate());
                    couponRepayModel.setStatus(RepayStatus.COMPLETE);
                    couponRepayMapper.update(couponRepayModel);
                    amountTransfer.transferInBalance(userCouponModel.getLoginName(),
                            userCouponModel.getId(),
                            actualInterest,
                            couponModel.getCouponType().getUserBillBusinessType(), null, null);

                    amountTransfer.transferOutBalance(userCouponModel.getLoginName(),
                            userCouponModel.getId(),
                            actualFee,
                            UserBillBusinessType.INVEST_FEE, null, null);

                    String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                            couponModel.getCouponType().getName(),
                            String.valueOf(userCouponModel.getId()),
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(transferAmount));

                    systemBillService.transferOut(userCouponModel.getId(), transferAmount, SystemBillBusinessType.COUPON, detail);

                    logger.info(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill and system bill is success",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId())));
                } catch (Exception e) {
                    logger.error(MessageFormat.format("[Coupon Repay {0}] user coupon({1}) update user bill is failed",
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(userCouponModel.getId())), e);
                }
            }
        }

        logger.info(MessageFormat.format("[Coupon Repay {0}] coupon repay is done", String.valueOf(loanRepayId)));
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

        List<CouponRepayModel> couponRepayModels = Lists.newArrayList();

        for (int index = 0; index < totalPeriods; index++) {
            int period = index + 1;
            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            Date currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration).toDate();
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
                        logger.error(MessageFormat.format("(coupon is exist (couponId = {0}))", userCouponModel.getCouponId()));
                        continue;
                    }

                    long expectedCouponInterest = InterestCalculator.estimateCouponExpectedInterest(successInvestModel.getAmount(),
                            loanModel, couponModel);
                    long expectedFee = new BigDecimal(expectedCouponInterest).setScale(0, BigDecimal.ROUND_DOWN)
                            .multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();

                    couponRepayModels.add(new CouponRepayModel(successInvestModel.getLoginName(),
                            couponModel.getId(),
                            userCouponModel.getId(),
                            successInvestModel.getId(),
                            expectedCouponInterest,
                            expectedFee,
                            period,
                            currentRepayDate
                    ));
                }

            }
        }
        couponRepayMapper.create(couponRepayModels);
    }
}