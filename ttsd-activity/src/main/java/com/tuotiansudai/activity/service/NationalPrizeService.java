package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
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
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NationalPrizeService {

    private static Logger logger = Logger.getLogger(NationalPrizeService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.startTime}\")}")
    private Date activityNationalStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.endTime}\")}")
    private Date activityNationalEndTime;

    public int getDrawPrizeTime(String mobile){
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }

        if(userModel.getRegisterTime().before(activityNationalEndTime) && userModel.getRegisterTime().after(activityNationalStartTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(activityNationalEndTime) && accountModel.getRegisterTime().after(activityNationalStartTime)){
            lotteryTime ++;
        }

        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModels){
            UserModel referrerUserModel = userMapper.findByLoginName(referrerRelationModel.getLoginName());
            if(referrerUserModel.getRegisterTime().before(activityNationalEndTime) && referrerUserModel.getRegisterTime().after(activityNationalStartTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), activityNationalStartTime, activityNationalEndTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(activityNationalEndTime) && bankCardModel.getCreatedTime().after(activityNationalStartTime)){
            lotteryTime ++;
        }

        if(rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, activityNationalStartTime,activityNationalEndTime) > 0){
            lotteryTime ++;
        }

        if(investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), activityNationalStartTime, activityNationalEndTime) > 0){
            lotteryTime ++;
        }

        long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.NATIONAL_PRIZE, null, null);
        if(lotteryTime > 0){
            lotteryTime -= userTime;
        }
        return lotteryTime;
    }

    public DrawLotteryResultDto drawLotteryPrize(String mobile){
        logger.debug(mobile + " is drawing the lottery prize.");

        Date nowDate = DateTime.now().toDate();
        if(!nowDate.before(activityNationalEndTime) || !nowDate.after(activityNationalStartTime)){
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        if (StringUtils.isEmpty(mobile)) {
            logger.debug("User not login. can't draw prize.");
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            logger.debug(mobile + "User is not found.");
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        int drawTime = getDrawPrizeTime(mobile);
        if(drawTime <= 0){
            logger.debug(mobile + "is no chance. draw time:" + drawTime);
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        LotteryPrize nationalPrize = getLotteryPrize();
        PrizeType prizeType = PrizeType.CONCRETE;
        if(nationalPrize.equals(LotteryPrize.RED_INVEST_15) || nationalPrize.equals(LotteryPrize.RED_INVEST_50)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(nationalPrize));
            prizeType = PrizeType.VIRTUAL;
        }else if(nationalPrize.equals(LotteryPrize.MEMBERSHIP_V5)){
            createUserMembershipModel(userModel.getLoginName(), MembershipLevel.V5.getLevel());
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(), accountModel != null ? accountModel.getUserName() : "", nationalPrize, DateTime.now().toDate(), ActivityCategory.NATIONAL_PRIZE));
        return new DrawLotteryResultDto(0,nationalPrize.name(),prizeType.name());
    }

    private long getCouponId(LotteryPrize lotteryPrize){
        switch (lotteryPrize){
            case RED_INVEST_15 :
                return 309;
            case RED_INVEST_50 :
                return 310;
        }
        return 0l;
    }

    private LotteryPrize getLotteryPrize(){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 100;
        if (mod >= 0 && mod <= 2){
            return LotteryPrize.MEMBERSHIP_V5;
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
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, ActivityCategory.NATIONAL_PRIZE);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(randomUtils.encryptWebMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }

    public String getMyActivityPoint(String loginName){
        return String.valueOf(pointBillMapper.findSumPointByLoginNameAndBusinessType(loginName, activityNationalStartTime, activityNationalEndTime, Lists.newArrayList(PointBusinessType.ACTIVITY)));
    }

    public Map getNationalActivityInvestAmountAndCount(){
        List<InvestModel> investModels = investMapper.countSuccessInvestByInvestTime(null, activityNationalStartTime, activityNationalEndTime);
        Map<String,Object> param = Maps.newConcurrentMap();
        long amount = 0l;
        long count = 0l;
        for(InvestModel investModel : investModels){
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getLoanDetailsByLoanId(investModel.getLoanId());
            if(loanDetailsModel != null && loanDetailsModel.isActivity()){
                amount += investModel.getAmount();
                count ++;
            }
        }
        param.put("investAmount",amount);
        param.put("investCount",count);
        return param;
    }

    private void createUserMembershipModel(String loginName, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).withTime(23,59,59,59).toDate(),
                new Date(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }
}
