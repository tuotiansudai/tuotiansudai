package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.dto.SmsCouponNotifyDto;
import com.tuotiansudai.job.CouponNotifyJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class CouponActivationServiceImpl implements CouponActivationService {

    static Logger logger = Logger.getLogger(CouponActivationServiceImpl.class);

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Transactional
    @Override
    public void inactive(String loginNameLoginName, long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (!couponModel.isActive() || (couponModel.getCouponType() != CouponType.NEWBIE_COUPON && couponModel.getCouponType() != CouponType.RED_ENVELOPE && couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON)) {
            return;
        }
        couponModel.setActive(false);
        couponModel.setActivatedBy(loginNameLoginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);
    }

    @Transactional
    @Override
    public void active(String operatorLoginName, long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (couponModel.isActive()) {
            return;
        }

        UserCollector collector = this.getCollector(couponModel.getUserGroup());

        couponModel.setTotalCount(collector.count(couponId));

        if (couponModel.getDeadline() != null) {
            Date now = new Date();
            couponModel.setStartTime(new DateTime(now).withTimeAtStartOfDay().toDate());
            couponModel.setEndTime(new DateTime(now).plusDays(couponModel.getDeadline()).withTimeAtStartOfDay().minusSeconds(1).toDate());
        }

        couponModel.setActive(true);
        couponModel.setActivatedBy(operatorLoginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);

        if (couponModel.isSmsAlert()) {
            this.createSmsNotifyJob(couponId);
        }
    }

    @Override
    public void sendSms(long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        List<String> loginNames = this.getCollector(couponModel.getUserGroup()).collect(couponModel.getId());

        SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
        notifyDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
        notifyDto.setRate(new BigDecimal(couponModel.getRate() * 100).setScale(0, BigDecimal.ROUND_UP).toString());
        notifyDto.setCouponType(couponModel.getCouponType());
        notifyDto.setExpiredDate(new DateTime(couponModel.getEndTime()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));

        for (String loginName : loginNames) {
            String mobile = userMapper.findByLoginName(loginName).getMobile();
            notifyDto.setMobile(mobile);
            try {
                smsWrapperClient.sendCouponNotify(notifyDto);
            } catch (Exception e) {
                logger.error(MessageFormat.format("Send coupon notify is failed (couponId = {0}, mobile = {1})", String.valueOf(couponId), mobile));
            }
        }
    }

    @Override
    @Transactional
    public void assignUserCoupon(String loginNameOrMobile, final List<UserGroup> userGroups) {
        final String loginName = userMapper.findByLoginNameOrMobile(loginNameOrMobile).getLoginName();

        List<CouponModel> coupons;

        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        if (Integer.parseInt(accountModel.getIdentityNumber().substring(10, 12)) != new DateTime().getMonthOfYear()) {
            coupons = couponMapper.findAllActiveCouponsNotExistsBirthday();
        } else {
            coupons = couponMapper.findAllActiveCoupons();
        }

        List<CouponModel> couponModels = Lists.newArrayList(Iterators.filter(coupons.iterator(), new Predicate<CouponModel>() {
            @Override
            public boolean apply(CouponModel couponModel) {
                boolean isInUserGroup = userGroups.contains(couponModel.getUserGroup())
                        && CouponActivationServiceImpl.this.getCollector(couponModel.getUserGroup()).contains(couponModel.getId(), loginName);
                List<UserCouponModel> existingUserCouponModels = userCouponMapper.findByLoginNameAndCouponId(loginName, couponModel.getId());
                boolean hasNoUsableCoupon = CollectionUtils.isEmpty(existingUserCouponModels);
                if (CollectionUtils.isNotEmpty(existingUserCouponModels) && couponModel.isMultiple()) {
                    hasNoUsableCoupon = Iterables.all(existingUserCouponModels, new Predicate<UserCouponModel>() {
                        @Override
                        public boolean apply(UserCouponModel input) {
                            return input.getStatus() == InvestStatus.SUCCESS;
                        }
                    });
                }
                return isInUserGroup && hasNoUsableCoupon;
            }
        }));

        for (CouponModel couponModel : couponModels) {
            CouponModel lockedCoupon = couponMapper.lockById(couponModel.getId());
            lockedCoupon.setIssuedCount(couponModel.getIssuedCount() + 1);
            couponMapper.updateCoupon(lockedCoupon);
            UserCouponModel userCouponModel = new UserCouponModel(loginName, couponModel.getId());
            userCouponMapper.create(userCouponModel);
        }
    }

    private UserCollector getCollector(UserGroup userGroup) {
        return Maps.newHashMap(ImmutableMap.<UserGroup, UserCollector>builder()
                .put(UserGroup.ALL_USER, this.allUserCollector)
                .put(UserGroup.NEW_REGISTERED_USER, this.newRegisteredUserCollector)
                .put(UserGroup.INVESTED_USER, this.investedUserCollector)
                .put(UserGroup.REGISTERED_NOT_INVESTED_USER, this.registeredNotInvestedUserCollector)
                .put(UserGroup.IMPORT_USER, this.importUserCollector)
                .build()).get(userGroup);
    }

    private void createSmsNotifyJob(long couponId) {
        try {
            Date oneMinuteLater = new DateTime().plusMinutes(1).toDate();
            jobManager.newJob(JobType.CouponNotify, CouponNotifyJob.class)
                    .runOnceAt(oneMinuteLater)
                    .addJobData(CouponNotifyJob.COUPON_ID, couponId)
                    .withIdentity(JobType.CouponNotify.name(), MessageFormat.format("Coupon-Notify-{0}", String.valueOf(couponId)))
                    .submit();
        } catch (Exception e) {
            logger.error(MessageFormat.format("Create coupon notify job failed (couponId = {0})", String.valueOf(couponId)), e);
        }
    }
}
