package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.PrizeType;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private LotteryDrawActivityService lotteryDrawActivityService;

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

    private static final long SINGLE_INVEST_AMOUNT = 3000000L;

    private static final long COUPON_ID = 323L;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final int timeout = 60 * 60 * 24 * 20;
    //判断圣诞节活动二是否开启
    public int isStart(){
        long investAmount =  (long)getActivityChristmasInvestAmountAndCount().get("investAmount");
        if(investAmount >= 420000000 && !redisWrapperClient.exists(redisKey))
            redisWrapperClient.hset(redisKey, redisHKey, sdf.format(new Date()), timeout);
        return investAmount >= 420000000 && redisWrapperClient.exists(redisKey) ? 1 : 0;
    }

    public Date getChristmasPrizeStartTime(){
        return redisWrapperClient.exists(redisKey) ? DateTime.parse(redisWrapperClient.hget(redisKey, redisHKey), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate() : activityChristmasEndTime;
    }

    //活动期间投资圣诞专享标单笔满30000元,奖励一张0.5%的加息劵
    public void assignUserCoupon(String loginName){
        UserModel userModel = userMapper.findByLoginName(loginName);
        if(userModel != null && userModel.getMobile() != null && getActivityChristmasInvestAmountByLoginName(loginName) >= SINGLE_INVEST_AMOUNT){
            couponAssignmentService.assignUserCoupon(userModel.getMobile(), COUPON_ID);
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

            if (userModel.getRegisterTime().before(activityChristmasEndTime) && userModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                lotteryTime++;
            }

            AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
            if (accountModel != null && accountModel.getRegisterTime().before(activityChristmasEndTime) && accountModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                lotteryTime++;
            }

            List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(activityChristmasPrizeStartTime, activityChristmasEndTime, userModel.getLoginName());
            for (UserModel referrerUserModel : userModels) {
                if (referrerUserModel.getRegisterTime().before(activityChristmasEndTime) && referrerUserModel.getRegisterTime().after(activityChristmasPrizeStartTime)) {
                    lotteryTime++;
                    if (investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), activityChristmasPrizeStartTime, activityChristmasEndTime) > 0) {
                        lotteryTime++;
                    }
                }
            }

            //每满2000元均增加一次
            long sumAmount = investMapper.sumSuccessInvestByInvestTimeAndLoginName(userModel.getLoginName(), activityChristmasPrizeStartTime, activityChristmasEndTime);
            lotteryTime += (int)(sumAmount/200000);

            lotteryTime = lotteryTime >= 10 ? 10 : lotteryTime;

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

        LotteryPrize christmasPrize = lotteryDrawActivityService.lotteryDrawPrize(ActivityCategory.CHRISTMAS_ACTIVITY);

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
                return 324;
        }
        return 0l;
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
        List<Long> amountList = Lists.newArrayList();
        for(InvestModel investModel: investModels){
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
            if(loanDetailsModel != null && loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals("圣诞专享")){
                amountList.add(investModel.getAmount());
            }
        }
        return amountList.size() > 0 ? amountList.parallelStream().max(((o1, o2) -> o1.compareTo(o2))).get() : 0;
    }

    public boolean isFirstInvest(String loginName){
        return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0 ? true : false;
    }
}
