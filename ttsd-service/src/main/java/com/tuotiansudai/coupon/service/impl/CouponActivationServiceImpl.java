package com.tuotiansudai.coupon.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.ExchangeCodeService;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.job.CouponNotifyJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.AuditLogUtil;
import com.tuotiansudai.util.JobManager;
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

    @Resource(name = "notAccountNotInvestedUserCollector")
    private UserCollector notAccountNotInvestedUserCollector;

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

    @Resource(name = "exchangeCodeCollector")
    private UserCollector exchangeCodeCollector;

    @Resource(name = "experienceInvestSuccessCollector")
    private UserCollector experienceInvestSuccessCollector;

    @Resource(name = "experienceRepaySuccessCollector")
    private UserCollector experienceRepaySuccessCollector;

    @Resource(name = "membershipUserCollector")
    private UserCollector membershipUserCollector;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AuditLogUtil auditLogUtil;

    @Autowired
    private ExchangeCodeService exchangeCodeService;

    @Transactional
    @Override
    public void inactive(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (!couponModel.isActive() || (couponModel.getCouponType() != CouponType.NEWBIE_COUPON && couponModel.getCouponType() != CouponType.RED_ENVELOPE && couponModel.getCouponType() != CouponType.BIRTHDAY_COUPON)) {
            return;
        }
        couponModel.setActive(false);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);

        UserModel auditor = userMapper.findByLoginName(loginName);
        String auditorRealName = auditor == null ? loginName : auditor.getUserName();

        UserModel operator = userMapper.findByLoginName(couponModel.getCreatedBy());
        String operatorRealName = operator == null ? couponModel.getCreatedBy() : operator.getUserName();

        String description = MessageFormat.format("{0} 撤销了 {1} 创建的 {2}", auditorRealName, operatorRealName, couponModel.getCouponType().getName());
        auditLogUtil.createAuditLog(loginName, couponModel.getCreatedBy(), OperationType.COUPON, String.valueOf(couponId), description, ip);
    }

    @Transactional
    @Override
    public void exchangeInactive(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        couponModel.setActive(false);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);
    }

    @Transactional
    @Override
    public void active(String loginName, long couponId, String ip) {
        CouponModel couponModel = couponMapper.findById(couponId);
        if (couponModel.isActive()) {
            return;
        }

        couponModel.setActive(true);
        couponModel.setActivatedBy(loginName);
        couponModel.setActivatedTime(new Date());
        couponMapper.updateCoupon(couponModel);

        UserModel auditor = userMapper.findByLoginName(loginName);
        String auditorRealName = auditor == null ? loginName : auditor.getUserName();

        UserModel operator = userMapper.findByLoginName(couponModel.getCreatedBy());
        String operatorRealName = operator == null ? couponModel.getCreatedBy() : operator.getUserName();

        if (couponModel.getUserGroup() == UserGroup.EXCHANGER_CODE) {
            exchangeCodeService.generateExchangeCode(couponModel.getId(), couponModel.getTotalCount().intValue());
        }

        if (couponModel.isSmsAlert()) {
            this.createSmsNotifyJob(couponId);
        }

        String description = MessageFormat.format("{0} 激活了 {1} 创建的 {2}", auditorRealName, operatorRealName, couponModel.getCouponType().getName());
        auditLogUtil.createAuditLog(loginName, couponModel.getCreatedBy(), OperationType.COUPON, String.valueOf(couponId), description, ip);
    }

    @Override
    public void sendSms(long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        List<String> loginNames = this.getCollector(couponModel.getUserGroup()).collect(couponModel.getId());

        SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
        notifyDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
        notifyDto.setRate(new BigDecimal(couponModel.getRate() * 100).setScale(0, BigDecimal.ROUND_UP).toString());
        notifyDto.setCouponType(couponModel.getCouponType());
        notifyDto.setExpiredDate(DateTime.now().plusDays(couponModel.getDeadline()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));

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
