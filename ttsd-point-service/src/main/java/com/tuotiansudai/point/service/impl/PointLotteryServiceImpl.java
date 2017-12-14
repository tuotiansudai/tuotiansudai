package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.point.repository.dto.UserPointPrizeDto;
import com.tuotiansudai.point.repository.mapper.PointPrizeMapper;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointChangingResult;
import com.tuotiansudai.point.repository.model.PointPrizeModel;
import com.tuotiansudai.point.repository.model.UserPointPrizeModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.RandomUtils;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class PointLotteryServiceImpl implements PointLotteryService {

    static Logger logger = Logger.getLogger(PointLotteryServiceImpl.class);

    private final static long LOTTERY_POINT = -1000;

    private final static String ALREADY_LOTTERY_NOT_SHARE = "AlreadyLotteryNotShare";

    private final static String ALREADY_LOTTERY_SHARE = "AlreadyLotteryShare";

    private final static String POINT_NOT_ENOUGH = "PointNotEnough";

    private final static String POINT_CHANGING_FREQUENTLY = "PointChangingFrequently";

    private final static String POINT_CHANGING_FAIL = "PointChangingFail";

    private final static String LAST_EXPIRY_TIME = " 23:59:59";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PointPrizeMapper pointPrizeMapper;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RandomUtils randomUtils;

    private static String redisShareTemple = "web:ranking:shared:{0}:{1}";

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153,186,188".split(",");

    private static String telTemplate = "{0}****{1}";

    @Override
    public void imitateLottery() {
        String mobile = this.imitateMobile();
        long notRealNum = userPointPrizeMapper.findAllNotReal();
        List<PointPrizeModel> pointPrizeModels;
        if (notRealNum % 2880 != 0 || notRealNum == 0) {
            pointPrizeModels = pointPrizeMapper.findAllPossibleWin();
        } else {
            pointPrizeModels = pointPrizeMapper.findAllUnPossibleWin();
        }
        int num = new Random().nextInt(pointPrizeModels.size());
        UserPointPrizeModel userPointPrizeModel = new UserPointPrizeModel(pointPrizeModels.get(num).getId(), mobile, false);
        userPointPrizeMapper.create(userPointPrizeModel);
    }

    private String imitateMobile() {
        Random random = new Random();
        int temp = random.nextInt(telFirst.length);
        int end = (int) (Math.random() * 9000 + 1000);
        return MessageFormat.format(telTemplate, telFirst[temp], String.valueOf(end));
    }

    @Override
    @Transactional
    public String pointLottery(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel.getPoint() < -LOTTERY_POINT) {
            return POINT_NOT_ENOUGH;
        }

        DateTime dateTime = new DateTime();
        List<UserPointPrizeModel> userPointPrizeModelToday = userPointPrizeMapper.findByLoginNameAndCreateTime(loginName, dateTime.toString("yyyy-MM-dd"));
        if (CollectionUtils.isEmpty(userPointPrizeModelToday) ||
                (redisWrapperClient.exists(MessageFormat.format(redisShareTemple, loginName, dateTime.toString("yyyyMMdd"))) && userPointPrizeModelToday.size() < 2)) {
            PointPrizeModel winPointPrize = this.winLottery();
            PointChangingResult pointChangingResult = pointBillService.createPointBill(loginName, winPointPrize.getId(), PointBusinessType.LOTTERY, LOTTERY_POINT);
            if (pointChangingResult == PointChangingResult.CHANGING_FAIL) {
                return POINT_CHANGING_FAIL;
            } else if (pointChangingResult == PointChangingResult.CHANGING_FREQUENTLY) {
                return POINT_CHANGING_FREQUENTLY;
            }
            UserPointPrizeModel userPointPrizeModel = new UserPointPrizeModel(winPointPrize.getId(), loginName, true);
            userPointPrizeMapper.create(userPointPrizeModel);

            if (winPointPrize.getCouponId() != null) {
                couponAssignmentService.assignUserCoupon(loginName, winPointPrize.getCouponId());
            }

            return winPointPrize.getName();
        } else if (userPointPrizeModelToday.size() == 1 && !redisWrapperClient.exists(MessageFormat.format(redisShareTemple, loginName, dateTime.toString("yyyyMMdd")))) {
            return ALREADY_LOTTERY_NOT_SHARE;
        } else {
            return ALREADY_LOTTERY_SHARE;
        }
    }


    private PointPrizeModel winLottery() {
        List<PointPrizeModel> pointPrizeModels = pointPrizeMapper.findAllPossibleWin();
        int[] probabilities = new int[pointPrizeModels.size()];
        for (int i = 0; i < probabilities.length; i++) {
            if (i > 0) {
                probabilities[i] = pointPrizeModels.get(i).getProbability() + pointPrizeModels.get(i - 1).getProbability();
            } else {
                probabilities[i] = pointPrizeModels.get(i).getProbability();
            }
        }
        int num = new Random().nextInt(100) + 1;
        int choosePointPrize = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (num <= probabilities[i]) {
                choosePointPrize = i;
                break;
            }
        }
        return pointPrizeModels.get(choosePointPrize);
    }

    @Override
    public void getLotteryOnceChance(String loginName) {
        SimpleDateFormat formatShortTime = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String begin = formatter.format(new Date());
        String end = formatShortTime.format(new Date()) + LAST_EXPIRY_TIME;
        int expiryTime = differenceSeconds(begin, end);
        redisWrapperClient.setex(MessageFormat.format(redisShareTemple, loginName, formatShortTime.format(new Date()).replace("-", "")), expiryTime, "1");
    }

    @Override
    public List<UserPointPrizeDto> findAllDrawLottery() {
        List<UserPointPrizeModel> userPointPrizeModels = userPointPrizeMapper.findAllDescCreatedTime();
        return userPointPrizeModels.stream().map(input -> {
            String loginName = input.isReality() ?
                    randomUtils.encryptMobile(null, input.getLoginName()) : input.getLoginName();
            return new UserPointPrizeDto(loginName,
                    pointPrizeMapper.findById(input.getPointPrizeId()).getDescription(),
                    input.getCreatedTime());
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserPointPrizeDto> findMyDrawLottery(String loginName) {
        List<UserPointPrizeModel> userPointPrizeModels = userPointPrizeMapper.findByLoginName(loginName);
        return userPointPrizeModels.stream()
                .map(input -> new UserPointPrizeDto(input.getLoginName(), pointPrizeMapper.findById(input.getPointPrizeId()).getDescription(), input.getCreatedTime()))
                .collect(Collectors.toList());
    }

    private int differenceSeconds(String date1, String date2) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse(date1);
            end = dfs.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (int) (end.getTime() - begin.getTime()) / 1000;
    }

}
