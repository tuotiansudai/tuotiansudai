package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.util.InvestAchievementUserCollector;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponType;
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
    private UserBirthdayUtil userBirthdayUtil;

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

        if(StringUtils.isNotEmpty(exchangeCode) && userCouponModel == null){
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
            isAssignableCoupon = Lists.newArrayList(UserGroup.EXCHANGER, UserGroup.EXCHANGER_CODE, UserGroup.WINNER).contains(couponModel.getUserGroup()) || Iterables.all(existingUserCoupons, new Predicate<UserCouponModel>() {
                @Override
                public boolean apply(UserCouponModel input) {
                    return input.getStatus() == InvestStatus.SUCCESS;
                }
            });
        }

        if (isAssignableCoupon) {
            ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
            logger.debug(MessageFormat.format("[Coupon Assignment] assign user({0}) coupon({1})", loginName, String.valueOf(couponId)));
        }

    }

    @Override
    public void assignUserCoupon(String loginNameOrMobile, final List<UserGroup> userGroups) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        List<CouponModel> coupons = couponMapper.findAllActiveCoupons();

        final List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginNameAndCouponId(loginName, null);

        List<CouponModel> couponModels = Lists.newArrayList(Iterators.filter(coupons.iterator(), new Predicate<CouponModel>() {
            @Override
            public boolean apply(CouponModel couponModel) {
                boolean isInUserGroup = userGroups.contains(couponModel.getUserGroup())
                        && CouponAssignmentServiceImpl.this.getCollector(couponModel.getUserGroup()).contains(couponModel.getId(), loginName);
                boolean isAssignableCoupon = this.isAssignableCoupon(couponModel, userCouponModels);
                return isInUserGroup && isAssignableCoupon;
            }

            private boolean isAssignableCoupon(final CouponModel couponModel, List<UserCouponModel> existingUserCouponModels) {
                boolean isAssignableCoupon = CollectionUtils.isEmpty(existingUserCouponModels);
                if (CollectionUtils.isNotEmpty(existingUserCouponModels) && couponModel.isMultiple()) {
                    isAssignableCoupon = !Iterables.any(existingUserCouponModels, new Predicate<UserCouponModel>() {
                        @Override
                        public boolean apply(UserCouponModel input) {
                            return couponModel.getId() == input.getCouponId() && input.getStatus() != InvestStatus.SUCCESS;
                        }
                    });
                }
                return isAssignableCoupon;
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

        couponModel.setIssuedCount(couponModel.getIssuedCount() + 1);
        couponMapper.updateCoupon(couponModel);

        Date startTime = new DateTime().withTimeAtStartOfDay().toDate();
        Date endTime = couponModel.getDeadline() == 0 ? couponModel.getEndTime() : new DateTime().plusDays(couponModel.getDeadline() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate();
        if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) {
            DateTime userBirthday = userBirthdayUtil.getUserBirthday(loginName);
            startTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
            endTime = new DateTime().withMonthOfYear(userBirthday.getMonthOfYear()).dayOfMonth().withMaximumValue().withTime(23, 59, 59, 0).toDate();
        }
        UserCouponModel userCouponModel = new UserCouponModel(loginName, couponModel.getId(), startTime, endTime);
        if(StringUtils.isNotEmpty(exchangeCode)){
            int exchangeCodeCount = userCouponMapper.findByExchangeCode(exchangeCode);
            if(exchangeCodeCount > 0){
                logger.debug(MessageFormat.format("[Exchange Coupon:] Exchange{0} had been exchanged ",exchangeCode));
                return null;
            }
        }
        userCouponModel.setExchangeCode(exchangeCode);
        userCouponMapper.create(userCouponModel);
        return userCouponModel;
    }

    @Override
    public void assignUserCoupon(long loanId,String loginNameOrMobile,long couponId){
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

        boolean contains = investAchievementCollector.contains(couponModel.getId(),loanId, loginName,couponModel.getUserGroup());

        if (!contains) {
            logger.error(MessageFormat.format("[Coupon Assignment] user({0}) is not coupon({1}) user group({2})", loginName, String.valueOf(couponId), couponModel.getUserGroup()));
            return;
        }

        if (couponModel.isMultiple()) {
            UserCouponModel userCouponModel = ((CouponAssignmentService) AopContext.currentProxy()).assign(loginName, couponModel.getId(), null);
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
