package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.message.LoanOutMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class LoanOutSuccessGenerateRepayMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessGenerateRepayMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    public static final List<CouponType> COUPON_TYPE_LIST = Lists.newArrayList(CouponType.NEWBIE_COUPON,
            CouponType.INVEST_COUPON,
            CouponType.INTEREST_COUPON,
            CouponType.BIRTHDAY_COUPON);

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateRepay;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutMessage loanOutMessage;
            try {
                loanOutMessage = JsonConverter.readValue(message, LoanOutMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutMessage.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[MQ] ready to consume message: generateRepay is execute, loanId:{0}", loanId);
            try {
                generateRepay(loanId);
            } catch (Exception e) {
                fatalSmsList.add("生成标的回款计划失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess generateRepay is fail. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info(MessageFormat.format("[MQ] generateCouponRepay is execute , (loanId : {0}) ", String.valueOf(loanId)));
            try {
                generateCouponRepay(loanId);
            } catch (Exception e) {
                fatalSmsList.add("生成优惠券回款计划失败");
                logger.error(MessageFormat.format("loan out : generate coupon payment fail, (loanId : {0})", String.valueOf(loanId)), e);
                return;
            }

            logger.info("[MQ] ready to consume message: rateIncreases is execute, loanId:{0}", loanId);
            if (!rateIncreases(loanId)) {
                fatalSmsList.add("阶梯加息错误");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess rateIncreases is fail. loanId:{0}", String.valueOf(loanId)));
            }


            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_GenerateRepay fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_GenerateRepay success.");
        }
    }

    private void generateRepay(long loanId) {
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0}", String.valueOf(loanId)));
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        LoanModel loanModel = loanMapper.findById(loanId);
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        boolean isPeriodUnitDay = LoanPeriodUnit.DAY == loanModel.getType().getLoanPeriodUnit();
        int totalPeriods = loanModel.getPeriods();
        DateTime lastRepayDate = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().minusSeconds(1);
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate begin", String.valueOf(loanId)));
        for (int index = 0; index < totalPeriods; index++) {
            logger.info(MessageFormat.format("[Generate_Repay:] loanRepay generate loanId:{0} period:{1} begin", String.valueOf(loanId), index + 1));
            int period = index + 1;

            int currentPeriodDuration = isPeriodUnitDay ? loanModel.getDuration() : InterestCalculator.DAYS_OF_MONTH;
            DateTime currentRepayDate = lastRepayDate.plusDays(currentPeriodDuration);

            long currentPeriodCorpus = 0;
            for (InvestModel successInvestModel : successInvestModels) {
                logger.info(MessageFormat.format("[Generate_Repay:]investRepay generate loanId:{0},investId:{1} period:{2}  end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
                long expectedInvestInterest = InterestCalculator.calculateInvestRepayInterest(loanModel, successInvestModel, lastRepayDate, currentRepayDate);
                long expectedFee = new BigDecimal(expectedInvestInterest).setScale(0, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(successInvestModel.getInvestFeeRate())).longValue();

                InvestRepayModel investRepayModel = new InvestRepayModel(idGenerator.generate(),
                        successInvestModel.getId(),
                        period,
                        period == totalPeriods ? successInvestModel.getAmount() : 0,
                        expectedInvestInterest,
                        expectedFee,
                        currentRepayDate.toDate(),
                        RepayStatus.REPAYING);
                currentPeriodCorpus += investRepayModel.getCorpus();
                if (investRepayMapper.findByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod()) == null) {
                    investRepayModels.add(investRepayModel);
                } else {
                    logger.info(MessageFormat.format("[Generate_Repay:]investRepay is exist loanId:{0},investId:{1} period:{2}  end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
                }
                logger.info(MessageFormat.format("[Generate_Repay:]investRepay generate repeat loanId:{0},investId:{1} period:{2}  end", String.valueOf(loanId), String.valueOf(successInvestModel.getId()), period));
            }
            long expectedLoanInterest = InterestCalculator.calculateLoanRepayInterest(loanModel, successInvestModels, lastRepayDate, currentRepayDate);
            LoanRepayModel loanRepayModel = new LoanRepayModel(idGenerator.generate(), loanModel.getId(), period, currentPeriodCorpus, expectedLoanInterest, currentRepayDate.toDate(), RepayStatus.REPAYING);
            loanRepayModel.setCorpus(currentPeriodCorpus);

            if (loanRepayMapper.findByLoanIdAndPeriod(loanId, period) == null) {
                loanRepayModels.add(loanRepayModel);
            } else {
                logger.error(MessageFormat.format("[Generate_Repay:] Loan Repay is exist (loanId = {0}, period = {1})", String.valueOf(loanRepayModel.getLoanId()), String.valueOf(loanRepayModel.getPeriod())));
            }
            lastRepayDate = currentRepayDate;
            logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepay generate {1} period end", String.valueOf(loanId), index + 1));
        }
        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} begin",
                String.valueOf(loanId), loanRepayModels == null ? 0 : loanRepayModels.size(), investRepayModels == null ? 0 : investRepayModels.size()));

        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(loanRepayModels)) {
            loanRepayMapper.create(loanRepayModels);
        }
        if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(investRepayModels)) {
            investRepayMapper.create(investRepayModels);
        }

        logger.info(MessageFormat.format("[Generate_Repay:] loanId:{0} loanRepayModels size:{1} investRepayModels size:{2} end",
                String.valueOf(loanId), loanRepayModels == null ? 0 : loanRepayModels.size(), investRepayModels == null ? 0 : investRepayModels.size()));
    }

    private void generateCouponRepay(long loanId) {
        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(loanId);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(successInvestModels)) {
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

    private boolean rateIncreases(long loanId) {
        List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanId);
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(extraLoanRateModels)) {
            return false;
        }
        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanId);

        LoanModel loanModel = loanMapper.findById(loanId);
        Date repayDate = new DateTime(loanModel.getRecheckTime()).plusDays(loanModel.getDuration()).toDate();
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : investModels) {
            for (ExtraLoanRateModel extraLoanRateModel : extraLoanRateModels) {
                if (investExtraRateMapper.findByInvestId(investModel.getId()) != null) {
                    continue;
                }

                if ((extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount() && investModel.getAmount() < extraLoanRateModel.getMaxInvestAmount()) ||
                        (extraLoanRateModel.getMaxInvestAmount() == 0 && extraLoanRateModel.getMinInvestAmount() <= investModel.getAmount())) {
                    InvestExtraRateModel investExtraRateModel = new InvestExtraRateModel();
                    investExtraRateModel.setLoanId(loanId);
                    investExtraRateModel.setInvestId(investModel.getId());
                    investExtraRateModel.setAmount(investModel.getAmount());
                    investExtraRateModel.setLoginName(investModel.getLoginName());
                    investExtraRateModel.setExtraRate(extraLoanRateModel.getRate());
                    investExtraRateModel.setRepayDate(repayDate);

                    long expectedInterest = InterestCalculator.calculateExtraLoanRateInterest(loanModel, extraLoanRateModel.getRate(), investModel, repayDate);
                    investExtraRateModel.setExpectedInterest(expectedInterest);

                    long expectedFee = new BigDecimal(investModel.getInvestFeeRate())
                            .multiply(new BigDecimal(expectedInterest))
                            .setScale(0, BigDecimal.ROUND_DOWN)
                            .longValue();
                    investExtraRateModel.setExpectedFee(expectedFee);

                    Source investSource;
                    if ("IOS".equals(investModel.getSource().name()) || "ANDROID".equals(investModel.getSource().name()) || "MOBILE".equals(investModel.getSource().name())) {
                        investSource = Source.MOBILE;
                    } else if ("WEB".equals(investModel.getSource().name())) {
                        investSource = Source.WEB;
                    } else {
                        investSource = Source.AUTO;
                    }

                    if (!org.apache.commons.collections4.CollectionUtils.isEmpty(loanDetailsModel.getExtraSource()) && loanDetailsModel.getExtraSource().contains(investSource)) {
                        investExtraRateMapper.create(investExtraRateModel);
                    }

                    logger.info(MessageFormat.format("[标的放款]创建阶梯加息 loanId:{0},investId:{1}", loanId, investModel.getId()));
                }
            }
        }
        return true;
    }
}
