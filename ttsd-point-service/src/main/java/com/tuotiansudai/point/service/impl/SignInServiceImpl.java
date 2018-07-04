package com.tuotiansudai.point.service.impl;

import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.point.repository.dto.SignInPointDto;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBillModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.SignInService;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.UserSignInMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class SignInServiceImpl implements SignInService {
    static Logger logger = Logger.getLogger(SignInServiceImpl.class);

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Autowired
    private UserSignInMapper userSignInMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    private static final long SIGN_IN_FULL_COUPON_ID = 381L;
    private static final int SIGN_IN_REWARD_PERIOD = 58;

    private enum SignInReward {
        REWARD_OF_8_DAY(8, 376L),
        REWARD_OF_18_DAY(18, 377L),
        REWARD_OF_28_DAY(28, 378L),
        REWARD_OF_38_DAY(38, 379L),
        REWARD_OF_58_DAY(58, 380L),
        NONE(0, null);

        final private int dayLimit;
        final private Long couponId;

        SignInReward(int dayLimit, Long couponId) {
            this.dayLimit = dayLimit;
            this.couponId = couponId;
        }

        public int getDayLimit() {
            return dayLimit;
        }

        public Long getCouponId() {
            return couponId;
        }
    }

    @Override
    @Transactional(transactionManager = "aaTransactionManager")
    public SignInPointDto signIn(String loginName) {
        BankAccountModel bankAccountModel = bankAccountMapper.findInvestorByLoginName(loginName);
        if (null == bankAccountModel) {
            return null;
        }

        SignInPointDto signInPointDto;
        SignInPointDto lastSignInPointDto = obtainCurrentSignInPointDto(loginName);
        if (lastSignInPointDto == null ||
                Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()).withTimeAtStartOfDay(), new DateTime().withTimeAtStartOfDay()).isGreaterThan(Days.ONE)) {
            signInPointDto = new SignInPointDto(1, new Date(), getCurrentSignInPoint(1), getNextSignInPoint(1), false);
        } else if (!lastSignInPointDto.isSignIn()) {
            signInPointDto = new SignInPointDto(lastSignInPointDto.getSignInCount() + 1, new Date(), getCurrentSignInPoint(lastSignInPointDto.getSignInCount() + 1), getNextSignInPoint(lastSignInPointDto.getSignInCount() + 1), false);
            signInPointDto.setFull(isFull(signInPointDto.getSignInCount()));
            signInPointDto.setCurrentRewardDesc(getCurrentRewardDesc(signInPointDto));
        } else {
            signInPointDto = lastSignInPointDto;
            signInPointDto.setStatus(false);
            signInPointDto.setMessage("今天已经签到过了，请明天再来!");
        }

        signInPointDto.setNextRewardDesc(getNextRewardDesc(signInPointDto));

        if (!signInPointDto.isSignIn()) {
            signInPointDto.setStatus(true);
            if (userSignInMapper.exists(loginName)) {
                userSignInMapper.updateSignInCount(loginName, signInPointDto.getSignInCount());
            } else {
                userSignInMapper.create(loginName, signInPointDto.getSignInCount());
            }
            pointBillService.createPointBill(loginName, null, PointBusinessType.SIGN_IN, signInPointDto.getSignInPoint());
            sendSignInCoupon(loginName, signInPointDto.getSignInCount());
            logger.info(MessageFormat.format("{0} sign in success {1} 次", loginName, signInPointDto.getSignInCount()));
        }

        return signInPointDto;
    }

    @Override
    public boolean signInIsSuccess(String loginName) {
        SignInPointDto signInPointDto = obtainCurrentSignInPointDto(loginName);

        return signInPointDto != null && signInPointDto.isSignIn();
    }

    @Override
    public SignInPointDto getLastSignIn(String loginName) {
        return obtainCurrentSignInPointDto(loginName);
    }

    private SignInPointDto obtainCurrentSignInPointDto(String loginName) {
        PointBillModel pointBillModel = pointBillMapper.findLatestSignInPointBillByLoginName(loginName);
        if (pointBillModel == null) {
            return null;
        }
        int signInCount = userSignInMapper.getUserSignInCount(loginName);
        return new SignInPointDto(
                signInCount,
                pointBillModel.getCreatedTime(),
                (int) pointBillModel.getPoint(),
                getNextSignInPoint(signInCount),
                DateUtils.isSameDay(pointBillModel.getCreatedTime(), new Date())
        );
    }

    @Override
    public int getNextSignInPoint(String loginName) {
        SignInPointDto currentSignInPointDto = obtainCurrentSignInPointDto(loginName);
        if (currentSignInPointDto == null ||
                (Days.daysBetween(new DateTime(currentSignInPointDto.getSignInDate()).withTimeAtStartOfDay(), new DateTime().withTimeAtStartOfDay()).getDays() > 1)) {
            return getNextSignInPoint(0);
        }

        return currentSignInPointDto.getNextSignInPoint();
    }

    @Override
    public int getSignInCount(String loginName) {
        SignInPointDto lastSignInPointDto = getLastSignIn(loginName);
        DateTime today = new DateTime().withTimeAtStartOfDay();
        if (lastSignInPointDto != null && (Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), today) == Days.ONE
                || Days.daysBetween(new DateTime(lastSignInPointDto.getSignInDate()), today) == Days.ZERO)) {
            return lastSignInPointDto.getSignInCount();
        }
        return 0;
    }

    private int getCurrentSignInPoint(int signInCount) {
        if (1 <= signInCount && signInCount <= 3) {
            return 10;
        } else if (4 <= signInCount && signInCount <= 6) {
            return 15;
        } else if (7 <= signInCount && signInCount <= 10) {
            return 20;
        } else if (11 <= signInCount && signInCount <= 15) {
            return 25;
        } else if (15 <= signInCount) {
            return 30;
        }
        return 0;
    }

    private int getNextSignInPoint(int signInCount) {
        if (0 <= signInCount && signInCount <= 2) {
            return 10;
        } else if (3 <= signInCount && signInCount <= 5) {
            return 15;
        } else if (6 <= signInCount && signInCount <= 9) {
            return 20;
        } else if (10 <= signInCount && signInCount <= 14) {
            return 25;
        } else if (14 <= signInCount) {
            return 30;
        }
        return 0;
    }

    private SignInReward getSignInReward(int signInCount) {
        final int SIGN_IN_REWARD_PERIOD = 58;
        int signInRewardDay = signInCount % SIGN_IN_REWARD_PERIOD;
        if (signInCount != 0 && signInRewardDay == 0) {
            signInRewardDay = SIGN_IN_REWARD_PERIOD;
        }
        for (SignInReward signInReward : SignInReward.values()) {
            if (signInReward.dayLimit == signInRewardDay) {
                return signInReward;
            }
        }
        return SignInReward.NONE;
    }

    private SignInReward getNextSignInReward(int signInCount) {
        int signInRewardDay = signInCount % SIGN_IN_REWARD_PERIOD;
        SignInReward curReward = SignInReward.REWARD_OF_58_DAY;
        for (SignInReward signInReward : SignInReward.values()) {
            if (signInRewardDay < signInReward.dayLimit && curReward.dayLimit > signInReward.dayLimit) {
                curReward = signInReward;
            }
        }
        return curReward;
    }

    private boolean isFull(int signInCount) {
        final int SIGN_IN_FULL_DAY_PERIOD = 365;
        return signInCount != 0 && signInCount % SIGN_IN_FULL_DAY_PERIOD == 0;
    }

    private void sendSignInCoupon(String loginName, int signInCount) {
        SignInReward signInReward = getSignInReward(signInCount);
        if (SignInReward.NONE != signInReward) {
            couponAssignmentService.assignUserCoupon(loginName, signInReward.getCouponId());
            logger.info(MessageFormat.format("send {0} sign in coupon. couponId: {1}", loginName, signInReward.getCouponId()));
        }
        if (isFull(signInCount)) {
            couponAssignmentService.assignUserCoupon(loginName, SIGN_IN_FULL_COUPON_ID);
            logger.info(MessageFormat.format("send {0} sign in coupon. couponId: {1}", loginName, signInReward.getCouponId()));
        }
    }

    private String getCurrentRewardDesc(SignInPointDto signInPointDto) {
        if (null == signInPointDto) {
            return null;
        }
        SignInReward signInReward = getSignInReward(signInPointDto.getSignInCount());
        if (SignInReward.NONE != signInReward) {
            CouponModel couponModel = couponService.findExchangeableCouponById(signInReward.getCouponId());
            return MessageFormat.format("获得{0}元{1}", AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
        } else {
            return null;
        }
    }

    private String getNextRewardDesc(SignInPointDto signInPointDto) {
        if (null == signInPointDto) {
            return null;
        }
        SignInReward signInReward = getNextSignInReward(signInPointDto.getSignInCount());
        CouponModel couponModel = couponService.findExchangeableCouponById(signInReward.getCouponId());
        return MessageFormat.format("再签到{0}天可再获得{1}元{2}", signInReward.getDayLimit() - (signInPointDto.getSignInCount() % SIGN_IN_REWARD_PERIOD), AmountConverter.convertCentToString(couponModel.getAmount()), couponModel.getCouponType().getName());
    }
}
