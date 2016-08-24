package com.tuotiansudai.activity.service.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.dto.DrawLotteryActivityDto;
import com.tuotiansudai.activity.dto.LotteryPrize;
import com.tuotiansudai.activity.dto.LotteryTaskStatus;
import com.tuotiansudai.activity.dto.UserLotteryDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryTimeMapper;
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
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

    private static Logger logger = Logger.getLogger(LotteryActivityServiceImpl.class);

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private UserLotteryTimeMapper userLotteryTimeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    private Date startTime;

    private Date endTime;

    @Override
    public List<UserLotteryPrizeView> findUserLotteryPrizeViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime,Integer index,Integer pageSize){
        return userLotteryPrizeMapper.findUserLotteryPrizeViews(mobile, selectPrize, startTime, endTime, index, pageSize);
    }

    @Override
    public int findUserLotteryPrizeCountViews(String mobile,LotteryPrize selectPrize,Date startTime,Date endTime){
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, selectPrize, startTime, endTime);
    }

    @Override
    public UserLotteryDto findUserLotteryByLoginName(String mobile){
        int lotteryTime = 0;
        UserLotteryDto userLotteryDto = new UserLotteryDto(lotteryTime,LotteryTaskStatus.UNFINISHED);

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
            if(referrerUserModel.getRegisterTime().before(endTime) && referrerUserModel.getRegisterTime().after(startTime)){
                lotteryTime ++;
                if(investMapper.countInvestorInvestPagination(referrerUserModel.getLoginName(),null,startTime,endTime) > 0){
                    lotteryTime ++;
                }
            }
        }

        if(userModel.getRegisterTime().before(endTime) && userModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
            userLotteryDto.setRegisterStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setRegisterStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if(accountModel.getRegisterTime().before(endTime) && accountModel.getRegisterTime().after(startTime)){
            lotteryTime ++;
            userLotteryDto.setCertificationStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setCertificationStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        BankCardModel bankCardModel = bankCardMapper.findPassedBankCardByLoginName(userModel.getLoginName());
        if(bankCardModel.getCreatedTime().before(endTime) && bankCardModel.getCreatedTime().after(startTime)){
            lotteryTime ++;
            userLotteryDto.setBindCardStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setBindCardStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }

        if(investMapper.countInvestorInvestPagination(userModel.getLoginName(), null, startTime, endTime) > 0){
            lotteryTime ++;
            userLotteryDto.setInvestStatus(LotteryTaskStatus.COMPLETE);
        }else{
            userLotteryDto.setInvestStatus(LotteryTaskStatus.PROCEED);
            return userLotteryDto;
        }
        return userLotteryDto;
    }

    public BaseDto<DrawLotteryActivityDto> drawLotteryPrize(String mobile,String drawType){
        logger.debug(mobile + " is drawing the lottery prize.");
        BaseDto baseDto = new BaseDto();
        DrawLotteryActivityDto drawLotteryActivityDto = new DrawLotteryActivityDto();
        baseDto.setData(drawLotteryActivityDto);

        if (StringUtils.isEmpty(mobile)) {
            logger.error("User not login. can't draw prize.");
            drawLotteryActivityDto.setMessage("您还未登陆，请登陆后再来抽奖吧！");
            drawLotteryActivityDto.setReturnCode(2);
            drawLotteryActivityDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        UserLotteryDto userLotteryDto = findUserLotteryByLoginName(mobile);
        if(userLotteryDto.getLotteryTime() == 0){
            logger.debug(mobile + "is no chance. draw time:" + userLotteryDto.getLotteryTime());
            drawLotteryActivityDto.setMessage("您暂无抽奖机会，赢取机会后再来抽奖吧！");
            drawLotteryActivityDto.setReturnCode(1);
            drawLotteryActivityDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        LotteryPrize lotteryPrize = getDrawPrize(drawType);
        UserModel userModel = userMapper.findByMobile(mobile);
        UserLotteryPrizeModel userLotteryPrizeModel = new UserLotteryPrizeModel();
        userLotteryPrizeModel.setMobile(mobile);
        userLotteryPrizeModel.setLoginName(userModel.getLoginName());
        userLotteryPrizeModel.setLotteryTime(DateTime.now().toDate());
        userLotteryPrizeModel.setPrize(lotteryPrize);
        userLotteryPrizeMapper.create(userLotteryPrizeModel);
        drawLotteryActivityDto.setStatus(false);
        baseDto.setSuccess(false);
        return baseDto;
    }

    private LotteryPrize getDrawPrize(String drawType){
        int random = (int) (Math.random() * 100000000);
        int mod = random % 200;
        System.out.print(mod + " =============> ");
        if(drawType.toUpperCase().equals(LotteryPrize.TOURISM) && mod == 0){
            return LotteryPrize.MANGO_CARD_100;
        }else if(drawType.toUpperCase().equals(LotteryPrize.LUXURY) && mod == 0){
            return LotteryPrize.PORCELAIN_CUP;
        }else if(mod >= 1 && mod <= 40){
            return LotteryPrize.RED_ENVELOPE_100;
        }else if(mod >= 41 && mod <= 90){
            return LotteryPrize.RED_ENVELOPE_50;
        }else if(mod >= 91 && mod <= 140){
            return LotteryPrize.INTEREST_COUPON_5;
        }else{
            return LotteryPrize.INTEREST_COUPON_2;
        }
    }

}
