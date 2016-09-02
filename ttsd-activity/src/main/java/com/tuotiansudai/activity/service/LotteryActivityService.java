package com.tuotiansudai.activity.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LotteryActivityService {

    private static Logger logger = Logger.getLogger(LotteryActivityService.class);

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

        long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(),null,null,null);
        if(lotteryTime > 0){
            lotteryTime -= userTime;
        }
        return lotteryTime;
    }

    public DrawLotteryResultDto drawLotteryPrize(String mobile,LotteryPrize drawType){
        logger.debug(mobile + " is drawing the lottery prize.");
        DrawLotteryResultDto drawLotteryResultDto = new DrawLotteryResultDto();

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

        LotteryPrize lotteryPrize = getDrawPrize(drawType);
        if(lotteryPrize.getType().equals("virtual")){
            couponAssignmentService.assignUserCoupon(mobile, getCouponId(lotteryPrize));
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile, userModel.getLoginName(),accountModel != null ? accountModel.getUserName() : "", lotteryPrize, DateTime.now().toDate()));
        drawLotteryResultDto.setReturnCode(0);
        drawLotteryResultDto.setPrizeType(lotteryPrize.getType());
        drawLotteryResultDto.setPrize(lotteryPrize.name());
        return drawLotteryResultDto;
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
        }
        return 0l;
    }

    private LotteryPrize getDrawPrize(LotteryPrize drawType){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 200;
        if(drawType.equals(LotteryPrize.TOURISM) && mod == 0){
            return LotteryPrize.MANGO_CARD_100;
        } else if (drawType.equals(LotteryPrize.LUXURY) && mod == 0){
            return LotteryPrize.PORCELAIN_CUP;
        } else if (mod >= 1 && mod <= 40){ // 1/5
            return LotteryPrize.RED_ENVELOPE_100;
        } else if (mod >= 41 && mod <= 90){ // 1/4
            return LotteryPrize.RED_ENVELOPE_50;
        } else if (mod >= 91 && mod <= 140){ // 1/4
            return LotteryPrize.INTEREST_COUPON_5;
        } else {
            return LotteryPrize.INTEREST_COUPON_2;
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
            view.setMobile(encryptWebMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }

    public String encryptWebMiddleMobile(String mobile) {
        return mobile.substring(0, 3) + showChar(4) + mobile.substring(7);
    }

    private static String showChar(int showLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < showLength; i++) {
            sb.append('*');
        }
        return sb.toString();
    }
}
