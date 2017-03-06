package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.util.DateUtil;
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

    @Autowired
    private ExperienceBillService experienceBillService;

    @Value("#{'${activity.money.tree.period}'.split('\\~')}")
    private List<String> moneyTreeTime = Lists.newArrayList();


    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_50 = 5000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_100 = 10000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_200 = 20000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_300 = 30000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_500 = 50000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_600 = 60000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_800 = 80000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_1000 = 100000;
    private static final long MONEY_TREE_EXPERIENCE_BILL_AMOUNT_2000 = 200000;

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile) {
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findMoneyTreeLotteryPrizeByMobile(mobile, ActivityCategory.MONEY_TREE);
        for (UserLotteryPrizeView view : userLotteryPrizeViews) {
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
            view.setPrizeValue(view.getPrize().getDescription());
        }
        return userLotteryPrizeViews;
    }

    public List<UserLotteryTop10PrizeView> findDrawLotteryPrizeRecordTop10() {
        List<UserLotteryTop10PrizeView> userLotteryPrize10Views = userLotteryPrizeMapper.findMoneyTreeLotteryPrizeTop10(ActivityCategory.MONEY_TREE);
        for (UserLotteryTop10PrizeView view : userLotteryPrize10Views) {
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
            view.setPrize(view.getPrize() + "元");
        }
        return userLotteryPrize10Views;
    }

    public int getLeftDrawPrizeTime(String mobile) {

        int lotteryTimes = 0;
        UserModel userModel = userMapper.findByMobile(mobile);
        if (userModel == null) {
            return 0;
        }

        //判断当天是否参与过摇奖，没有参与过给一次机会
        int isLottery = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, new DateTime(new Date()).withTimeAtStartOfDay().toDate(), new DateTime(new Date()).withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate());
        if (isLottery == 0) {
            lotteryTimes = 1;
        }

        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), DateTime.parse(moneyTreeTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate(), userModel.getLoginName());

        int referrerLotteryTimes = 0;
        //根据注册时间分组
        Map<String, Long> groupByEveryDayCounts = userModels
                .stream()
                .collect(Collectors.groupingBy(p -> String.format("%tF", p.getRegisterTime()), Collectors.counting()));

        //单日邀请人数超过3人者，最多给3次摇奖机会
        for (Map.Entry<String, Long> entry : groupByEveryDayCounts.entrySet()) {
            if (entry.getValue() > 3) {
                referrerLotteryTimes += 3;
            } else {
                referrerLotteryTimes += entry.getValue();
            }
        }
        //查询所有已经摇奖的次数
        int usedLotteryTimes = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, null, null);

        int usedEveryDayLotteryTimes = 0;
        Date startTime = DateTime.parse(moneyTreeTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = new Date();

        //查询活动开始后，每天的摇奖次数（如果每天都有摇将，则记录每天一次的总和）
        long countDays = DateUtil.differenceDay(startTime, endTime);

        for (int i = 0; i <= countDays; i++) {
            int isEveryDayLottery = userLotteryPrizeMapper.findUserLotteryPrizeCountViews(userModel.getMobile(), null, ActivityCategory.MONEY_TREE, new DateTime(startTime).plusDays(i).withTimeAtStartOfDay().toDate(), new DateTime(startTime).plusDays(i).withTimeAtStartOfDay().plusHours(23).plusMinutes(59).plusSeconds(59).toDate());
            usedEveryDayLotteryTimes += isEveryDayLottery > 0 ? 1 : 0;
        }

        lotteryTimes = (lotteryTimes + referrerLotteryTimes) - (usedLotteryTimes - usedEveryDayLotteryTimes);
        return lotteryTimes;
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

        this.drawResultAssignExperience(moneyTreePrize, userModel.getLoginName());

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

    private void drawResultAssignExperience(LotteryPrize moneyTreePrize, String loginName) {
        ExperienceBillOperationType experienceBillOperationType = ExperienceBillOperationType.IN;
        ExperienceBillBusinessType experienceBillBusinessType = ExperienceBillBusinessType.MONEY_TREE;
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
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_50, loginName, experienceBillOperationType, experienceBillBusinessType);
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
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_100, loginName, experienceBillOperationType, experienceBillBusinessType);
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
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_200, loginName, experienceBillOperationType, experienceBillBusinessType);
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
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_300, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
            case MONEY_TREE_50000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_60000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_500:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_500:
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_500, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
            case MONEY_TREE_60000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_70000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_80000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_600:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_600:
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_600, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
            case MONEY_TREE_80000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_90000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_800:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_800:
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_800, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
            case MONEY_TREE_90000_EXPERIENCE_GOLD_1000:
            case MONEY_TREE_100000_EXPERIENCE_GOLD_1000:
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_1000:
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_1000, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
            case MONEY_TREE_ABOVE_100000_EXPERIENCE_GOLD_2000:
                experienceBillService.updateUserExperienceBalanceByLoginName(MONEY_TREE_EXPERIENCE_BILL_AMOUNT_2000, loginName, experienceBillOperationType, experienceBillBusinessType);
                break;
        }

    }
}
