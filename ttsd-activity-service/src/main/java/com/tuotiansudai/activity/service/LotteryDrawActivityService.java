package com.tuotiansudai.activity.service;


import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointChangingResult;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LotteryDrawActivityService {

    static Logger logger = Logger.getLogger(LotteryDrawActivityService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private PointBillService pointBillService;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    public final static String ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY = "activity:double:eleven:invest";

    public final static String ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY = "activity:double:eleven:user:invest:count";

    @Value("#{'${activity.point.draw.period}'.split('\\~')}")
    private List<String> pointTime = Lists.newArrayList();

    @Value("#{'${activity.carnival.period}'.split('\\~')}")
    private List<String> carnivalTime = Lists.newArrayList();

    @Value("#{'${activity.annual.period}'.split('\\~')}")
    private List<String> annualTime = Lists.newArrayList();

    @Value("${activity.national.startTime}")
    private String nationalStartTime;

    @Value("${activity.national.endTime}")
    private String nationalEndTime;

    @Value("${activity.autumn.startTime}")
    private String autumnStartTime;

    @Value("${activity.autumn.endTime}")
    private String autumnEndTime;

    @Value(value = "${activity.lanternFestival.startTime}")
    private String lanternFestivalStartTime;

    @Value(value = "${activity.lanternFestival.endTime}")
    private String lanternFestivalEndTime;

    @Value("#{'${activity.spring.festival.period}'.split('\\~')}")
    private List<String> springFestivalTime = Lists.newArrayList();

    @Value(value = "${activity.woman.day.startTime}")
    private String activityWomanDayStartTime;

    @Value(value = "${activity.woman.day.endTime}")
    private String activityWomanDayEndTime;

    @Value(value = "${activity.mothers.day.startTime}")
    private String activityMothersStartTime;

    @Value(value = "${activity.mothers.day.endTime}")
    private String activityMothersEndTime;

    @Value(value = "${activity.celebration.single.startTime}")
    private String activitySingleStartTime;

    @Value(value = "${activity.celebration.single.endTime}")
    private String activitySingleEndTime;

    @Value(value = "${activity.exercise.work.startTime}")
    private String acticityExerciseWorkStartTime;

    @Value(value = "${activity.exercise.work.endTime}")
    private String acticityExerciseWorkEndTime;

    @Value(value = "${activity.house.decorate.startTime}")
    private String acticityHouseDecorateStartTime;

    @Value(value = "${activity.house.decorate.endTime}")
    private String acticityHouseDecorateEndTime;

    @Value(value = "${activity.school.season.startTime}")
    private String activitySchoolSeasonStartTime;

    @Value(value = "${activity.school.season.endTime}")
    private String activitySchoolSeasonEndTime;

    @Value(value = "${activity.iphoneX.startTime}")
    private String activityIphoneXStartTime;

    @Value(value = "${activity.iphoneX.endTime}")
    private String activityIphoneXEndTime;

    @Value(value = "${activity.double.eleven.startTime}")
    private String activityDoubleElevenStartTime;

    @Value(value = "${activity.double.eleven.endTime}")
    private String activityDoubleElevenEndTime;

    @Value(value = "${activity.year.end.awards.startTime}")
    private String activityYearEndAwardsStartTime;

    @Value(value = "${activity.year.end.awards.endTime}")
    private String activityYearEndAwardsEndTime;

    //往期活动任务
    private final List activityTasks = Lists.newArrayList(ActivityDrawLotteryTask.REGISTER, ActivityDrawLotteryTask.EACH_REFERRER,
            ActivityDrawLotteryTask.EACH_REFERRER_INVEST, ActivityDrawLotteryTask.CERTIFICATION, ActivityDrawLotteryTask.BANK_CARD,
            ActivityDrawLotteryTask.RECHARGE, ActivityDrawLotteryTask.INVEST);
    //元旦活动任务
    private final List newYearsActivityTask = Lists.newArrayList(ActivityDrawLotteryTask.EACH_ACTIVITY_SIGN_IN, ActivityDrawLotteryTask.REFERRER_USER,
            ActivityDrawLotteryTask.EACH_INVEST_5000);

    //圣诞活动活动任务
    private final List christmasTasks = Lists.newArrayList(ActivityDrawLotteryTask.REGISTER, ActivityDrawLotteryTask.EACH_REFERRER,
            ActivityDrawLotteryTask.EACH_REFERRER_INVEST, ActivityDrawLotteryTask.CERTIFICATION, ActivityDrawLotteryTask.INVEST,
            ActivityDrawLotteryTask.EACH_INVEST_2000);

    //春节活动任务
    private final List springFestivalTasks = Lists.newArrayList(ActivityDrawLotteryTask.TODAY_ACTIVITY_SIGN_IN);

    public static final String ACTIVITY_DESCRIPTION = "新年专享";

    //每投资5000奖励抽奖次数
    private final long EACH_INVEST_AMOUNT_50000 = 500000L;

    private final long EACH_INVEST_AMOUNT_20000 = 200000L;

    private final long EACH_INVEST_AMOUNT_100000 = 1000000L;

    @Transactional
    public synchronized DrawLotteryResultDto drawPrizeByCompleteTask(String mobile, ActivityCategory activityCategory) {

        if (StringUtils.isEmpty(mobile)) {
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return new DrawLotteryResultDto(2);//该用户不存在！
        }

        Date nowDate = DateTime.now().toDate();
        List<String> activityTime = getActivityTime(activityCategory);
        Date activityStartTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date activityEndTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        if (!nowDate.before(activityEndTime) || !nowDate.after(activityStartTime)) {
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        if (ActivityCategory.DOUBLE_ELEVEN_ACTIVITY == activityCategory) {
            int CurrentUsedDrawTimes = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, activityCategory, new DateTime(new Date()).withTimeAtStartOfDay().toDate(), new DateTime(new Date()).plusDays(1).minusSeconds(1).toDate());
            if (CurrentUsedDrawTimes >= 10) {
                return new DrawLotteryResultDto(4);//您今天的抽奖机会已用完，明天再来抽奖吧！
            }
        }

        int drawTime = countDrawLotteryTime(mobile, activityCategory);
        if (drawTime <= 0) {
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        LotteryPrize lotteryPrize = drawLotteryPrize(activityCategory);
        if (lotteryPrize.getPrizeType().equals(PrizeType.VIRTUAL)) {
            getCouponId(lotteryPrize).stream().forEach(couponId -> couponAssignmentService.assignUserCoupon(mobile, couponId));
        } else if (lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)) {
            createUserMembershipModel(userModel.getLoginName(), MembershipLevel.V5.getLevel());
        } else if (lotteryPrize.getPrizeType().equals(PrizeType.EXPERIENCE)) {
            grantExperience(userModel.getLoginName(), lotteryPrize);
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        try {
            userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), accountModel != null ? userModel.getUserName() : "", lotteryPrize, DateTime.now().toDate(), activityCategory));
        } catch (Exception e) {
            logger.error(MessageFormat.format("draw is fail, mobile:{0},activity:{1}", mobile, activityCategory.getDescription()));
        }

        return new DrawLotteryResultDto(0, lotteryPrize.name(), lotteryPrize.getPrizeType().name(), lotteryPrize.getDescription());
    }

    @Transactional
    public synchronized DrawLotteryResultDto drawPrizeByPoint(String mobile, ActivityCategory activityCategory, boolean longTermActivity) {
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        //长期活动不执行
        if (!longTermActivity) {
            Date nowDate = DateTime.now().toDate();
            List<String> activityTime = getActivityTime(activityCategory);
            Date activityStartTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            Date activityEndTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (!nowDate.before(activityEndTime) || !nowDate.after(activityStartTime)) {
                return new DrawLotteryResultDto(3);//不在活动时间范围内！
            }
        }

        if (Strings.isNullOrEmpty(mobile)) {
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        AccountModel accountModel = accountMapper.lockByLoginName(userModel.getLoginName());

        if (accountModel == null) {
            return new DrawLotteryResultDto(4);//您还未实名认证，请实名认证后再来抽奖吧！
        }

        if (accountModel.getPoint() < activityCategory.getConsumeCategory().getPoint()) {
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }


        LotteryPrize lotteryPrize = drawLotteryPrize(activityCategory);

        PointChangingResult pointChangingResult = pointBillService.createPointBill(userModel.getLoginName(), null, activityCategory.equals(ActivityCategory.POINT_SHOP_DRAW_1000) ? PointBusinessType.POINT_LOTTERY : PointBusinessType.ACTIVITY, (-activityCategory.getConsumeCategory().getPoint()), MessageFormat.format("抽中{0}", lotteryPrize.getDescription()));

        if (pointChangingResult == PointChangingResult.CHANGING_FREQUENTLY) {
            return new DrawLotteryResultDto(5);
        } else if (pointChangingResult == PointChangingResult.CHANGING_FAIL) {
            return new DrawLotteryResultDto(6);
        }

        try {
            grantPrize(mobile, userModel.getLoginName(), lotteryPrize);
            userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), userModel.getUserName(), lotteryPrize, DateTime.now().toDate(), activityCategory));
        } catch (Exception e) {
            logger.error(MessageFormat.format("draw is fail, mobile:{0},activity:{1}", mobile, activityCategory.getDescription()));
        }

        return new DrawLotteryResultDto(0, lotteryPrize.name(), lotteryPrize.getPrizeType().name(), lotteryPrize.getDescription(), String.valueOf(accountModel.getPoint()));
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile, ActivityCategory activityCategory) {
        return Strings.isNullOrEmpty(mobile) ? Lists.newArrayList() : findDrawLotteryPrizeRecord(mobile, activityCategory);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile, ActivityCategory activityCategory) {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, activityCategory, 200);
        for (UserLotteryPrizeView view : userLotteryPrizeViews) {
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
            view.setPrizeValue(view.getPrize().getDescription());
        }
        return userLotteryPrizeViews;
    }

    private void createUserMembershipModel(String loginName, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).withTime(23, 59, 59, 59).toDate(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }

    private List<Long> getCouponId(LotteryPrize lotteryPrize) {
        return Maps.newHashMap(ImmutableMap.<LotteryPrize, List<Long>>builder()
                .put(LotteryPrize.RED_ENVELOPE_100, Lists.newArrayList(305L))
                .put(LotteryPrize.RED_ENVELOPE_50, Lists.newArrayList(305L))
                .put(LotteryPrize.INTEREST_COUPON_5, Lists.newArrayList(307L))
                .put(LotteryPrize.INTEREST_COUPON_2, Lists.newArrayList(308L))
                .put(LotteryPrize.RED_INVEST_15, Lists.newArrayList(309L))
                .put(LotteryPrize.RED_INVEST_50, Lists.newArrayList(310L))
                .put(LotteryPrize.RED_ENVELOPE_10, Lists.newArrayList(311L))
                .put(LotteryPrize.RED_ENVELOPE_50_POINT_DRAW, Lists.newArrayList(312L))
                .put(LotteryPrize.INTEREST_COUPON_5_POINT_DRAW, Lists.newArrayList(313L))
                .put(LotteryPrize.INTEREST_COUPON_2_POINT_DRAW, Lists.newArrayList(314L))
                .put(LotteryPrize.INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL, Lists.newArrayList(321L))
                .put(LotteryPrize.RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL, Lists.newArrayList(320L))
                .put(LotteryPrize.RED_ENVELOPE_5, Lists.newArrayList(325L))
                .put(LotteryPrize.RED_ENVELOPE_3, Lists.newArrayList(326L))
                .put(LotteryPrize.RED_ENVELOPE_18, Lists.newArrayList(327L))
                .put(LotteryPrize.RED_ENVELOPE_8, Lists.newArrayList(328L))
                .put(LotteryPrize.INTEREST_COUPON_2_NEW_YEARS, Lists.newArrayList(329L))
                .put(LotteryPrize.LANTERN_FESTIVAL_RED_ENVELOPE_5, Lists.newArrayList(364L))
                .put(LotteryPrize.LANTERN_FESTIVAL_RED_ENVELOPE_8, Lists.newArrayList(365L))
                .put(LotteryPrize.LANTERN_FESTIVAL_RED_ENVELOPE_10, Lists.newArrayList(366L))
                .put(LotteryPrize.LANTERN_FESTIVAL_RED_ENVELOPE_40, Lists.newArrayList(367L))
                .put(LotteryPrize.LANTERN_FESTIVAL_RED_ENVELOPE_30, Lists.newArrayList(368L))
                .put(LotteryPrize.LANTERN_FESTIVAL_INTEREST_COUPON_5, Lists.newArrayList(369L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_68, Lists.newArrayList(340L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_58, Lists.newArrayList(341L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_38, Lists.newArrayList(342L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_18, Lists.newArrayList(343L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_88, Lists.newArrayList(344L))
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_188, Lists.newArrayList(345L))
                .put(LotteryPrize.SPRING_FESTIVAL_INTEREST_COUPON_5, Lists.newArrayList(346L))
                .put(LotteryPrize.SPRING_FESTIVAL_INTEREST_COUPON_2, Lists.newArrayList(347L))
                .put(LotteryPrize.POINT_SHOP_RED_ENVELOPE_10, Lists.newArrayList(360L))
                .put(LotteryPrize.POINT_SHOP_RED_ENVELOPE_50, Lists.newArrayList(361L))
                .put(LotteryPrize.POINT_SHOP_INTEREST_COUPON_2, Lists.newArrayList(362L))
                .put(LotteryPrize.POINT_SHOP_INTEREST_COUPON_5, Lists.newArrayList(363L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_521, Lists.newArrayList(392L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_580, Lists.newArrayList(393L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_880, Lists.newArrayList(394L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_1000, Lists.newArrayList(395L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_INTEREST_COUPON_2, Lists.newArrayList(396L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_38, Lists.newArrayList(397L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_ENVELOP_38_100, Lists.newArrayList(398L, 397L))
                .put(LotteryPrize.WOMAN_DAY_ACTIVITY_INTEREST_COUPON_5, Lists.newArrayList(399L))
                .put(LotteryPrize.MOTHERS_DAY_ACTIVITY_ENVELOP_5, Lists.newArrayList(409L))
                .put(LotteryPrize.MOTHERS_DAY_ACTIVITY_ENVELOP_10, Lists.newArrayList(410L))
                .put(LotteryPrize.MOTHERS_DAY_ACTIVITY_ENVELOP_20, Lists.newArrayList(411L))
                .put(LotteryPrize.MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_5, Lists.newArrayList(412L))
                .put(LotteryPrize.MOTHERS_DAY_ACTIVITY_INTEREST_COUPON_1, Lists.newArrayList(413L))
                .put(LotteryPrize.CELEBRATION_SINGLE_ACTIVITY_ENVELOP_5, Lists.newArrayList(441L))
                .put(LotteryPrize.CELEBRATION_SINGLE_ACTIVITY_ENVELOP_10, Lists.newArrayList(442L))
                .put(LotteryPrize.CELEBRATION_SINGLE_ACTIVITY_ENVELOP_30, Lists.newArrayList(443L))
                .put(LotteryPrize.CELEBRATION_SINGLE_ACTIVITY_COUPON_5, Lists.newArrayList(444L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_ENVELOP_5, Lists.newArrayList(445L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_ENVELOP_10, Lists.newArrayList(446L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_ENVELOP_20, Lists.newArrayList(447L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_ENVELOP_30, Lists.newArrayList(448L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_COUPON_5, Lists.newArrayList(449L))
                .put(LotteryPrize.EXERCISE_WORK_ACTIVITY_COUPON_8, Lists.newArrayList(450L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_ENVELOP_5, Lists.newArrayList(451L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_ENVELOP_8, Lists.newArrayList(452L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_ENVELOP_10, Lists.newArrayList(453L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_ENVELOP_15, Lists.newArrayList(454L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_COUPON_5, Lists.newArrayList(455L))
                .put(LotteryPrize.HOUSE_DECORATE_ACTIVITY_COUPON_8, Lists.newArrayList(456L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_ENVELOP_2_99, Lists.newArrayList(457L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_ENVELOP_5_99, Lists.newArrayList(458L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_ENVELOP_6_99, Lists.newArrayList(459L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_ENVELOP_9_99, Lists.newArrayList(460L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_ENVELOP_19_99, Lists.newArrayList(461L))
                .put(LotteryPrize.SCHOOL_SEASON_ACTIVITY_COUPON_5, Lists.newArrayList(462L))
                .put(LotteryPrize.IPHONEX_ACTIVITY_ENVELOP_ENVELOP_18, Lists.newArrayList(464L))
                .put(LotteryPrize.IPHONEX_ACTIVITY_ENVELOP_ENVELOP_188, Lists.newArrayList(465L))
                .put(LotteryPrize.IPHONEX_ACTIVITY_ENVELOP_ENVELOP_288, Lists.newArrayList(466L))
                .put(LotteryPrize.IPHONEX_ACTIVITY_ENVELOP_ENVELOP_588, Lists.newArrayList(467L))
                .put(LotteryPrize.IPHONEX_ACTIVITY_ENVELOP_COUPON_5, Lists.newArrayList(468L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_ENVELOP_20, Lists.newArrayList(469L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_ENVELOP_50, Lists.newArrayList(470L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_ENVELOP_100, Lists.newArrayList(471L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_ENVELOP_200, Lists.newArrayList(472L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_INTEREST_COUPON_2, Lists.newArrayList(473L))
                .put(LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_INTEREST_COUPON_5, Lists.newArrayList(474L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_10, Lists.newArrayList(475L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_20, Lists.newArrayList(476L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_50, Lists.newArrayList(477L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_100, Lists.newArrayList(478L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_120, Lists.newArrayList(479L))
                .put(LotteryPrize.YEAR_END_AWARDS_ACTIVITY_ENVELOP_200, Lists.newArrayList(480L))
                .build()).get(lotteryPrize);
    }

    public LotteryPrize drawLotteryPrize(final ActivityCategory activityCategory) {
        int random = (int) (Math.random() * 100000000);
        final int mod = random % 200;

        List<Probability> probabilityList = Lists.newArrayList();
        double maxIndex = 0;
        for (LotteryPrize lotteryPrize : LotteryPrize.values()) {
            if (lotteryPrize.getActivityCategory().equals(activityCategory) && lotteryPrize.getRate() != 0) {
                double rate = lotteryPrize.getRate() * 2;
                probabilityList.add(new Probability(lotteryPrize, maxIndex, maxIndex + rate));
                maxIndex += rate;
            }
        }

        Optional<Probability> probability = Iterators.tryFind(probabilityList.iterator(), input -> {
            if (input.getMinProbability() <= mod && input.getMaxProbability() > mod) {
                return true;
            }
            return false;
        });

        return probability.get().getLotteryPrize();
    }

    private PrizeType grantPrize(String mobile, String loginName, LotteryPrize lotteryPrize) {
        PrizeType prizeType = PrizeType.VIRTUAL;
        if (lotteryPrize.getPrizeType().equals(PrizeType.VIRTUAL)) {
            getCouponId(lotteryPrize).stream().forEach(couponId -> couponAssignmentService.assignUserCoupon(mobile, couponId));
        } else if (lotteryPrize.equals(LotteryPrize.RED_INVEST_15) || lotteryPrize.equals(LotteryPrize.RED_INVEST_50)) {
            getCouponId(lotteryPrize).stream().forEach(couponId -> couponAssignmentService.assignUserCoupon(mobile, couponId));
            prizeType = PrizeType.VIRTUAL;
        } else if (lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)) {
            prizeType = PrizeType.MEMBERSHIP;
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
        } else if (lotteryPrize.equals(LotteryPrize.POINT_SHOP_POINT_500)) {
            prizeType = PrizeType.POINT;

            pointBillService.createPointBill(loginName, null, PointBusinessType.POINT_LOTTERY, 500, MessageFormat.format("抽中{0}", lotteryPrize.getDescription()));
        } else if (lotteryPrize.equals(LotteryPrize.POINT_SHOP_POINT_3000)) {
            prizeType = PrizeType.POINT;
            pointBillService.createPointBill(loginName, null, PointBusinessType.POINT_LOTTERY, 3000, MessageFormat.format("抽中{0}", lotteryPrize.getDescription()));
        }
        return prizeType;
    }


    public int countDrawLotteryTime(String mobile, ActivityCategory activityCategory) {
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) return lotteryTime;

        switch (activityCategory) {
            case AUTUMN_PRIZE:
            case NATIONAL_PRIZE:
            case CARNIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, activityTasks);
            case ANNUAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, newYearsActivityTask);
            case CHRISTMAS_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, christmasTasks);
            case LANTERN_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_1000));
            case SPRING_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, springFestivalTasks);
            case MOTHERS_DAY_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
            case CELEBRATION_SINGLE_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_10000));
            case EXERCISE_WORK_ACTIVITY:
                return getExerciseVSWorkDrawTime(userModel, activityCategory);
            case HOUSE_DECORATE_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
            case SCHOOL_SEASON_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
            case IPHONEX_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_INVEST_10000));
            case DOUBLE_ELEVEN_ACTIVITY:
                return getDoubleElevenDrawTimes(userModel, activityCategory);
            case YEAR_END_AWARDS_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, Lists.newArrayList(ActivityDrawLotteryTask.EACH_EVERY_DAY));
        }
        return lotteryTime;
    }

    private int countDrawLotteryTime(UserModel userModel, ActivityCategory activityCategory, List<ActivityDrawLotteryTask> activityDrawLotteryTasks) {
        int time = 0;
        List<String> activityTime = getActivityTime(activityCategory);
        Date startTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        for (ActivityDrawLotteryTask activityDrawLotteryTask : activityDrawLotteryTasks) {
            switch (activityDrawLotteryTask) {
                case REGISTER:
                    if (userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)) {
                        time++;
                    }
                    break;
                case EACH_REFERRER:
                    List<UserRegisterInfo> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserRegisterInfo referrerUserModel : userModels) {
                        if (referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)) {
                            time++;
                        }
                    }
                    break;
                case EACH_REFERRER_INVEST:
                    List<UserRegisterInfo> referrerUserModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserRegisterInfo referrerUserModel : referrerUserModels) {
                        if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), startTime, endTime) > 0) {
                            time++;
                        }
                    }
                    break;
                case CERTIFICATION:
                    AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
                    if (accountModel != null && accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)) {
                        time++;
                    }
                    break;
                case BANK_CARD:
                    BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
                    if (bankCardModel != null && bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)) {
                        time++;
                    }
                    break;
                case RECHARGE:
                    if (rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime, null) > 0) {
                        time++;
                    }
                    break;
                case INVEST:
                    if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case EACH_ACTIVITY_SIGN_IN:
                    time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), null, startTime, endTime, Lists.newArrayList(PointBusinessType.SIGN_IN));
                    break;
                case TODAY_ACTIVITY_SIGN_IN:
                    if (DateTime.now().toDate().before(endTime) && DateTime.now().toDate().after(startTime)) {
                        time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), null, DateTime.now().withTimeAtStartOfDay().toDate(),
                                DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate(), Lists.newArrayList(PointBusinessType.SIGN_IN));

                        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate()) : time;
                    }
                    break;
                case REFERRER_USER:
                    long referrerUserCount = userMapper.findUserCountByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    time += referrerUserCount * 5;
                    break;
                case EACH_INVEST_5000:
                    long sumInvestAmount = investMapper.sumSuccessActivityInvestAmount(userModel.getLoginName(), ACTIVITY_DESCRIPTION, startTime, endTime);
                    long investAwardTime = sumInvestAmount / EACH_INVEST_AMOUNT_50000;
                    if (investAwardTime <= 10) {
                        time += investAwardTime;
                    } else {
                        time += 10;
                    }
                    break;
                case EACH_INVEST_2000:
                    long sumAmount = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(ProductType._90, ProductType._180, ProductType._360));
                    time += (int) (sumAmount / EACH_INVEST_AMOUNT_20000);
                    time = time >= 10 ? 10 : time;
                    break;
                case EACH_INVEST_1000:
                    time = investMapper.sumDrawCountByLoginName(userModel.getLoginName(), startTime, endTime, 100000);
                    break;

                case EACH_EVERY_DAY:
                    time = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                            DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate()) == 0 ? 1 : 0;
                    return time;
                case EACH_INVEST_10000:
                    List<InvestModel> investModels = investMapper.findSuccessByLoginNameExceptTransferAndTime(userModel.getLoginName(), startTime, endTime);
                    for (InvestModel investModel : investModels) {
                        time += investModel.getAmount() < EACH_INVEST_AMOUNT_100000 ? 0 : Integer.parseInt(String.valueOf(investModel.getAmount() / EACH_INVEST_AMOUNT_100000));
                    }
                    break;
            }
        }

        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory, null, null) : time;
    }

    class Probability {
        LotteryPrize lotteryPrize;
        double minProbability;
        double maxProbability;

        public Probability(LotteryPrize lotteryPrize, double minProbability, double maxProbability) {
            this.lotteryPrize = lotteryPrize;
            this.minProbability = minProbability;
            this.maxProbability = maxProbability;
        }

        public double getMinProbability() {
            return minProbability;
        }

        public double getMaxProbability() {
            return maxProbability;
        }

        public LotteryPrize getLotteryPrize() {
            return lotteryPrize;
        }
    }

    public List<String> getActivityTime(ActivityCategory activityCategory) {
        return Maps.newHashMap(ImmutableMap.<ActivityCategory, List<String>>builder()
                .put(ActivityCategory.POINT_DRAW_1000, pointTime)
                .put(ActivityCategory.POINT_DRAW_10000, pointTime)
                .put(ActivityCategory.CARNIVAL_ACTIVITY, carnivalTime)
                .put(ActivityCategory.ANNUAL_ACTIVITY, annualTime)
                .put(ActivityCategory.NATIONAL_PRIZE, Lists.newArrayList(nationalStartTime, nationalEndTime))
                .put(ActivityCategory.AUTUMN_PRIZE, Lists.newArrayList(autumnStartTime, autumnEndTime))
                .put(ActivityCategory.LANTERN_FESTIVAL_ACTIVITY, Lists.newArrayList(lanternFestivalStartTime, lanternFestivalEndTime))
                .put(ActivityCategory.SPRING_FESTIVAL_ACTIVITY, springFestivalTime)
                .put(ActivityCategory.WOMAN_DAY_ACTIVITY, Lists.newArrayList(activityWomanDayStartTime, activityWomanDayEndTime))
                .put(ActivityCategory.MOTHERS_DAY_ACTIVITY, Lists.newArrayList(activityMothersStartTime, activityMothersEndTime))
                .put(ActivityCategory.CELEBRATION_SINGLE_ACTIVITY, Lists.newArrayList(activitySingleStartTime, activitySingleEndTime))
                .put(ActivityCategory.EXERCISE_WORK_ACTIVITY, Lists.newArrayList(acticityExerciseWorkStartTime, acticityExerciseWorkEndTime))
                .put(ActivityCategory.HOUSE_DECORATE_ACTIVITY, Lists.newArrayList(acticityHouseDecorateStartTime, acticityHouseDecorateEndTime))
                .put(ActivityCategory.SCHOOL_SEASON_ACTIVITY, Lists.newArrayList(activitySchoolSeasonStartTime, activitySchoolSeasonEndTime))
                .put(ActivityCategory.IPHONEX_ACTIVITY, Lists.newArrayList(activityIphoneXStartTime, activityIphoneXEndTime))
                .put(ActivityCategory.DOUBLE_ELEVEN_ACTIVITY, Lists.newArrayList(activityDoubleElevenStartTime, activityDoubleElevenEndTime))
                .put(ActivityCategory.YEAR_END_AWARDS_ACTIVITY, Lists.newArrayList(activityYearEndAwardsStartTime, activityYearEndAwardsEndTime))
                .build()).get(activityCategory);
    }

    public List<Integer> generateSteps(String loginName) {
        List<Integer> steps = Lists.newArrayList(1, 0, 0, 0, 0);
        if (Strings.isNullOrEmpty(loginName)) {
            return steps;
        }
        steps.set(0, 2);
        if (accountMapper.findByLoginName(loginName) == null) {
            steps.set(1, 1);
            return steps;
        }
        steps.set(1, 2);
        steps.set(2, 1);
        steps.set(3, 1);
        steps.set(4, 1);
        if (bankCardMapper.findPassedBankCardByLoginName(loginName) != null) {
            steps.set(2, 2);
        }
        return steps;
    }

    public String getActivityEndTime(ActivityCategory activityCategory) {
        List<String> activityTime = getActivityTime(activityCategory);
        return activityTime.get(1).replaceAll("-", "/");
    }

    public int toDayIsDrawByMobile(String mobile, ActivityCategory activityCategory) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, activityCategory,
                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate());
    }

    private void grantExperience(String loginName, LotteryPrize lotteryPrize) {
        long experienceAmount = 0l;
        if (LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_EXPERIENCE_GOLD_1000.equals(lotteryPrize)) {
            experienceAmount = 100000l;
        }
        if (LotteryPrize.YEAR_END_AWARDS_ACTIVITY_EXPERIENCE_GOLD_500.equals(lotteryPrize)) {
            experienceAmount = 50000l;
        }

        if (experienceAmount == 0) {
            return;
        }

        ExperienceBillBusinessType experienceBillBusinessType = LotteryPrize.DOUBLE_ELEVEN_ACTIVITY_EXPERIENCE_GOLD_1000.equals(lotteryPrize) ? ExperienceBillBusinessType.DOUBLE_ELEVEN : ExperienceBillBusinessType.YEAR_END_AWARDS;

        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                new ExperienceAssigningMessage(loginName, experienceAmount, ExperienceBillOperationType.IN, experienceBillBusinessType));

    }

    public int getExerciseVSWorkDrawTime(UserModel userModel, ActivityCategory activityCategory) {
        int investDrawTime = 0;
        int sumToDayIsDraw = 1;
        List<String> activityTime = getActivityTime(activityCategory);
        DateTime startTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime endTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        List<InvestModel> investModels = investMapper.findSuccessByLoginNameExceptTransferAndTime(userModel.getLoginName(), startTime.toDate(), endTime.toDate());
        for (InvestModel investModel : investModels) {
            investDrawTime += investModel.getAmount() < EACH_INVEST_AMOUNT_100000 ? 0 : Integer.parseInt(String.valueOf(investModel.getAmount() / EACH_INVEST_AMOUNT_100000));
        }
        if (investDrawTime == 0) {
            return toDayIsDrawByMobile(userModel.getMobile(), activityCategory) == 0 ? 1 : 0;
        }

        Date yesterdayDate = DateTime.now().withTimeAtStartOfDay().minusMillis(1).toDate();
        startTime = startTime.withTimeAtStartOfDay();
        while (startTime.toDate().before(yesterdayDate)) {
            sumToDayIsDraw += userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                    startTime.toDate(), startTime.plusDays(1).minusMillis(1).toDate()) == 0 ? 0 : 1;
            startTime = startTime.plusDays(1);
        }
        return sumToDayIsDraw + investDrawTime - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory, null, null);
    }

    public int getDoubleElevenDrawTimes(UserModel userModel, ActivityCategory activityCategory) {
        int investDrawTimes = 0;
        List<String> activityTime = getActivityTime(activityCategory);
        DateTime startTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        DateTime endTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));

        List<InvestModel> investModels = investMapper.findSuccessDoubleElevenActivityByTime(null, startTime.toDate(), endTime.toDate());
        redisWrapperClient.del(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY);
        int count = 0;
        for (InvestModel investModel : investModels) {
            String hkey = MessageFormat.format("{0}:{1}:{2}", investModel.getLoanId(), investModel.getId(), userModel.getLoginName());
            String incrKey = MessageFormat.format("{0}:{1}", userModel.getLoginName(), new DateTime(investModel.getTradingTime()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));
            boolean even = String.valueOf(redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_INVEST_KEY, hkey)).equals("0");
            boolean booleanEvenOfDay = redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey) != null;
            long evenCountOfDay = booleanEvenOfDay ? Long.parseLong(redisWrapperClient.hget(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey)) : 0;
            if (even && evenCountOfDay < 10) {
                if (booleanEvenOfDay) {
                    count++;
                } else {
                    count = 1;
                }
                redisWrapperClient.hset(ACTIVITY_DOUBLE_ELEVEN_USER_INVEST_COUNT_KEY, incrKey, String.valueOf(count));
                investDrawTimes++;
            }
        }

        return investDrawTimes > 0 ? investDrawTimes - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory, null, null) : investDrawTimes;
    }

}
