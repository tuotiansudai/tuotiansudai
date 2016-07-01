package com.tuotiansudai.point.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.point.dto.UserPointPrizeDto;
import com.tuotiansudai.point.repository.mapper.PointPrizeMapper;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointPrizeModel;
import com.tuotiansudai.point.repository.model.UserPointPrizeModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PointLotteryServiceImpl implements PointLotteryService{

    static Logger logger = Logger.getLogger(PointLotteryServiceImpl.class);

    private final static long LOTTERY_POINT = -1000;

    private final static String ALREADY_LOTTERY_NOT_SHARE = "AlreadyLotteryNotShare";

    private final static String ALREADY_LOTTERY_SHARE = "AlreadyLotteryShare";

    private final static String POINT_NOT_ENOUGH = "PointNotEnough";

    private final static String LAST_EXPIRY_TIME = " 23:59:59";

    @Autowired
    private PointPrizeMapper pointPrizeMapper;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    public static String redisShareTemple = "web:ranking:shared:{0}:{1}";

    @Override
    public void imitateLottery() {
        String loginName;
        do {
            loginName = this.imitateLoginName();
        } while (userMapper.findByLoginName(loginName) != null);
        long notRealNum = userPointPrizeMapper.findAllNotReal();
        List<PointPrizeModel> pointPrizeModels;
        if (notRealNum % 2880 != 0 || notRealNum == 0) {
            pointPrizeModels = pointPrizeMapper.findAllPossibleWin();
        } else {
            pointPrizeModels = pointPrizeMapper.findAllUnPossibleWin();
        }
        int num = new Random().nextInt(pointPrizeModels.size());
        UserPointPrizeModel userPointPrizeModel = new UserPointPrizeModel(pointPrizeModels.get(num).getId(), loginName, false);
        userPointPrizeMapper.create(userPointPrizeModel);
    }

    private String imitateLoginName() {
        int length = new Random().nextInt(20) + 5;
        String base = "abcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public String pointLottery(String loginName) {
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (accountModel.getPoint() < -LOTTERY_POINT) {
            return POINT_NOT_ENOUGH;
        }
        DateTime dateTime = new DateTime();
        List<UserPointPrizeModel> userPointPrizeModelToday = userPointPrizeMapper.findByLoginNameAndCreateTime(loginName, dateTime.toString("yyyy-MM-dd"));
        if (CollectionUtils.isEmpty(userPointPrizeModelToday) ||
                (redisWrapperClient.exists(MessageFormat.format(redisShareTemple, loginName, dateTime.toString("yyyyMMdd"))) && userPointPrizeModelToday.size() < 2)) {
            PointPrizeModel winPointPrize = this.winLottery();
            UserPointPrizeModel userPointPrizeModel = new UserPointPrizeModel(winPointPrize.getId(), loginName, true);
            userPointPrizeMapper.create(userPointPrizeModel);

            pointBillService.createPointBill(loginName, winPointPrize.getId(), PointBusinessType.LOTTERY, LOTTERY_POINT);

            if (winPointPrize.getCouponId() != null) {
                couponAssignmentService.assignUserCoupon(loginName, winPointPrize.getCouponId());
            }

            return winPointPrize.getName();
        } else if (userPointPrizeModelToday.size() == 1 && !redisWrapperClient.exists(MessageFormat.format(redisShareTemple, loginName, dateTime.toString("yyyyMMdd")))){
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
        int num = new Random().nextInt(100)+1;
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
    public void getLotteryOnceChance(String loginName){
        SimpleDateFormat formatShortTime = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String begin = formatter.format(new Date());
        String end = formatShortTime.format(new Date()) + LAST_EXPIRY_TIME;
        int expiryTime = DateUtil.differenceSeconds(begin, end);
        redisWrapperClient.setex(MessageFormat.format(redisShareTemple, loginName, formatShortTime.format(new Date()).replace("-","")), expiryTime, "1");
    }

    @Override
    public List<UserPointPrizeDto> findAllDrawLottery() {
        List<UserPointPrizeModel> userPointPrizeModels = userPointPrizeMapper.findAllDescCreatedTime();
        return Lists.transform(userPointPrizeModels, new Function<UserPointPrizeModel, UserPointPrizeDto>() {
            @Override
            public UserPointPrizeDto apply(UserPointPrizeModel input) {
                UserPointPrizeDto userPointPrizeDto = new UserPointPrizeDto(input.getLoginName(), pointPrizeMapper.findById(input.getPointPrizeId()).getDescription(), input.getCreatedTime());
                return userPointPrizeDto;
            }
        });
    }

    @Override
    public List<UserPointPrizeDto> findMyDrawLottery(String loginName) {
        List<UserPointPrizeModel> userPointPrizeModels = userPointPrizeMapper.findByLoginName(loginName);
        return Lists.transform(userPointPrizeModels, new Function<UserPointPrizeModel, UserPointPrizeDto>() {
            @Override
            public UserPointPrizeDto apply(UserPointPrizeModel input) {
                UserPointPrizeDto userPointPrizeDto = new UserPointPrizeDto(input.getLoginName(), pointPrizeMapper.findById(input.getPointPrizeId()).getDescription(), input.getCreatedTime());
                return userPointPrizeDto;
            }
        });
    }

}
