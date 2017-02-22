package com.tuotiansudai.mq.consumer.loan;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;

@Component
public class CouponSmsNotifyMessageConsumer implements MessageConsumer {

    static Logger logger = Logger.getLogger(CouponSmsNotifyMessageConsumer.class);

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

    @Resource(name = "membershipUserCollector")
    private UserCollector membershipUserCollector;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponSmsNotify;
    }

    @Override
    public void consume(String message) {
        logger.info(MessageFormat.format("CouponSmsNotify is consume, message:{0}", message));
        long couponId = Long.parseLong(message);
        CouponModel couponModel = couponMapper.findById(couponId);
        List<String> loginNames = this.getCollector(couponModel.getUserGroup()).collect(couponModel.getId());

        SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
        notifyDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
        notifyDto.setRate(String.format("%.1f", couponModel.getRate() * 100));
        notifyDto.setCouponType(couponModel.getCouponType());
        notifyDto.setExpiredDate(DateTime.now().plusDays(couponModel.getDeadline()).withTimeAtStartOfDay().toString("yyyy-MM-dd"));

        for (String loginName : loginNames) {
            logger.info(MessageFormat.format("Send coupon notify, loginName:{0}, couponId:{1}", loginName, String.valueOf(couponId)));
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
                .put(UserGroup.MEMBERSHIP_V0, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V1, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V2, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V3, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V4, this.membershipUserCollector)
                .put(UserGroup.MEMBERSHIP_V5, this.membershipUserCollector)
                .build()).get(userGroup);
    }
}
