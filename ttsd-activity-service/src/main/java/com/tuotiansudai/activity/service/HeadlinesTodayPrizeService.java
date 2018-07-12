package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.LotteryPrize;
import com.tuotiansudai.activity.repository.model.PrizeType;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeadlinesTodayPrizeService {

    private static Logger logger = Logger.getLogger(HeadlinesTodayPrizeService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    private static final long RED_ENVELOPE_50_YUAN_DRAW_REF_CARNIVAL_COUPON_ID = 332L;

    public int getDrawPrizeTime(String mobile) {
        int lotteryTime = 0;
        if (StringUtils.isEmpty(mobile)) {
            return lotteryTime;
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return lotteryTime;
        }

        if (userModel != null) {
            lotteryTime++;
        }

        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), Role.INVESTOR);
        if (bankAccountModel != null) {
            lotteryTime++;
        }

        long usedTime = userLotteryPrizeMapper.findUserLotteryPrizeCountByMobile(userModel.getMobile(), ActivityCategory.HEADLINES_TODAY_ACTIVITY);
        if (lotteryTime > 0) {
            lotteryTime -= usedTime;
        }
        return lotteryTime;
    }

    @Transactional
    public DrawLotteryResultDto drawLotteryPrize(String mobile) {
        logger.info(mobile + " is drawing the lottery prize.");

        if (StringUtils.isEmpty(mobile)) {
            logger.info("User not login. can't draw prize.");
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来抽奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            logger.info(mobile + "User is not found.");
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        userMapper.lockByLoginName(userModel.getLoginName());
        int drawTime = getDrawPrizeTime(mobile);
        if (drawTime <= 0) {
            logger.info(mobile + "is no chance. draw time:" + drawTime);
            return new DrawLotteryResultDto(1);//您暂无抽奖机会，赢取机会后再来抽奖吧！
        }

        LotteryPrize headlinesTodayPrize = lotteryDrawActivityService.drawLotteryPrize(ActivityCategory.HEADLINES_TODAY_ACTIVITY);
        PrizeType prizeType = PrizeType.CONCRETE;
        if (headlinesTodayPrize.equals(LotteryPrize.RED_ENVELOPE_50_YUAN_DRAW_REF_CARNIVAL)) {
            couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_50_YUAN_DRAW_REF_CARNIVAL_COUPON_ID);
            prizeType = PrizeType.VIRTUAL;
        }

        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile,
                userModel.getLoginName(),
                Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getUserName(),
                headlinesTodayPrize,
                DateTime.now().toDate(),
                ActivityCategory.HEADLINES_TODAY_ACTIVITY));
        return new DrawLotteryResultDto(0, headlinesTodayPrize.name(), prizeType.name(), headlinesTodayPrize.getDescription());
    }

    public String userStatus(String mobile, String status) {
        if (StringUtils.isEmpty(mobile)) {
            logger.info("User is not exist, please register");
            return "NOT_REGISTER";
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), Role.INVESTOR);

        if (userModel == null) {
            logger.info("User is not exist, please register");
            return "NOT_REGISTER";
        }

        if (userModel != null && bankAccountModel == null && status.equals("fromRegister")) {
            logger.info("User is is exist, but not account");
            return "REGISTER_LOGIN_TO_ACCOUNT";
        }

        if (userModel != null && bankAccountModel == null && status.equals("")) {
            logger.info("User is is exist, but not account");
            return "LOGIN_TO_ACCOUNT";
        }

        if (userModel != null && bankAccountModel != null) {
            logger.info("user is account");
            return "ACCOUNT";
        }
        return "";
    }


}
