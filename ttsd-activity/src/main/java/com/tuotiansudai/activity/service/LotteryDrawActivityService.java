package com.tuotiansudai.activity.service;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.dto.ActivityCategory;
import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.PrizeType;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.util.RandomUtils;
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
import java.util.Map;

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
    private RandomUtils randomUtils;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private BindBankCardService bindBankCardService;


    @Value("#{'${activity.point.draw.period}'.split('\\~')}")
    private List<String> pointTime = Lists.newArrayList();


    @Value("#{'${activity.carnival.period}'.split('\\~')}")
    private List<String> carnivalTime = Lists.newArrayList();

    @Transactional
    public synchronized DrawLotteryResultDto drawPrizeByCompleteTaskCount(String mobile, ActivityCategory activityCategory){
        Date nowDate = DateTime.now().toDate();
        List<String> activityTime = getActivityTime(activityCategory);
        Date activityStartTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date activityEndTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        if(!nowDate.before(activityEndTime) || !nowDate.after(activityStartTime)){
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        if (StringUtils.isEmpty(mobile)) {
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        int drawTime = getDrawPrizeTime(mobile, activityCategory);
        if(drawTime <= 0){
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return new DrawLotteryResultDto(1);//该用户不存在！
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        LotteryPrize lotteryPrize = lotteryDrawPrize(activityCategory);
        if(lotteryPrize.getActivityCategory().equals(PrizeType.VIRTUAL)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        try{
            pointBillService.createPointBill(userModel.getLoginName(), null, PointBusinessType.ACTIVITY, (-activityCategory.getConsumeCategory().getPoint()), MessageFormat.format("抽中{0}", lotteryPrize.getDescription()));
            userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), accountModel != null ? accountModel.getUserName() : "", lotteryPrize, DateTime.now().toDate(), activityCategory));
        }catch (Exception e){
            logger.error(MessageFormat.format("draw is fail, mobile:{0},activity:{1}",mobile,activityCategory.getDescription()));
        }

        return new DrawLotteryResultDto(0,lotteryPrize.name(),lotteryPrize.getPrizeType().name(),lotteryPrize.getDescription(),String.valueOf(accountModel.getPoint()));
    }

    @Transactional
    public synchronized DrawLotteryResultDto drawPrizeByPoint(String mobile, ActivityCategory activityCategory){
        Date nowDate = DateTime.now().toDate();
        List<String> activityTime = getActivityTime(activityCategory);
        Date activityStartTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date activityEndTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        if(!nowDate.before(activityEndTime) || !nowDate.after(activityStartTime)){
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        if (Strings.isNullOrEmpty(mobile)) {
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        AccountModel accountModel = accountMapper.lockByLoginName(userModel.getLoginName());

        if(accountModel == null){
            return new DrawLotteryResultDto(4);//您还未实名认证，请实名认证后再来抽奖吧！
        }

        if(accountModel.getPoint() < activityCategory.getConsumeCategory().getPoint()){
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        LotteryPrize lotteryPrize = lotteryDrawPrize(activityCategory);

        if(lotteryPrize.getPrizeType().equals(PrizeType.VIRTUAL)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        }else if(lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)){
            createUserMembershipModel(userModel.getLoginName(), MembershipLevel.V5.getLevel());
        }

        try{
            userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), accountModel != null ? accountModel.getUserName() : "", lotteryPrize, DateTime.now().toDate(), activityCategory));
        }catch (Exception e){
            logger.error(MessageFormat.format("draw is fail, mobile:{0},activity:{1}",mobile,activityCategory.getDescription()));
        }

        return new DrawLotteryResultDto(0,lotteryPrize.name(),lotteryPrize.getPrizeType().name(),lotteryPrize.getDescription(),String.valueOf(accountModel.getPoint()));
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile,ActivityCategory activityCategory){
        return Strings.isNullOrEmpty(mobile) ? Lists.newArrayList() : findDrawLotteryPrizeRecord(mobile,activityCategory);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile,ActivityCategory activityCategory){
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, activityCategory);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(randomUtils.encryptWebMiddleMobile(view.getMobile()));
            view.setPrizeValue(view.getPrize().getDescription());
        }
        return userLotteryPrizeViews;
    }

    private void createUserMembershipModel(String loginName, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).withTime(23,59,59,59).toDate(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }

    private long getCouponId(LotteryPrize lotteryPrize){
        switch (lotteryPrize){
            case RED_ENVELOPE_100 :
                return 305;
            case RED_ENVELOPE_50 :
                return 306;
            case INTEREST_COUPON_5 :
                return 307;
            case INTEREST_COUPON_2 :
                return 308;
            case RED_INVEST_15 :
                return 309;
            case RED_INVEST_50 :
                return 310;
            case RED_ENVELOPE_10 :
                return 311;
            case RED_ENVELOPE_50_POINT_DRAW :
                return 312;
            case INTEREST_COUPON_5_POINT_DRAW :
                return 313;
            case INTEREST_COUPON_2_POINT_DRAW :
                return 314;
            case INTEREST_COUPON_5_POINT_DRAW_REF_CARNIVAL:
                return 310;
            case RED_ENVELOPE_50_POINT_DRAW_REF_CARNIVAL:
                return 313;
        }
        return 0l;
    }

    private LotteryPrize lotteryDrawPrize(final ActivityCategory activityCategory){
        int random = (int) (Math.random() * 100000000);
        final int mod = random % 200;

        List<Probability> probabilityList = Lists.newArrayList();
        double maxIndex = 0;
        for(LotteryPrize lotteryPrize : LotteryPrize.values()){
            if(lotteryPrize.getActivityCategory().equals(activityCategory) && lotteryPrize.getRate() != 0){
                double rate = lotteryPrize.getRate() * 2;
                probabilityList.add(new Probability(lotteryPrize,maxIndex,maxIndex + rate));
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

    public int getDrawPrizeTime(String mobile,ActivityCategory activityCategory){
        List<String> activityTime = getActivityTime(activityCategory);
        Date startTime = DateTime.parse(activityTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(activityTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }

        if(userModel.getRegisterTime().before(startTime) && userModel.getRegisterTime().after(endTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(startTime) && accountModel.getRegisterTime().after(endTime)){
            lotteryTime ++;
        }

        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModels){
            UserModel referrerUserModel = userMapper.findByLoginName(referrerRelationModel.getLoginName());
            if(referrerUserModel.getRegisterTime().before(startTime) && referrerUserModel.getRegisterTime().after(endTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), endTime, startTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(startTime) && bankCardModel.getCreatedTime().after(endTime)){
            lotteryTime ++;
        }

        if(rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, endTime, startTime) > 0){
            lotteryTime ++;
        }

        if(investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), endTime, startTime) > 0){
            lotteryTime ++;
        }

        long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.AUTUMN_PRIZE, null, null);
        if(lotteryTime > 0){
            lotteryTime -= userTime;
        }
        return lotteryTime;
    }

    class Probability{
        LotteryPrize lotteryPrize;
        double minProbability;
        double maxProbability;

        public Probability(LotteryPrize lotteryPrize,double minProbability, double maxProbability) {
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

    private List<String> getActivityTime(ActivityCategory activityCategory){
        return Maps.newHashMap(ImmutableMap.<ActivityCategory, List<String>>builder()
                .put(ActivityCategory.POINT_DRAW_1000, pointTime)
                .put(ActivityCategory.POINT_DRAW_10000, pointTime)
                .put(ActivityCategory.CARNIVAL_ACTIVITY, carnivalTime)
                .build()).get(activityCategory);
    }

    public List<Integer> generateSteps(String loginName) {
        List<Integer> steps = Lists.newArrayList(1, 0, 0, 0, 0);
        if (Strings.isNullOrEmpty(loginName)) {
            return steps;
        }
        steps.set(0, 2);
        if (accountService.findByLoginName(loginName) == null) {
            steps.set(1, 1);
            return steps;
        }
        steps.set(1, 2);
        steps.set(2, 1);
        steps.set(3, 1);
        steps.set(4, 1);
        if (bindBankCardService.getPassedBankCard(loginName) != null) {
            steps.set(2, 2);
        }
        return steps;
    }
}
