package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MoneyTreePrizeService {

    private static Logger logger = Logger.getLogger(MoneyTreePrizeService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Value("#{'${activity.money.tree.period}'.split('\\~')}")
    private List<String> moneyTreeTime = Lists.newArrayList();

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile) {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findMoneyTreeLotteryPrizeByMobile(mobile, ActivityCategory.MONEY_TREE);
        for (UserLotteryPrizeView view : userLotteryPrizeViews) {
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
            view.setPrizeValue(view.getPrize().getDescription());
        }
        return userLotteryPrizeViews;
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordTop10() {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findMoneyTreeLotteryPrizeTop10(ActivityCategory.MONEY_TREE);
        for (UserLotteryPrizeView view : userLotteryPrizeViews) {
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
            view.setPrizeValue(view.getPrize().getDescription());
        }
        return userLotteryPrizeViews;
    }

    public int getLeftDrawPrizeTime(String mobile) {
        int lotteryTime = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return lotteryTime;
        }

        //每天增加一次
        if (userModel != null) {
            lotteryTime++;
        }

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), userModel.getLoginName());

        //根据注册时间分组
        Map<String, Long> groupByTeachers = userModels
                .stream()
                .collect(Collectors.groupingBy(p -> String.format("%tF", p.getRegisterTime()), Collectors.counting()));

        //单日邀请人数超过3人者，最多给3次摇奖机会
        for (Map.Entry<String, Long> entry : groupByTeachers.entrySet()) {
            if (entry.getValue() >= 3) {
                lotteryTime += 3;
            } else {
                lotteryTime += entry.getValue();
            }
        }

        long userTime = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, null, null);
        if (lotteryTime > 0) {
            lotteryTime -= userTime;
        }
        return lotteryTime;
    }

    @Transactional
    public DrawLotteryResultDto drawLotteryPrize(String mobile) {
        logger.info(mobile + " is drawing the lottery prize.");

        Date nowDate = DateTime.now().toDate();
        if (!nowDate.before(DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()) || !nowDate.after(DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())) {
            return new DrawLotteryResultDto(3);//不在活动时间范围内！
        }

        if (StringUtils.isEmpty(mobile)) {
            logger.info("User not login. can't draw prize.");
            return new DrawLotteryResultDto(2);//您还未登陆，请登陆后再来拆奖吧！
        }

        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            logger.info(mobile + "User is not found.");
            return new DrawLotteryResultDto(2);//"该用户不存在！"
        }

        userMapper.lockByLoginName(userModel.getLoginName());
        int drawTime = getLeftDrawPrizeTime(mobile);
        if (drawTime <= 0) {
            logger.info(mobile + "is no chance. draw time:" + drawTime);
            return new DrawLotteryResultDto(1);//您暂无摇奖机会，赢取机会后再来抽奖吧！
        }

        ActivityCategory activityCategory = this.getActivityCategoryByLoginName(
                DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(),
                DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(),
                userModel.getLoginName());

        LotteryPrize moneyTreePrize = lotteryDrawActivityService.drawLotteryPrize(activityCategory);

        this.drawResultAssignUserCoupon(moneyTreePrize, mobile);

        userLotteryPrizeMapper.create(new UserLotteryPrizeModel(mobile,
                userModel.getLoginName(),
                Strings.isNullOrEmpty(userModel.getUserName()) ? "" : userModel.getUserName(),
                moneyTreePrize,
                DateTime.now().toDate(),
                activityCategory));
        return new DrawLotteryResultDto(0, moneyTreePrize.name(), PrizeType.VIRTUAL.name(), moneyTreePrize.getDescription());
    }

    public int isActivity() {
        Date nowDate = DateTime.now().toDate();
        return nowDate.after(DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()) && nowDate.before(DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate()) ? 1 : 0;
    }

    private ActivityCategory getActivityCategoryByLoginName(Date activityStartTime, Date activityEndTime, String loginName) {
        long sumAmount = investMapper.sumInvestAmountByLoginNameInvestTimeProductType(loginName, activityStartTime, activityEndTime, Lists.newArrayList(ProductType._90, ProductType._180, ProductType._360));
        sumAmount = sumAmount / 100;
        if (sumAmount <= 1000) {
            return ActivityCategory.MONEY_TREE_UNDER_1000_ACTIVITY;
        } else if (sumAmount >= 1001 && sumAmount <= 10000) {
            return ActivityCategory.MONEY_TREE_UNDER_10000_ACTIVITY;
        } else if (sumAmount >= 10001 && sumAmount <= 20000) {
            return ActivityCategory.MONEY_TREE_UNDER_20000_ACTIVITY;
        } else if (sumAmount >= 20001 && sumAmount <= 30000) {
            return ActivityCategory.MONEY_TREE_UNDER_30000_ACTIVITY;
        } else if (sumAmount >= 30001 && sumAmount <= 40000) {
            return ActivityCategory.MONEY_TREE_UNDER_40000_ACTIVITY;
        } else if (sumAmount >= 40001 && sumAmount <= 50000) {
            return ActivityCategory.MONEY_TREE_UNDER_50000_ACTIVITY;
        } else if (sumAmount >= 50001 && sumAmount <= 60000) {
            return ActivityCategory.MONEY_TREE_UNDER_60000_ACTIVITY;
        } else if (sumAmount >= 60001 && sumAmount <= 70000) {
            return ActivityCategory.MONEY_TREE_UNDER_70000_ACTIVITY;
        } else if (sumAmount >= 70001 && sumAmount <= 80000) {
            return ActivityCategory.MONEY_TREE_UNDER_80000_ACTIVITY;
        } else if (sumAmount >= 80001 && sumAmount <= 90000) {
            return ActivityCategory.MONEY_TREE_UNDER_90000_ACTIVITY;
        } else if (sumAmount >= 90001 && sumAmount < 100000) {
            return ActivityCategory.MONEY_TREE_UNDER_100000_ACTIVITY;
        } else if (sumAmount >= 100000) {
            return ActivityCategory.MONEY_TREE_ABOVE_100000_ACTIVITY;
        }
        return null;
    }

    private void drawResultAssignUserCoupon(LotteryPrize moneyTreePrize, String mobile) {
        switch (moneyTreePrize) {
            case MONEY_TREE_1000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_10000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_20000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_30000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_40000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_50000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_50:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_50:
                //存入体验金
                // couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_50_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_20000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_30000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_40000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_50000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_100:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_100:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_100_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_20000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_30000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_40000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_50000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_200:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_200:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_200_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_30000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_40000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_50000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_300:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_300:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_300_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_50000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_500:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_500_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_60000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_600:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_600_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_80000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_800:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_800_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_90000_EXPERIENCE_GOLD_1000:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_1000:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_1000:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_1000_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_2000:
                //存入体验金
                //couponAssignmentService.assignUserCoupon(mobile, RED_ENVELOPE_2000_DRAW_REF_MONEY_TREE_COUPON_ID);
                break;
        }
    }
}
