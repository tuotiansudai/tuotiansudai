package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.activity.repository.model.DragonBoatPunchCardPrize;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DragonBoatFestivalService {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatFestivalService.class);

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String DRAGON_BOAT_EXCHANGE_FETCH = "dragon_boat_exchange_fetch:{0}:{1}";

    private static final int TWO_MONTH_SECONDS = 60 * 60 * 24 * 60;

    private static final String DRAGON_BOAT_SHARE_EXPERIENCE_PRIZE = "dragon_boat_share_experience_prize:{0}:{1}";

    private static final String DRAGON_BOAT_SHARE_COUPON_FETCH = "dragon_boat_share_coupon_fetch:{0}:{1}";

    private static final String DRAGON_BOAT_SHARE_UNIQUE_CODE = "dragon_boat_share_unique_code:{0}";

    private static final ThreadLocal<SimpleDateFormat> SDF_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("MM-dd"));

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.dragon.boat.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.dragon.boat.endTime}\")}")
    private Date endTime;


    public boolean inActivityPeriod() {
        Date now = new Date();
        if (now.before(startTime)) {
            logger.info("[Dragon boat festival] dragon boat activity has not started yet.");
            return false;
        } else if (now.after(endTime)) {
            logger.info("[Dragon boat festival] dragon boat activity is end.");
            return false;
        }
        return true;
    }

    public String getCouponExchangeCode(String loginName) {
        logger.info("[Dragon boat festival] {} is fetching the prize.", loginName);

        if (StringUtils.isEmpty(loginName)) {
            logger.info("[Dragon boat festival] user is not login.");
            return null;
        }

        if (!inActivityPeriod()) {
            return null;
        }

        String date = SDF_LOCAL.get().format(new Date());

        String key = MessageFormat.format(DRAGON_BOAT_EXCHANGE_FETCH, date, loginName);

        if (redisWrapperClient.exists(key)) {
            logger.info("[Dragon boat festival] {} has fetched the prize today, can't fetch anymore.", loginName);
            String exchangeCode = redisWrapperClient.get(key);
            String shareUniqueCode = exchangeCode.substring(4, 10);
            return exchangeCode + ":" + shareUniqueCode;
        }

        int random = (int) (Math.random() * 100000);
        int mod = random % 100;

        DragonBoatPunchCardPrize prize;

        if (mod < 20) {
            prize = DragonBoatPunchCardPrize.RedEnveloper5;
        } else if (mod < 35) {
            prize = DragonBoatPunchCardPrize.RedEnveloper8;
        } else if (mod < 50) {
            prize = DragonBoatPunchCardPrize.RedEnveloper10;
        } else if (mod < 70) {
            prize = DragonBoatPunchCardPrize.RedEnveloper18;
        } else if (mod < 90) {
            prize = DragonBoatPunchCardPrize.RedEnveloper28;
        } else {
            prize = DragonBoatPunchCardPrize.InterestCoupon5;
        }

        String exchangeCode = redisWrapperClient.lpop(ExchangeCodeService.EXCHANGE_CODE_LIST_KEY + prize.getCouponId());
        if (exchangeCode == null) {
            logger.error("[Dragon boat festival] exchange code is used done, coupon id:{}", prize.getCouponId());
            return null;
        }
        redisWrapperClient.setex(key, TWO_MONTH_SECONDS, exchangeCode);

        // 设置分享标识码，后续领券时最安全判断，防止恶意刷券
        String shareUniqueCode = exchangeCode.substring(4, 10);
        redisWrapperClient.setex(MessageFormat.format(DRAGON_BOAT_SHARE_UNIQUE_CODE, shareUniqueCode), TWO_MONTH_SECONDS, "1");

        logger.info("[Dragon boat festival] {} draw a prize:{}, exchange code:{}", loginName, prize, exchangeCode);

        return exchangeCode + ":" + shareUniqueCode;
    }

    public int sendCouponAfterRegisterOrLogin(String loginName, String shareUniqueCode) {

        if (!inActivityPeriod()) {
            logger.info("[Dragon Boat Register or Login {}] not in activity period.", loginName);
            return -1;
        }

        if (StringUtils.isEmpty(loginName)) {
            logger.info("[Dragon Boat][Send Coupon After Register or Login] user not login.", loginName);
            return 0;
        }

        String shareUniqueKey = MessageFormat.format(DRAGON_BOAT_SHARE_UNIQUE_CODE, shareUniqueCode);

        String couponFetchKey = MessageFormat.format(DRAGON_BOAT_SHARE_COUPON_FETCH, shareUniqueCode, loginName);

        // shareUniqueKey存在，且couponFetchKey不存在，表示：shareUniqueCode是正确的，且未被此用户领取过。
        if (redisWrapperClient.exists(shareUniqueKey) && redisWrapperClient.setnx(couponFetchKey, "1")) {
            // 给新用户发10元红包优惠券
            logger.info("[Dragon Boat Register User {}] assign weiXin share ￥10 red enveloper ", loginName);
            couponAssignmentService.assignUserCoupon(loginName, 421);
            redisWrapperClient.expire(couponFetchKey, TWO_MONTH_SECONDS);
            return 1;
        } else {
            return 0;
        }
    }

    public void afterNewUserRegister(String registerUserMobile, String referrer) {

        if (!inActivityPeriod()) {
            logger.info("[Dragon Boat Register User {}] not in activity period.", registerUserMobile);
            return;
        }

        // 给分享者增加邀请用户数量
        logger.info("[Dragon Boat Register User {}] add invite-new-user-count for {}", registerUserMobile, referrer);
        addInviteNewUserCount(referrer);

        // 给分享者发放5000元体验金， 每天每人最多领取5次。
        String date = SDF_LOCAL.get().format(new Date());
        if (redisWrapperClient.incrEx(MessageFormat.format(DRAGON_BOAT_SHARE_EXPERIENCE_PRIZE, date, referrer), TWO_MONTH_SECONDS) > 5) {
            logger.info("[Dragon boat Register User {}] {} has won 5 exprience prize today, can't won anymore.", registerUserMobile, referrer);
            return;
        } else {
            logger.info("[Dragon Boat Register User {}] send ￥5000 experience to {} for inviting new user.", registerUserMobile, referrer);
            mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                    new ExperienceAssigningMessage(referrer, 500000, ExperienceBillOperationType.IN, ExperienceBillBusinessType.DRAGON_BOAT_INVITE_NEW_USER));
            // 记录邀请用户获取的体验金
            addInviteExperienceAmount(referrer, 500000);
        }
    }

    private void addInviteNewUserCount(String loginName) {
        logger.info("[Dragon boat festival] add a new user count for user {}", loginName);
        UserModel userModel = userMapper.findByLoginName(loginName);
        dragonBoatFestivalMapper.addInviteNewUserCount(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
    }


    private void addInviteExperienceAmount(String loginName, long experience) {
        logger.info("[Dragon boat festival] add invite experience amount for user {}, experience amount:{}", loginName, experience);
        UserModel userModel = userMapper.findByLoginName(loginName);
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
        dragonBoatFestivalModel.setInviteExperienceAmount(experience);
        dragonBoatFestivalMapper.addInviteExperienceAmount(dragonBoatFestivalModel);
    }

    public String joinPK(String loginName, String group) {

        if (!inActivityPeriod()) {
            logger.info("[Dragon Boat][Join PK] not in activity period. loginName: {}", loginName);
            return "GAME_OVER";
        }

        String joinedGroup = getGroupByLoginName(loginName);
        if (joinedGroup != null) {
            logger.info("[Dragon boat] {} has joined PK, can't join again.", loginName);
            return joinedGroup; // 用户已经选择过派别了，不能再次选择
        }
        UserModel userModel = userMapper.findByLoginName(loginName);
        DragonBoatFestivalModel dragonBoatFestivalModel = new DragonBoatFestivalModel(userModel.getLoginName(), userModel.getUserName(), userModel.getMobile());
        dragonBoatFestivalModel.setPkGroup(group);

        logger.info("[Dragon boat] {} is joining {} group.", loginName, group);
        dragonBoatFestivalMapper.setPKGroup(dragonBoatFestivalModel);
        return "SUCCESS";
    }

    public String getGroupByLoginName(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return null; // 没有登录，返回null
        }
        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName(loginName);
        return model == null ? null : model.getPkGroup();
    }

    public long getGroupPKInvestAmount(String group) {
        return dragonBoatFestivalMapper.getGroupPKInvestAmount(group);
    }

    public long getGroupSupportCount(String group) {
        return dragonBoatFestivalMapper.getGroupSupportCount(group);
    }

    public long getActivityInvestAmount(String loginName) {

        if (StringUtils.isEmpty(loginName)) {
            return 0; //若没有登录，则返回0
        }
        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName(loginName);
        long investAmount = model == null ? 0 : model.getTotalInvestAmount();
        return investAmount;
    }

    public int getChampagnePrizeLevel(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return 0; //若没有登录，则返回0
        }
        DragonBoatFestivalModel model = dragonBoatFestivalMapper.findByLoginName(loginName);
        long investAmount = model == null ? 0 : model.getTotalInvestAmount();
        int level = 0;
        if (investAmount >= 60000000) {
            level = 5;
        } else if (investAmount >= 30000000) {
            level = 4;
        } else if (investAmount >= 12000000) {
            level = 3;
        } else if (investAmount >= 6000000) {
            level = 2;
        } else if (investAmount >= 500000) {
            level = 1;
        }
        return level;
    }


}
