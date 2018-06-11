package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.util.InvestAchievementUserCollector;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.message.CouponAssignSmsNotifyMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponAssignmentServiceImpl implements CouponAssignmentService {

    private static Logger logger = Logger.getLogger(CouponAssignmentServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Resource(name = "allUserCollector")
    private UserCollector allUserCollector;

    @Resource(name = "newRegisteredUserCollector")
    private UserCollector newRegisteredUserCollector;

    @Resource(name = "investedUserCollector")
    private UserCollector investedUserCollector;

    @Resource(name = "registeredNotInvestedUserCollector")
    private UserCollector registeredNotInvestedUserCollector;

    @Resource(name = "importUserCollector")
    private UserCollector importUserCollector;

    @Resource(name = "agentCollector")
    private UserCollector agentCollector;

    @Resource(name = "staffCollector")
    private UserCollector staffCollector;

    @Resource(name = "channelCollector")
    private UserCollector channelCollector;

    @Resource(name = "staffRecommendLevelOneCollector")
    private UserCollector staffRecommendLevelOneCollector;

    @Resource(name = "exchangerCollector")
    private UserCollector exchangerCollector;

    @Resource(name = "winnerCollector")
    private UserCollector winnerCollector;

    @Resource(name = "winnerNotifyCollector")
    private UserCollector winnerNotifyCollector;

    @Resource(name = "membershipUserCollector")
    private UserCollector membershipUserCollector;

    @Resource(name = "exchangeCodeCollector")
    private UserCollector exchangeCodeCollector;

    @Resource(name = "notAccountNotInvestedUserCollector")
    private UserCollector notAccountNotInvestedUserCollector;

    @Resource(name = "investAchievementCollector")
    private InvestAchievementUserCollector investAchievementCollector;

    @Override
    public UserCouponModel assignUserCoupon(String loginNameOrMobile, String exchangeCode) {
        String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        long couponId = exchangeCodeService.getValueBase31(exchangeCode);
        CouponModel couponModel = couponMapper.findById(couponId);

        if (couponModel == null) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) is not correct", exchangeCode));
            return null;
        }

        if (!couponModel.isActive() || couponModel.getEndTime().before(new Date())) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) exchange coupon({1}) is inactive", exchangeCode, String.valueOf(couponId)));
            return null;
        }

        if (couponModel.getUserGroup() != UserGroup.EXCHANGER_CODE) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) coupon({1}) user group({2}) is not EXCHANGER_CODE", exchangeCode, String.valueOf(couponId), couponModel.getUserGroup()));
            return null;
        }

        UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), exchangeCode);

        if (userCouponModel != null) {
            logger.info(MessageFormat.format("[Exchange Coupon] user({0}) exchange coupon({1}) with code({2})", loginName, String.valueOf(couponId), exchangeCode));
        }

        return userCouponModel;
    }

    @Override
    public void assignUserCoupon(String loginNameOrMobile, long couponId) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);

        if (userModel == null) {
            logger.error(MessageFormat.format("[Coupon Assignment] user ({0}) is not exist", loginNameOrMobile));
            return;
        }

        final String loginName = userModel.getLoginName();

        CouponModel couponModel = couponMapper.findById(couponId);

        if (couponModel == null) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is not exist", String.valueOf(couponId)));
            return;
        }

        if (!couponModel.isActive() || couponModel.getEndTime().before(new Date())) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is inactive", String.valueOf(couponId)));
            return;
        }

        UserCollector collector = getCollector(couponModel.getUserGroup());

        if (collector == null) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) coupon({1}) user group({2}) collector not found", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return;
        }

        boolean contains = collector.contains(couponModel, userModel);
        if (!contains) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) is not coupon({1}) user group({2})", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return;
        }

        List<UserCouponModel> existingUserCoupons = userCouponMapper.findByLoginNameAndCouponId(loginName, couponModel.getId());

        boolean isAssignableCoupon = CollectionUtils.isEmpty(existingUserCoupons);

        if (CollectionUtils.isNotEmpty(existingUserCoupons) && couponModel.isMultiple()) {
            isAssignableCoupon = Lists.newArrayList(UserGroup.EXCHANGER, UserGroup.EXCHANGER_CODE, UserGroup.WINNER, UserGroup.WINNER_NOTIFY).contains(couponModel.getUserGroup())
                    || existingUserCoupons.stream().allMatch(input -> input.getStatus() == InvestStatus.SUCCESS);
        }

        if (isAssignableCoupon) {
            UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
            if (userCouponModel == null) {
                return;
            }
            logger.info(MessageFormat.format("[Coupon Assignment] assign user({0}) coupon({1})", loginName, String.valueOf(couponId)));
        }

    }

    /**
     * 异步给指定的用户发放指定的 userGroup 类型的优惠券
     *
     * @param loginNameOrMobile 指定的用户
     * @param userGroups        指定的userGroup
     */
    @Override
    public List<CouponModel> asyncAssignUserCoupon(String loginNameOrMobile, final List<UserGroup> userGroups) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        final String loginName = userModel.getLoginName();

        // 当前可领取的优惠券
        List<CouponModel> coupons = couponMapper.findAllActiveCoupons();

        // 该用户已领取的优惠券
        final List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginNameAndCouponId(loginName, null);

        return coupons.stream()
                // 该用户可领取的优惠券
                // 该优惠券在参数指定的userGroup里
                .filter(couponModel -> userGroups.contains(couponModel.getUserGroup()))
                // 该优惠券是否可以被发放给该用户（数量上检查）
                .filter(couponModel -> isAssignableCoupon(couponModel, userCouponModels))
                // 该优惠券和该用户符合该userGroup的规则（资格上检查）
                // 此处特意将资格检查放在数量检查后面，以提高处理效率
                .filter(couponModel -> getCollector(couponModel.getUserGroup()).contains(couponModel, userModel))
                // 生成MQ消息内容
                .map(couponModel -> sendCouponAssignMessage(couponModel, loginName))
                .collect(Collectors.toList());
                /*.map(couponModel -> loginName + ":" + couponModel.getId())
                // 发送MQ消息
                .forEach(message -> mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, message));
                */
    }

    private CouponModel sendCouponAssignMessage(CouponModel couponModel, String loginName) {
        mqWrapperClient.sendMessage(MessageQueue.Coupon_Assigning, loginName + ":" + couponModel.getId());
        return couponModel;
    }

    /**
     * 检查优惠券是否可以被发送给该用户
     * 如果用户没有持有该类型的优惠券，则返回true
     * 否则，如果用户持有的该类型的优惠券都被使用过了，且该优惠券可以被多次领取，则返回true
     */
    private boolean isAssignableCoupon(CouponModel couponModel, List<UserCouponModel> userCouponModels) {
        // 列出用户已持有的该类型的优惠券
        List<UserCouponModel> existingUserCouponList = userCouponModels.stream()
                .filter(userCoupon -> userCoupon.getCouponId() == couponModel.getId())
                .collect(Collectors.toList());

        // 如果用户没有持有该类型的优惠券，则该用户可以得到该优惠券，返回true
        if (existingUserCouponList.isEmpty()) {
            return true;
        } else {
            // 该优惠券可以被多次领取（目前只有生日券）而且全部都使用过了
            return couponModel.isMultiple() &&
                    existingUserCouponList.stream().allMatch(userCoupon -> userCoupon.getStatus() == InvestStatus.SUCCESS);
        }
    }

    @Transactional
    @Override
    public UserCouponModel assign(String loginName, long couponId, String exchangeCode) {
        CouponModel couponModel = couponMapper.lockById(couponId);

        if (!Strings.isNullOrEmpty(exchangeCode) && userCouponMapper.findByExchangeCode(exchangeCode) > 0) {
            logger.info(MessageFormat.format("[Exchange Coupon:] Exchange{0} had been exchanged ", exchangeCode));
            return null;
        }

        couponModel.setIssuedCount(couponModel.getIssuedCount() + 1);
        couponMapper.updateCoupon(couponModel);

        Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
        Date endTime = couponModel.getDeadline() == 0 ? couponModel.getEndTime() : new DateTime().plusDays(couponModel.getDeadline() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate();
        DateTime userBirthday = UserBirthdayUtil.getUserBirthday(userMapper.findByLoginName(loginName).getIdentityNumber());
        if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && userBirthday != null) {
            startTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
            endTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMaximumValue().withTime(23, 59, 59, 0).toDate();
        }

        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponModel.getId(), startTime, endTime);
        userCouponModel.setExchangeCode(exchangeCode);
        userCouponMapper.create(userCouponModel);

        if (Lists.newArrayList(UserGroup.IMPORT_USER, UserGroup.WINNER_NOTIFY).contains(couponModel.getUserGroup())) {
            mqWrapperClient.sendMessage(MessageQueue.CouponSmsAssignNotify, new CouponAssignSmsNotifyMessage(couponId, loginName));
        }

        return userCouponModel;
    }

    @Override
    public boolean assignInvestAchievementUserCoupon(String loginNameOrMobile, long loanId, long couponId) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        CouponModel couponModel = couponMapper.findById(couponId);

        if (couponModel == null) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is not exist", String.valueOf(couponId)));
            return false;
        }

        if (!couponModel.isActive() || couponModel.getEndTime().before(new Date())) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is inactive", String.valueOf(couponId)));
            return false;
        }

        boolean contains = investAchievementCollector.contains(couponModel.getId(), loanId, loginName, couponModel.getUserGroup());

        if (!contains) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) is not coupon({1}) user group({2})", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return false;
        }

        if (couponModel.isMultiple()) {
            UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
            if (userCouponModel == null) {
                return false;
            }
            userCouponModel.setAchievementLoanId(loanId);
            userCouponMapper.update(userCouponModel);
            logger.info(MessageFormat.format("[Coupon Assignment] assign user({0}) coupon({1})", loginName, String.valueOf(couponId)));
        }
        return true;
    }

    private UserCollector getCollector(UserGroup userGroup) {
        return Maps.newHashMap(ImmutableMap.<UserGroup, UserCollector>builder()
                .put(UserGroup.ALL_USER, this.allUserCollector)
                .put(UserGroup.NEW_REGISTERED_USER, this.newRegisteredUserCollector)
                .put(UserGroup.INVESTED_USER, this.investedUserCollector)
                .put(UserGroup.REGISTERED_NOT_INVESTED_USER, this.registeredNotInvestedUserCollector)
                .put(UserGroup.NOT_ACCOUNT_NOT_INVESTED_USER, this.notAccountNotInvestedUserCollector)
                .put(UserGroup.IMPORT_USER, this.importUserCollector)
                .put(UserGroup.AGENT, this.agentCollector)
                .put(UserGroup.CHANNEL, this.channelCollector)
                .put(UserGroup.STAFF, this.staffCollector)
                .put(UserGroup.STAFF_RECOMMEND_LEVEL_ONE, this.staffRecommendLevelOneCollector)
                .put(UserGroup.EXCHANGER, this.exchangerCollector)
                .put(UserGroup.WINNER, this.winnerCollector)
                .put(UserGroup.WINNER_NOTIFY, this.winnerNotifyCollector)
                .put(UserGroup.EXCHANGER_CODE, this.exchangeCodeCollector)
                .put(UserGroup.MEMBERSHIP_V0, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V1, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V2, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V3, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V4, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V5, this.membershipUserCollector)
                .build()).get(userGroup);
    }
}
