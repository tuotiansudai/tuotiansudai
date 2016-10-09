package com.tuotiansudai.activity.service;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
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
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LotteryDrawActivityService {

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

    @Value("#{'${activity.point.draw.period}'.split('\\~')}")
    private List<String> activityTime = Lists.newArrayList();

    public DrawLotteryResultDto drawLotteryResultDto(String mobile,ActivityCategory activityCategory){
        Date nowDate = DateTime.now().toDate();
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

        if(accountModel.getPoint() < activityCategory.getPoint()){
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        LotteryPrize lotteryPrize = lotteryDrawPrize(activityCategory);
        if(lotteryPrize.getPrizeType().equals(PrizeType.VIRTUAL)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        }else if(lotteryPrize.equals(LotteryPrize.MEMBERSHIP_V5)){
            createUserMembershipModel(userModel.getLoginName(), MembershipLevel.V5.getLevel());
        }

        if(activityCategory.equals(ActivityCategory.POINT_DRAW_1000) || activityCategory.equals(ActivityCategory.POINT_DRAW_10000)){
            accountModel.setPoint(accountModel.getPoint() - activityCategory.getPoint());
            accountMapper.update(accountModel);
        }

        try {
            userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), accountModel != null ? accountModel.getUserName() : "", lotteryPrize, DateTime.now().toDate(), activityCategory));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new DrawLotteryResultDto(0,lotteryPrize.name(),lotteryPrize.getPrizeType().name(),lotteryPrize.getDescription(),String.valueOf(accountModel.getPoint()));
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile,ActivityCategory activityCategory){
        if(Strings.isNullOrEmpty(mobile)){
            return Lists.newArrayList();
        }
        return findDrawLotteryPrizeRecord(mobile,activityCategory);
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
                new Date(),
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

        Optional<Probability> probability = Iterators.tryFind(probabilityList.iterator(), new Predicate<Probability>() {
            @Override
            public boolean apply(Probability input) {
                if (input.getMinProbability() <= mod && input.getMaxProbability() > mod) {
                    return true;
                }
                return false;
            }
        });

        return probability.get().getLotteryPrize();
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
}
