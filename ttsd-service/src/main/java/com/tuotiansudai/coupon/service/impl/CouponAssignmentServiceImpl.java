package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.util.InvestAchievementUserCollector;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.UserBirthdayUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.stream.Stream;

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

    @Resource(name = "experienceInvestSuccessCollector")
    private UserCollector experienceInvestSuccessCollector;

    @Resource(name = "membershipUserCollector")
    private UserCollector membershipUserCollector;

    @Resource(name = "experienceRepaySuccessCollector")
    private UserCollector experienceRepaySuccessCollector;

    @Resource(name = "exchangeCodeCollector")
    private UserCollector exchangeCodeCollector;

    @Resource(name = "notAccountNotInvestedUserCollector")
    private UserCollector notAccountNotInvestedUserCollector;

    @Resource(name = "investAchievementCollector")
    private InvestAchievementUserCollector investAchievementCollector;

    @Override
    public boolean assignUserCoupon(String loginNameOrMobile, String exchangeCode) {
        String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        long couponId = exchangeCodeService.getValueBase31(exchangeCode);
        CouponModel couponModel = couponMapper.findById(couponId);

        if (couponModel == null) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) is not correct", exchangeCode));
            return false;
        }

        if (!couponModel.isActive() || couponModel.getEndTime().before(new Date())) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) exchange coupon({1}) is inactive", exchangeCode, String.valueOf(couponId)));
            return false;
        }

        if (couponModel.getUserGroup() != UserGroup.EXCHANGER_CODE) {
            logger.error(MessageFormat.format("[Exchange Coupon] code({0}) coupon({1}) user group({2}) is not EXCHANGER_CODE", exchangeCode, String.valueOf(couponId), couponModel.getUserGroup()));
            return false;
        }


        UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), exchangeCode);

        if (userCouponModel == null) {
            return false;
        }

        logger.debug(MessageFormat.format("[Exchange Coupon] user({0}) exchange coupon({1}) with code({2})", loginName, String.valueOf(couponId), exchangeCode));
        return true;
    }

    @Override
    public void assignUserCoupon(String loginNameOrMobile, long couponId) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

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

        boolean contains = collector.contains(couponId, loginName);
        if (!contains) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) is not coupon({1}) user group({2})", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return;
        }

        List<UserCouponModel> existingUserCoupons = userCouponMapper.findByLoginNameAndCouponId(loginName, couponModel.getId());

        boolean isAssignableCoupon = CollectionUtils.isEmpty(existingUserCoupons);

        if (CollectionUtils.isNotEmpty(existingUserCoupons) && couponModel.isMultiple()) {
            isAssignableCoupon = Lists.newArrayList(UserGroup.EXCHANGER, UserGroup.EXCHANGER_CODE, UserGroup.WINNER).contains(couponModel.getUserGroup())
                    || existingUserCoupons.stream().allMatch(input -> input.getStatus() == InvestStatus.SUCCESS);
        }

        if (isAssignableCoupon) {
            UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
            if (userCouponModel == null) {
                return;
            }
            logger.debug(MessageFormat.format("[Coupon Assignment] assign user({0}) coupon({1})", loginName, String.valueOf(couponId)));
        }

    }

    /**
     * 给指定的用户发放指定的 userGroup 类型的优惠券
     *
     * @param loginNameOrMobile 指定的用户
     * @param userGroups        指定的userGroup
     */
    @Override
    public void assignUserCoupon(String loginNameOrMobile, final List<UserGroup> userGroups) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName().toLowerCase();

        // 当前可领取的优惠券
        List<CouponModel> coupons = couponMapper.findAllActiveCoupons();

        // 该用户已领取的优惠券
        final List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginNameAndCouponId(loginName, null);

        // 该用户可领取的优惠券
        List<CouponModel> couponModels = Lists.newArrayList(Iterators.filter(coupons.iterator(), new Predicate<CouponModel>() {
            @Override
            public boolean apply(CouponModel couponModel) {

                // 该优惠券在参数指定的userGroup里  且 该优惠券和该用户符合该userGroup的规则
                boolean isInUserGroup = userGroups.contains(couponModel.getUserGroup())
                        && CouponAssignmentServiceImpl.this.getCollector(couponModel.getUserGroup()).contains(couponModel.getId(), loginName);

                // 该优惠券是否可以被发放给该用户
                boolean isAssignableCoupon = this.isAssignableCoupon(couponModel);

                return isInUserGroup && isAssignableCoupon;
            }

            // 用户已经持有的该类型的优惠券的数量
            int assignedCouponCount;

            /**
             * 检查优惠券是否可以被发送给该用户
             * 如果用户没有持有该类型的优惠券，则返回true
             * 否则，如果用户持有的该类型的优惠券都被使用过了，且该优惠券可以被多次领取，则返回true
             *
             * @param couponModel
             * @return
             */
            private boolean isAssignableCoupon(final CouponModel couponModel) {

                assignedCouponCount = 0;

                // 用户持有的该类型的优惠券（可能为多个）
                Stream<UserCouponModel> assignedUserCoupons = userCouponModels.stream().filter(input -> input.getCouponId() == couponModel.getId());

                // 是否存在未使用的该类型优惠券
                boolean isUnusedUserCouponExisted = assignedUserCoupons.anyMatch(input -> {
                    assignedCouponCount++;
                    return input.getStatus() != InvestStatus.SUCCESS;
                });

                // 如果用户没有持有该类型的优惠券，则该用户可以得到该优惠券，返回true
                if (assignedCouponCount == 0) {
                    return true;
                }

                // 该优惠券可以被多次领取（目前只有生日券）且 用户持有的该优惠券已经被全部使用了，则返回true，表示该优惠券还可以被该用户再次领取
                return couponModel.isMultiple() && !isUnusedUserCouponExisted;
            }
        }));

        for (CouponModel couponModel : couponModels) {
            ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
        }
    }

    @Transactional
    @Override
    public UserCouponModel assign(String loginName, long couponId, String exchangeCode) {
        CouponModel couponModel = couponMapper.lockById(couponId);

        List<UserCouponModel> assignedUserCoupons = userCouponMapper.findByLoginNameAndCouponId(loginName, couponId);
        if (!couponModel.isMultiple() && CollectionUtils.isNotEmpty(assignedUserCoupons)) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) has been assigned to user({1})", String.valueOf(couponId), loginName));
            return null;
        }

        couponModel.setIssuedCount(couponModel.getIssuedCount() + 1);
        couponMapper.updateCoupon(couponModel);

        Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
        Date endTime = couponModel.getDeadline() == 0 ? couponModel.getEndTime() : new DateTime().plusDays(couponModel.getDeadline() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate();
        if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) {
            DateTime userBirthday = UserBirthdayUtil.getUserBirthday(userMapper.findByLoginName(loginName).getIdentityNumber());
            startTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
            endTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMaximumValue().withTime(23, 59, 59, 0).toDate();
        }
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponModel.getId(), startTime, endTime);
        if (StringUtils.isNotEmpty(exchangeCode)) {
            int exchangeCodeCount = userCouponMapper.findByExchangeCode(exchangeCode);
            if (exchangeCodeCount > 0) {
                logger.debug(MessageFormat.format("[Exchange Coupon:] Exchange{0} had been exchanged ", exchangeCode));
                return null;
            }
        }
        userCouponModel.setExchangeCode(exchangeCode);
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }

    @Override
    public void assignInvestAchievementUserCoupon(long loanId, String loginNameOrMobile, long couponId) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        CouponModel couponModel = couponMapper.findById(couponId);

        if (couponModel == null) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is not exist", String.valueOf(couponId)));
            return;
        }

        if (!couponModel.isActive() || couponModel.getEndTime().before(new Date())) {
            logger.error(MessageFormat.format("[Coupon Assignment] coupon({0}) is inactive", String.valueOf(couponId)));
            return;
        }

        boolean contains = investAchievementCollector.contains(couponModel.getId(), loanId, loginName, couponModel.getUserGroup());

        if (!contains) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) is not coupon({1}) user group({2})", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return;
        }

        if (couponModel.isMultiple()) {
            UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
            if (userCouponModel == null) {
                return;
            }
            userCouponModel.setAchievementLoanId(loanId);
            userCouponMapper.update(userCouponModel);
            logger.debug(MessageFormat.format("[Coupon Assignment] assign user({0}) coupon({1})", loginName, String.valueOf(couponId)));
        }
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
                .put(UserGroup.EXCHANGER_CODE, this.exchangeCodeCollector)
                .put(UserGroup.EXPERIENCE_INVEST_SUCCESS, this.experienceInvestSuccessCollector)
                .put(UserGroup.EXPERIENCE_REPAY_SUCCESS, this.experienceRepaySuccessCollector)
                .put(UserGroup.MEMBERSHIP_V0, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V1, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V2, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V3, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V4, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V5, this.membershipUserCollector)
                .build()).get(userGroup);
    }
}
