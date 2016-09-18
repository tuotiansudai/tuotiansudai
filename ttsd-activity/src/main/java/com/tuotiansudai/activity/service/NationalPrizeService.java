package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.NationalPrize;
import com.tuotiansudai.activity.dto.PrizeType;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public int getDrawPrizeTime(String mobile){
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return lotteryTime;
        }

        if(userModel.getRegisterTime().before(activityAutumnEndTime) && userModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(activityAutumnEndTime) && accountModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModels){
            UserModel referrerUserModel = userMapper.findByLoginName(referrerRelationModel.getLoginName());
            if(referrerUserModel.getRegisterTime().before(activityAutumnEndTime) && referrerUserModel.getRegisterTime().after(activityAutumnStartTime)){
                lotteryTime ++;
                if(investMapper.countInvestorSuccessInvestByInvestTime(referrerUserModel.getLoginName(), activityAutumnStartTime, activityAutumnEndTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(activityAutumnEndTime) && bankCardModel.getCreatedTime().after(activityAutumnStartTime)){
            lotteryTime ++;
        }

        if(rechargeMapper.findRechargeCount(null, userModel.getMobile(), null, RechargeStatus.SUCCESS, null, activityAutumnStartTime,activityAutumnEndTime) > 0){
            lotteryTime ++;
        }

        if(investMapper.countInvestorSuccessInvestByInvestTime(userModel.getLoginName(), activityAutumnStartTime, activityAutumnEndTime) > 0){
            lotteryTime ++;
        }

        long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(),null,PrizeType.NATIONAL_PRIZE,null,null);
        if(lotteryTime > 0){
            lotteryTime -= userTime;
        }
        return lotteryTime;
    }

    public DrawLotteryResultDto drawLotteryPrize(String mobile,LotteryPrize drawType){
        logger.debug(mobile + " is drawing the lottery prize.");
        DrawLotteryResultDto drawLotteryResultDto = new DrawLotteryResultDto();

        Date nowDate = DateTime.now().toDate();
        if(!nowDate.before(activityAutumnEndTime) || !nowDate.after(activityAutumnStartTime)){
            drawLotteryResultDto.setMessage("不在活动时间范围内！");
            drawLotteryResultDto.setReturnCode(3);
            return drawLotteryResultDto;
        }

        if (StringUtils.isEmpty(mobile)) {
            logger.error("User not login. can't draw prize.");
            drawLotteryResultDto.setMessage("您还未登陆，请登陆后再来抽奖吧！");
            drawLotteryResultDto.setReturnCode(2);
            return drawLotteryResultDto;
        }

        int drawTime = getDrawPrizeTime(mobile);
        if(drawTime <= 0){
            logger.debug(mobile + "is no chance. draw time:" + drawTime);
            drawLotteryResultDto.setMessage("您暂无抽奖机会，赢取机会后再来抽奖吧！");
            drawLotteryResultDto.setReturnCode(1);
            return drawLotteryResultDto;
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            logger.debug(mobile + "is no chance. draw time:" + drawTime);
            drawLotteryResultDto.setMessage("该用户不存在！");
            drawLotteryResultDto.setReturnCode(1);
            return drawLotteryResultDto;
        }

        userMapper.lockByLoginName(userModel.getLoginName());

        NationalPrize nationalPrize = getNationalprize();
        if(nationalPrize.equals(NationalPrize.RED_ENVELOPE_15) || nationalPrize.equals(NationalPrize.RED_ENVELOPE_50)){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(nationalPrize));
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(),accountModel != null ? accountModel.getUserName() : "", lotteryPrize, DateTime.now().toDate(), PrizeType.NATIONAL_PRIZE));
        drawLotteryResultDto.setReturnCode(0);
        drawLotteryResultDto.setPrizeType(lotteryPrize.getType());
        drawLotteryResultDto.setPrize(lotteryPrize.name());
        return drawLotteryResultDto;
    }

    private long getCouponId(NationalPrize lotteryPrize){
        switch (lotteryPrize){
            case RED_ENVELOPE_15 :
                return 309;
            case RED_ENVELOPE_50 :
                return 310;
        }
        return 0l;
    }

    private NationalPrize getNationalprize(){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 100;
        if (mod >= 0 && mod <= 3){
            return NationalPrize.MEMBERSHIP_V5;
        } else if (mod >= 4 && mod <= 6){
            return NationalPrize.CINEMA_TICKET;
        } else if (mod >= 7 && mod <= 10){
            return NationalPrize.IQIYI_MEMBERSHIP;
        } else if (mod >= 11 && mod <= 15){
            return NationalPrize.TELEPHONE_FARE_10;
        } else if (mod >= 16 && mod <= 45){
            return NationalPrize.RED_ENVELOPE_50;
        }else if (mod >= 46 && mod <= 75){
            return NationalPrize.RED_ENVELOPE_15;
        }else{
            return NationalPrize.MEMBERSHIP_V5;
        }
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile,LotteryPrize activityType){
        if(Strings.isNullOrEmpty(mobile)){
            return Lists.newArrayList();
        }
        return findDrawLotteryPrizeRecord(mobile,activityType);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile,LotteryPrize activityType){
        List<LotteryPrize> lotteryPrizes = activityType.equals(LotteryPrize.TOURISM) ? Lists.newArrayList(LotteryPrize.TOURISM,LotteryPrize.MANGO_CARD_100) : Lists.newArrayList(LotteryPrize.PORCELAIN_CUP,LotteryPrize.LUXURY);

        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, lotteryPrizes);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(randomUtils.encryptWebMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }
}
