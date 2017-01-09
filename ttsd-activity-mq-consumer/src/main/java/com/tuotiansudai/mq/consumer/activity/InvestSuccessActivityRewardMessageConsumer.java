package com.tuotiansudai.mq.consumer.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.InvestRewardMapper;
import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.activity.repository.model.InvestRewardModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class InvestSuccessActivityRewardMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityRewardMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

    @Autowired
    private AnnualPrizeMapper annualPrizeMapper;

    @Autowired
    private InvestRewardMapper investRewardMapper;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "圣诞专享";

    final static private long INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID = 323L;

    final static private long INVEST_LIMIT = 3000000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    @Value("#{'${activity.annual.period}'.split('\\~')}")
    private List<String> annualTime = Lists.newArrayList();

    @Value("#{'${activity.spring.festival.period}'.split('\\~')}")
    private List<String> springFestivalTime = Lists.newArrayList();

    final private static String LOAN_ANNUAL_ACTIVITY_DESCRIPTION = "新年专享";

    final static private long INVEST_20_RED_ENVELOPE_LIMIT = 500000L;

    final static private long INVEST_800_RED_ENVELOPE_LIMIT = 30000000L;

    final static private long INTEREST_COUPON_OF_20_COUPON_ID = 330;

    final static private long INTEREST_COUPON_OF_800_COUPON_ID = 331;

    final static private List<Long> springFestivalTasks = Lists.newArrayList(100000L, 500000L, 1200000L, 3000000L);

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityReward;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            this.assignActivityChristmasInterestCoupon(message);
            this.assignActivityAnnualInvestReward(message);
            this.assignActivitySpringFestivalInvestReward(message);
        }
    }

    private void assignActivityChristmasInterestCoupon(String message) {
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Date nowDate = DateTime.now().toDate();

        logger.info("[MQ] ready to consume activity message: assigning coupon.");
        if ((activityChristmasStartTime.before(nowDate) && activityChristmasEndTime.after(nowDate))
                && investSuccessMessage.getLoanDetailInfo().isActivity() && investSuccessMessage.getLoanDetailInfo().getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)
                && (!investSuccessMessage.getInvestInfo().getTransferStatus().equals("SUCCESS") && investSuccessMessage.getInvestInfo().getStatus().equals("SUCCESS"))
                && investSuccessMessage.getInvestInfo().getAmount() >= INVEST_LIMIT) {

            mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID);
        }
    }

    private void assignActivityAnnualInvestReward(String message) {
        logger.info("[MQ] assign annual reward begin.");
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Date nowDate = DateTime.now().toDate();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();
        UserInfo userInfo = investSuccessMessage.getUserInfo();

        logger.info("[MQ] ready to consume activity annual message: invest reward.");
        Date startTime = DateTime.parse(annualTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(annualTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        if ((startTime.before(nowDate) && endTime.after(nowDate))
                && loanDetailInfo.isActivity() && loanDetailInfo.getActivityDesc().equals(LOAN_ANNUAL_ACTIVITY_DESCRIPTION)
                && (!investInfo.getTransferStatus().equals("SUCCESS") && investInfo.getStatus().equals("SUCCESS"))) {

            AnnualPrizeModel annualPrizeModel = annualPrizeMapper.findByMobile(userInfo.getMobile());
            boolean firstSendCoupon = false;
            boolean secondSendCoupon = false;
            if (null != annualPrizeModel) {
                annualPrizeModel.setInvestAmount(annualPrizeModel.getInvestAmount() + investInfo.getAmount());
                logger.info(MessageFormat.format("[MQ] annual prize is already exits. firstSendCount:{0}, SecondSendCoupon:{1}, investAmount:{2}", annualPrizeModel.isFirstSendCoupon(), annualPrizeModel.isSecondSendCoupon(), annualPrizeModel.getInvestAmount()));

                if (!annualPrizeModel.isFirstSendCoupon() && annualPrizeModel.getInvestAmount() >= INVEST_20_RED_ENVELOPE_LIMIT) {
                    firstSendCoupon = true;
                    annualPrizeModel.setFirstSendCoupon(firstSendCoupon);
                }

                if (!annualPrizeModel.isSecondSendCoupon() && annualPrizeModel.getInvestAmount() >= INVEST_800_RED_ENVELOPE_LIMIT) {
                    secondSendCoupon = true;
                    annualPrizeModel.setSecondSendCoupon(secondSendCoupon);
                }

                annualPrizeMapper.update(annualPrizeModel);
            } else {
                logger.info(MessageFormat.format("[MQ] annual prize is not exits. firstSendCount:{0}, SecondSendCoupon:{1}, investAmount:{2}", firstSendCoupon, secondSendCoupon, investInfo.getAmount()));

                if (investInfo.getAmount() >= INVEST_20_RED_ENVELOPE_LIMIT) {
                    firstSendCoupon = true;
                }

                if (investInfo.getAmount() >= INVEST_800_RED_ENVELOPE_LIMIT) {
                    secondSendCoupon = true;
                }

                annualPrizeModel = new AnnualPrizeModel(userInfo.getLoginName(), userInfo.getUserName(), userInfo.getMobile(), investInfo.getAmount(), firstSendCoupon, secondSendCoupon);
                annualPrizeMapper.create(annualPrizeModel);
            }

            if (firstSendCoupon) {
                logger.info(MessageFormat.format("[MQ] execute first coupon assign coupon . loginName:{0}, couponId:{1}", investSuccessMessage.getInvestInfo().getLoginName(), INTEREST_COUPON_OF_20_COUPON_ID));
                mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_20_COUPON_ID);
            }

            if (secondSendCoupon) {
                logger.info(MessageFormat.format("[MQ] execute second coupon assign coupon . loginName:{0}, couponId:{1}", investSuccessMessage.getInvestInfo().getLoginName(), INTEREST_COUPON_OF_20_COUPON_ID));
                mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_800_COUPON_ID);
            }
        }

        logger.info("[MQ] assign annual reward end.");
    }

    private void assignActivitySpringFestivalInvestReward(String message) {
        logger.info("[MQ] assign springFestival reward begin.");
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Date nowDate = DateTime.now().toDate();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();
        UserInfo userInfo = investSuccessMessage.getUserInfo();

        logger.info("[MQ] ready to consume activity springFestival message: invest reward.");
        Date startTime = DateTime.parse(springFestivalTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(springFestivalTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Long investGrade;
        int currentGrade = 1;
        InvestRewardModel investRewardModel;
        final String loginName = investSuccessMessage.getInvestInfo().getLoginName();
        if (userInfo != null && (startTime.before(nowDate) && endTime.after(nowDate))
                && loanDetailInfo.isActivity() && (!investInfo.getTransferStatus().equals("SUCCESS") && investInfo.getStatus().equals("SUCCESS"))) {
            investRewardModel = investRewardMapper.findByMobile(userInfo.getMobile());
            if (null != investRewardModel) {
                investRewardModel.setInvestAmount(investRewardModel.getInvestAmount() + investInfo.getAmount());
                logger.info(MessageFormat.format("[MQ] springFestival reward is exits. investAmount:{0}, currentGrade:{1}", investInfo.getAmount(), investRewardModel.getRewardGrade()));
                investGrade = getInvestTaskGrade(investInfo.getAmount());
                currentGrade += investRewardModel.getRewardGrade();
                if(investGrade != investRewardModel.getRewardGrade()){
                    investRewardModel.setRewardGrade(investGrade);
                }

                investRewardMapper.update(investRewardModel);
            } else {
                logger.info(MessageFormat.format("[MQ] springFestival reward is not exits. investAmount:{0}", investInfo.getAmount()));
                investGrade = getInvestTaskGrade(investInfo.getAmount());
                investRewardModel = new InvestRewardModel(userInfo.getLoginName(), userInfo.getUserName(), userInfo.getMobile(), investInfo.getAmount(), investGrade);
                investRewardMapper.create(investRewardModel);
            }

            getInvestReward(currentGrade, investGrade).stream().forEach(investReward -> {
                logger.info(MessageFormat.format("[MQ] execute second coupon assign coupon . loginName:{0}, couponId:{1}", loginName, investReward));
                mqClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":" + investReward);
            });
        }
    }

    private Long getInvestTaskGrade(long investAmount) {
        Long investGrade = 0L;
        for (Long taskAmount : springFestivalTasks) {
            if (investAmount >= taskAmount) {
                investGrade = getInvestGradeByInvestAmount(taskAmount);
                continue;
            }
            break;
        }
        return investGrade;
    }

    private Long getInvestGradeByInvestAmount(Long taskAmount) {
        return Maps.newHashMap(ImmutableMap.<Long, Long>builder()
                .put(100000L, 1L)
                .put(500000L, 2L)
                .put(1200000L, 3L)
                .put(3000000L, 4L)
                .build()).get(taskAmount);
    }

    private List getInvestRewardByInvestGrade(Integer investGrade) {
        return Maps.newHashMap(ImmutableMap.<Integer, List>builder()
                .put(1, Lists.newArrayList(341L, 342L))
                .put(2, Lists.newArrayList(343L, 344L))
                .put(3, Lists.newArrayList(345L, 346L, 347L, 348L))
                .put(4, Lists.newArrayList(349L, 350L, 351L, 352L))
                .build()).get(investGrade);
    }

    private List getInvestReward(int currentGrade, Long investGrade) {
        List investRewardList = Lists.newArrayList();
        for (int i = currentGrade; i <= investGrade; i++) {
            investRewardList.addAll(getInvestRewardByInvestGrade(i));
        }
        return investRewardList;
    }

}
