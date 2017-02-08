package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.enums.PushType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class CouponExpiredNotifyScheduler {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Scheduled(cron = "0 0 10 * * ?", zone = "Asia/Shanghai")
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
