package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.message.CouponAssignSmsNotifyMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class CouponAssignSmsNotifyMessageConsumer implements MessageConsumer {

    static Logger logger = Logger.getLogger(CouponAssignSmsNotifyMessageConsumer.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponSmsAssignNotify;
    }

    @Override
    public void consume(String message) {
        logger.info(MessageFormat.format("CouponSmsNotify is consume, message:{0}", message));

        CouponAssignSmsNotifyMessage couponAssignSmsNotifyMessage;
        try {
            couponAssignSmsNotifyMessage = JsonConverter.readValue(message, CouponAssignSmsNotifyMessage.class);
        } catch (IOException e) {
            logger.error(MessageFormat.format("[CouponAssignSmsNotifyMessageConsumer][consume] message parse failed!, message:{0}", message), e);
            return;
        }

        CouponModel couponModel = couponMapper.findById(couponAssignSmsNotifyMessage.getCouponId());
        if (null == couponModel) {
            logger.error(MessageFormat.format("[CouponAssignSmsNotifyMessageConsumer][consume] coupon is null!, message:{0}", message));
            return;
        }
        UserModel userModel = userMapper.findByLoginName(couponAssignSmsNotifyMessage.getLoginName());
        if (null == userModel) {
            logger.error(MessageFormat.format("[CouponAssignSmsNotifyMessageConsumer][consume] user is null!, message:{0}", message));
            return;
        }

        SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
        notifyDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
        notifyDto.setRate(String.format("%.1f", couponModel.getRate() * 100));
        notifyDto.setCouponType(couponModel.getCouponType());
        notifyDto.setExpiredDate(DateTime.now().plusDays(couponModel.getDeadline()).withTimeAtStartOfDay().toString("yyyy年MM月dd日"));
        notifyDto.setDeadLine(couponModel.getDeadline());

        logger.info(MessageFormat.format("Send coupon notify, loginName:{0}, couponId:{1}", couponAssignSmsNotifyMessage.getLoginName(), String.valueOf(couponAssignSmsNotifyMessage.getCouponId())));

        notifyDto.setMobile(userModel.getMobile());
        try {
            smsWrapperClient.sendCouponAssignSuccessNotify(notifyDto);
        } catch (Exception e) {
            logger.error(MessageFormat.format("Send coupon notify is failed (couponId = {0}, mobile = {1})", String.valueOf(couponAssignSmsNotifyMessage.getCouponId()), userModel.getMobile()));
        }
    }
}
