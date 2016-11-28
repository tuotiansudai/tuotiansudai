package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ChristmasPrizeService {

    private static Logger logger = Logger.getLogger(ChristmasPrizeService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    private static final String redisKey = "web:christmasTime:lottery:startTime";
    private static final String redisHKey = "activityChristmasPrizeStartTime";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int timeout = 60 * 60 * 24 * 20;
    //判断圣诞节活动二是否开启
    public boolean isStart(){
        long investAmount =  (long)getActivityChristmasInvestAmountAndCount().get("investAmount");
        if(investAmount >= 420000000)
            redisWrapperClient.hset(redisKey, redisHKey, sdf.format(new Date()), timeout);
        return investAmount >= 420000000 & redisWrapperClient.exists(redisKey);
    }

    //活动期间投资是否满
    public void assignUserCoupon(String loginName){
        UserModel userModel = userMapper.findByLoginName(loginName);
        if(userModel != null && userModel.getMobile() != null && getActivityChristmasInvestAmountByLoginName(loginName) >= 20000){
            couponAssignmentService.assignUserCoupon(userModel.getMobile(), 322);
        }
    }

    public int getDrawPrizeTime(String mobile) {
        int lotteryTime = 0;
        if (redisWrapperClient.exists(redisKey)) {
            Date activityChristmasPrizeStartTime = DateTime.parse(redisWrapperClient.hget(redisKey, redisHKey), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            UserModel userModel = userMapper.findByMobile(mobile);
            if (userModel == null) {
                return lotteryTime;
            }

            //注册增加一次
            if (userModel.getRegisterTime().before(activityChristmasEndTime) && userModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                lotteryTime++;
            }

            //实名认证增加一次
            AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
            if (accountModel != null && accountModel.getRegisterTime().before(activityChristmasEndTime) && accountModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                lotteryTime++;
            }

            //首次投资和邀请好友
            List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(activityChristmasPrizeStartTime, activityChristmasEndTime, userModel.getLoginName());
            for (UserModel referrerUserModel : userModels) {
                if (referrerUserModel.getRegisterTime().before(activityChristmasEndTime) && referrerUserModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                    lotteryTime++;
                    if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), activityChristmasPrizeStartTime, activityChristmasEndTime) > 0) {
                        lotteryTime++;
                    }
                }
            }

            //首次投资
            if (investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), activityChristmasPrizeStartTime, activityChristmasEndTime) == 1) {
                lotteryTime++;
            }

            long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.CHRISTMAS_ACTIVITY, null, null);
            if (lotteryTime > 0) {
                lotteryTime -= userTime;
            }
        }
        return lotteryTime;
    }

    public DrawLotteryResultDto drawLotteryPrize(String mobile){
        logger.debug(mobile + " is drawing the lottery prize.");

        Date nowDate = DateTime.now().toDate();
        if(!nowDate.before(activityChristmasEndTime) || !nowDate.after(activityChristmasStartTime)){
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        if (StringUtils.isEmpty(mobile)) {
            logger.debug("User not login. can't draw prize.");
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来拆奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            logger.debug(mobile + "User is not found.");
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        int drawTime = getDrawPrizeTime(mobile);
        if(drawTime <= 0){
            logger.debug(mobile + "is no chance. draw time:" + drawTime);
            return new DrawLotteryResultDto(1);//您暂无拆奖机会，赢取机会后再来抽奖吧！
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        LotteryPrize christmasPrize = getLotteryPrize();
        PrizeType prizeType = PrizeType.CONCRETE;
        if(christmasPrize.equals(LotteryPrize.RED_ENVELOPE_20_POINT_DRAW_REF_CARNIVAL)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(christmasPrize));
            prizeType = PrizeType.VIRTUAL;
        }

        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile,
                userModel.getLoginName(),
                Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getUserName(),
                christmasPrize,
                DateTime.now().toDate(),
                ActivityCategory.CHRISTMAS_ACTIVITY));
        return new DrawLotteryResultDto(0, christmasPrize.name(), prizeType.name(), christmasPrize.getDescription());
    }

    private long getCouponId(LotteryPrize lotteryPrize){
        switch (lotteryPrize){
            case RED_ENVELOPE_20_POINT_DRAW_REF_CARNIVAL :
                return 323;
        }
        return 0l;
    }

    private LotteryPrize getLotteryPrize(){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 100;
        if (mod >= 0 && mod <= 2){
            return LotteryPrize.FLOWER_CUP;
        } else if (mod >= 3 && mod <= 5){
            return LotteryPrize.CINEMA_TICKET;
        } else if (mod >= 6 && mod <= 9){
            return LotteryPrize.IQIYI_MEMBERSHIP;
        } else if (mod >= 10 && mod <= 14){
            return LotteryPrize.TELEPHONE_FARE_10;
        } else if (mod >= 15 && mod <= 44){
            return LotteryPrize.RED_INVEST_50;
        }else if (mod >= 45 && mod <= 74){
            return LotteryPrize.RED_INVEST_15;
        }else{
            return LotteryPrize.MEMBERSHIP_V5;
        }
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile){
        if(Strings.isNullOrEmpty(mobile)){
            return Lists.newArrayList();
        }
        return findDrawLotteryPrizeRecord(mobile);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile){
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, ActivityCategory.CHRISTMAS_ACTIVITY);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(MobileEncryptor.encryptWebMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }

    public Map getActivityChristmasInvestAmountAndCount(){
        List<InvestModel> investModels = investMapper.countSuccessInvestByInvestTime(null, activityChristmasStartTime, activityChristmasEndTime);
        Map<String,Object> param = Maps.newConcurrentMap();
        long amount = 0l;
        long count = 0l;
        Map<String,String> userMap = Maps.newConcurrentMap();
        for(InvestModel investModel : investModels){
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
            if(loanDetailsModel != null && loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals("圣诞专享")){
                amount += investModel.getAmount();
                if(userMap.get(investModel.getLoginName()) == null){
                    userMap.put(investModel.getLoginName(),investModel.getLoginName());
                    count ++;
                }
            }
        }
        param.put("investAmount",amount);
        param.put("investCount",count);
        return param;
    }

    public long getActivityChristmasInvestAmountByLoginName(String loginName){
        List<InvestModel> investModels = investMapper.countSuccessInvestByInvestTimeAndLoginName(loginName, activityChristmasStartTime, activityChristmasEndTime);
        long amount = 0l;
        for(InvestModel investModel: investModels){
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
            if(loanDetailsModel != null && loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals("圣诞专享")){
                amount += investModel.getAmount();
            }
        }
        return amount;
    }

    public static void main(String args[]){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RedisWrapperClient redisWrapperClient = new RedisWrapperClient();
        redisWrapperClient.hset(redisKey, redisHKey, sdf.format(new Date()), timeout);
        System.out.println("sssf = "  + redisWrapperClient.hget(redisKey, redisHKey));


        for(int i = 1; i < 100; i++){
            int random = (int) (Math.random() * 100000000);
            int mod = random % 100;
            System.out.println("第" + i + "次 random = " + random + "  " + mod);
        }

    }
}
