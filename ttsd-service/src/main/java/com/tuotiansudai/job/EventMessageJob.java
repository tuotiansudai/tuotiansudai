package com.tuotiansudai.job;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class EventMessageJob implements Job {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    static Logger logger = Logger.getLogger(EventMessageJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.birthdayMessage();
        this.membershipExpiredMessage();
        this.couponExpiredAfterFiveDays();
    }

    private void birthdayMessage() {
        List<String> birthDayUsers = userMapper.findBirthDayUsers();
        //Title:拓天速贷为您送上生日祝福，请查收！
        //AppTitle:拓天速贷为您送上生日祝福，请查收！
        //Content:尊敬的{0}先生/女士，我猜今天是您的生日，拓天速贷在此送上真诚的祝福，生日当月投资即可享受收益翻倍哦！
        String title = MessageEventType.BIRTHDAY.getTitleTemplate();
        birthDayUsers.forEach(loginName -> {
            String userName = userMapper.findByLoginName(loginName).getUserName();
            String content = MessageFormat.format(MessageEventType.BIRTHDAY.getContentTemplate(), userName);
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.BIRTHDAY, Lists.newArrayList(loginName), title, content, null));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(loginName), PushSource.ALL, PushType.BIRTHDAY, title));
        });
    }

    private void membershipExpiredMessage() {
        List<String> membershipExpiredUsers = userMembershipMapper.findLevelFiveMembershipExpiredUsers();
        if (CollectionUtils.isEmpty(membershipExpiredUsers)) {
            logger.info("[EventMessageJob] today is no user whose membership is expired");
            return;
        }
        //Title:您的V5会员已到期，请前去购买
        //Content:尊敬的用户，您的V5会员已到期，V5会员可享受服务费7折优惠，平台也将会在V5会员生日时送上神秘礼包哦。请及时续费以免耽误您获得投资奖励！
        String title = MessageEventType.MEMBERSHIP_EXPIRED.getTitleTemplate();
        String content = MessageEventType.MEMBERSHIP_EXPIRED.getContentTemplate();
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_EXPIRED, membershipExpiredUsers, title, content, null));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(membershipExpiredUsers, PushSource.ALL, PushType.MEMBERSHIP_EXPIRED, title));
    }

    private void couponExpiredAfterFiveDays() {
        List<UserCouponModel> expireAfterFiveDays = userCouponMapper.findExpireAfterFiveDays();
        //Title:您有一张{0}即将失效
        //AppTitle: 您有一张{0}即将失效
        //Content:尊敬的用户，您有一张{0}即将失效(有效期至:{1})，请尽快使用！
        for (UserCouponModel userCouponModel : expireAfterFiveDays) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            String title;
            String content;
            String endTime = new SimpleDateFormat("yyyy年MM月dd日").format(userCouponModel.getEndTime());
            switch (couponModel.getCouponType()) {
                case RED_ENVELOPE:
                case NEWBIE_COUPON:
                case INVEST_COUPON:
                    long amount = couponModel.getAmount();
                    title = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getTitleTemplate(), AmountConverter.convertCentToString(amount) + "元" + couponModel.getCouponType().getName());
                    content = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getContentTemplate(), AmountConverter.convertCentToString(amount) + "元" + couponModel.getCouponType().getName(), endTime);
                    break;
                case INTEREST_COUPON:
                    double rate = couponModel.getRate();
                    title = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getTitleTemplate(), new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + couponModel.getCouponType().getName());
                    content = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getContentTemplate(), new BigDecimal(rate).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + "%" + couponModel.getCouponType().getName(), endTime);
                    break;
                default:
                    title = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getTitleTemplate(), couponModel.getCouponType().getName());
                    content = MessageFormat.format(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT.getContentTemplate(), couponModel.getCouponType().getName(), endTime);
                    break;
            }
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.COUPON_5DAYS_EXPIRED_ALERT, Lists.newArrayList(userCouponModel.getLoginName()), title, content, userCouponModel.getId()));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(userCouponModel.getLoginName()), PushSource.ALL, PushType.COUPON_5DAYS_EXPIRED_ALERT, title));
        }
    }
}
