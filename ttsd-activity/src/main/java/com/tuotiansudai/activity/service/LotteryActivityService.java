package com.tuotiansudai.activity.service;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.dto.ActivityTaskStatus;
import com.tuotiansudai.activity.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryActivityService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.UserModel;
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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;

    public UserLotteryDto findUserLotteryByLoginName(String mobile){
        int lotteryTime = 0;
        UserLotteryDto userLotteryDto = new UserLotteryDto(lotteryTime, ActivityTaskStatus.UNFINISHED);
        if (Strings.isNullOrEmpty(mobile)) {
            return userLotteryDto;
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if(userModel == null){
            return userLotteryDto;
        }

        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(userModel.getLoginName(), 1);
        for(ReferrerRelationModel referrerRelationModel : referrerRelationModels){
            UserModel referrerUserModel = userMapper.findByLoginName(referrerRelationModel.getLoginName());
            if(referrerUserModel.getRegisterTime().before(activityAutumnEndTime) && referrerUserModel.getRegisterTime().after(activityAutumnStartTime)){
                lotteryTime ++;
                if(investMapper.countInvestorInvestPagination(referrerUserModel.getLoginName(),null,activityAutumnStartTime,activityAutumnEndTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(activityAutumnEndTime) && userModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
            userLotteryDto.setRegisterStatus(ActivityTaskStatus.COMPLETE);
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel != null && accountModel.getRegisterTime().before(activityAutumnEndTime) && accountModel.getRegisterTime().after(activityAutumnStartTime)){
            lotteryTime ++;
            userLotteryDto.setCertificationStatus(ActivityTaskStatus.COMPLETE);
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel != null && bankCardModel.getCreatedTime().before(activityAutumnEndTime) && bankCardModel.getCreatedTime().after(activityAutumnStartTime)){
            lotteryTime ++;
            userLotteryDto.setBindCardStatus(ActivityTaskStatus.COMPLETE);
        }

        if(investMapper.countInvestorInvestPagination(userModel.getLoginName(), null, activityAutumnStartTime, activityAutumnEndTime) > 0){
            lotteryTime ++;
            userLotteryDto.setInvestStatus(ActivityTaskStatus.COMPLETE);
        }

        return userLotteryDto;
    }

    public DrawLotteryResultDto drawLotteryPrize(String mobile,String drawType){
        logger.debug(mobile + " is drawing the lottery prize.");
        DrawLotteryResultDto drawLotteryResultDto = new DrawLotteryResultDto();

        if (StringUtils.isEmpty(mobile)) {
            logger.error("User not login. can't draw prize.");
            drawLotteryResultDto.setMessage("您还未登陆，请登陆后再来抽奖吧！");
            drawLotteryResultDto.setReturnCode(2);
            return drawLotteryResultDto;
        }

        UserLotteryDto userLotteryDto = findUserLotteryByLoginName(mobile);
        if(userLotteryDto.getLotteryTime() == 0){
            logger.debug(mobile + "is no chance. draw time:" + userLotteryDto.getLotteryTime());
            drawLotteryResultDto.setMessage("您暂无抽奖机会，赢取机会后再来抽奖吧！");
            drawLotteryResultDto.setReturnCode(1);
            drawLotteryResultDto.setStatus(false);
            return drawLotteryResultDto;
        }

        LotteryPrize lotteryPrize = getDrawPrize(drawType);
        UserModel userModel = userMapper.findByMobile(mobile);
        UserLotteryPrizeModel userLotteryPrizeModel = new UserLotteryPrizeModel();
        userLotteryPrizeModel.setMobile(mobile);
        userLotteryPrizeModel.setLoginName(userModel.getLoginName());
        userLotteryPrizeModel.setLotteryTime(DateTime.now().toDate());
        userLotteryPrizeModel.setPrize(lotteryPrize);
        userLotteryPrizeMapper.create(userLotteryPrizeModel);
        drawLotteryResultDto.setReturnCode(0);
        drawLotteryResultDto.setStatus(true);
        return drawLotteryResultDto;
    }

    private LotteryPrize getDrawPrize(String drawType){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 200;
        if(drawType.toUpperCase().equals(LotteryPrize.TOURISM) && mod == 0){
            return LotteryPrize.MANGO_CARD_100;
        } else if (drawType.toUpperCase().equals(LotteryPrize.LUXURY) && mod == 0){
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

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile){
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobile("18888376666");
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(encryptWebMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }


    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByAll(){
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobile("");
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
