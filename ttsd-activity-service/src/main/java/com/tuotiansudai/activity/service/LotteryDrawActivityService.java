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
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.MobileEncryptor;
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

    @Value("#{'${activity.spring.festival.period}'.split('\\~')}")
    private List<String> springFestivalTime = Lists.newArrayList();

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

        int drawTime = countDrawLotteryTime(mobile, activityCategory);
        if (drawTime <= 0) {
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        LotteryPrize lotteryPrize = drawLotteryPrize(activityCategory);
        if (lotteryPrize.getPrizeType().equals(PrizeType.VIRTUAL)) {
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        } else if (lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)) {
            createUserMembershipModel(userModel.getLoginName(), MembershipLevel.V5.getLevel());
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
        if(!longTermActivity){
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

        grantPrize(mobile, userModel.getLoginName(), lotteryPrize);

        try {
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
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, activityCategory);
        for (UserLotteryPrizeView view : userLotteryPrizeViews) {
            view.setMobile(MobileEncryptor.encryptWebMiddleMobile(view.getMobile()));
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

    private void createPointBillModel(String loginName, int point, LotteryPrize lotteryPrize) {
        PointBillModel pointBillModel = new PointBillModel(loginName,
                null,
                point,
                PointBusinessType.LOTTERY,
                MessageFormat.format("抽中{0}", lotteryPrize.getDescription()));
        pointBillMapper.create(pointBillModel);
    }

    private long getCouponId(LotteryPrize lotteryPrize) {
        return Maps.newHashMap(ImmutableMap.<LotteryPrize, Long>builder()
                .put(LotteryPrize.RED_ENVELOPE_100, 305L)
                .put(LotteryPrize.RED_ENVELOPE_50, 305L)
                .put(LotteryPrize.INTEREST_COUPON_5, 307L)
                .put(LotteryPrize.INTEREST_COUPON_2, 308L)
                .put(LotteryPrize.RED_INVEST_15, 309L)
                .put(LotteryPrize.RED_INVEST_50, 310L)
                .put(LotteryPrize.RED_ENVELOPE_10, 311L)
                .put(LotteryPrize.RED_ENVELOPE_50_POINT_DRAW, 312L)
                .put(LotteryPrize.INTEREST_COUPON_5_POINT_DRAW, 313L)
                .put(LotteryPrize.INTEREST_COUPON_2_POINT_DRAW, 314L)
                .put(LotteryPrize.INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL, 321L)
                .put(LotteryPrize.RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL, 320L)
                .put(LotteryPrize.RED_ENVELOPE_5, 325L)
                .put(LotteryPrize.RED_ENVELOPE_3, 326L)
                .put(LotteryPrize.RED_ENVELOPE_18, 327L)
                .put(LotteryPrize.RED_ENVELOPE_8, 328L)
                .put(LotteryPrize.INTEREST_COUPON_2_NEW_YEARS, 329L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_68, 340L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_58, 341L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_38, 342L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_18, 343L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_88, 344L)
                .put(LotteryPrize.SPRING_FESTIVAL_RED_ENVELOP_188, 345L)
                .put(LotteryPrize.SPRING_FESTIVAL_INTEREST_COUPON_5, 346L)
                .put(LotteryPrize.SPRING_FESTIVAL_INTEREST_COUPON_2, 347L)
                .put(LotteryPrize.POINT_SHOP_RED_ENVELOPE_10, 355L)
                .put(LotteryPrize.POINT_SHOP_RED_ENVELOPE_50, 356L)
                .put(LotteryPrize.POINT_SHOP_INTEREST_COUPON_2, 357L)
                .put(LotteryPrize.POINT_SHOP_INTEREST_COUPON_5, 358L)
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
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        } else if (lotteryPrize.equals(LotteryPrize.RED_INVEST_15) || lotteryPrize.equals(LotteryPrize.RED_INVEST_50)) {
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
            prizeType = PrizeType.VIRTUAL;
        } else if (lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)) {
            prizeType = PrizeType.MEMBERSHIP;
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
        }
        else if (lotteryPrize.equals(LotteryPrize.POINT_SHOP_POINT_500)) {
            prizeType = PrizeType.POINT;
            createPointBillModel(loginName, 500, lotteryPrize);
        }
        else if (lotteryPrize.equals(LotteryPrize.POINT_SHOP_POINT_3000)) {
            prizeType = PrizeType.POINT;
            createPointBillModel(loginName, 3000, lotteryPrize);
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
            case SPRING_FESTIVAL_ACTIVITY:
                return countDrawLotteryTime(userModel, activityCategory, springFestivalTasks);
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
                    List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserModel referrerUserModel : userModels) {
                        if (referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)) {
                            time++;
                        }
                    }
                    break;
                case EACH_REFERRER_INVEST:
                    List<UserModel> referrerUserModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    for (UserModel referrerUserModel : referrerUserModels) {
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
                    if (rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case INVEST:
                    if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), startTime, endTime) > 0) {
                        time++;
                    }
                    break;
                case EACH_ACTIVITY_SIGN_IN:
                    time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), startTime, endTime, Lists.newArrayList(PointBusinessType.SIGN_IN));
                    break;
                case TODAY_ACTIVITY_SIGN_IN:
                    if(DateTime.now().toDate().before(endTime) && DateTime.now().toDate().after(startTime)){
                        time += pointBillMapper.findCountPointBillPagination(userModel.getLoginName(), DateTime.now().withTimeAtStartOfDay().toDate(),
                                DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate(), Lists.newArrayList(PointBusinessType.SIGN_IN));

                        return time > 0 ? time - userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, activityCategory,
                                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate()) : time;
                    }
                    break;
                case REFERRER_USER:
                    List<UserModel> referrerUsers = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, userModel.getLoginName());
                    time += referrerUsers.size() * 5;
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
                .put(ActivityCategory.SPRING_FESTIVAL_ACTIVITY, springFestivalTime)
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

    public int toDayIsDrawByMobile(String mobile) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, ActivityCategory.SPRING_FESTIVAL_ACTIVITY,
                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate());
    }

}
