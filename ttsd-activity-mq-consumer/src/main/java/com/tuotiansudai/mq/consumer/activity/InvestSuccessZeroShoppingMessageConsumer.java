package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeSelectMapper;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectModel;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

@Component
public class InvestSuccessZeroShoppingMessageConsumer implements MessageConsumer {

    public static Logger logger = LoggerFactory.getLogger(InvestSuccessZeroShoppingMessageConsumer.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static String ZERO_SHOPPING_ACTIVITY_PRIZE = "zero_shopping_activity_prize:{0}";

    @Autowired
    private ZeroShoppingPrizeSelectMapper zeroShoppingPrizeSelectMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.zero.shopping.startTime}\")}")
    private Date activityZeroShoppingStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.zero.shopping.endTime}\")}")
    private Date activityZeroShoppingEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ZeroShopping;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }
        Date date = DateTime.now().toDate();
        if (date.before(activityZeroShoppingStartTime) || date.after(activityZeroShoppingEndTime)) {
            return;
        }

        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UserInfo userInfo = investSuccessMessage.getUserInfo();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();
        String prizeKey = MessageFormat.format(ZERO_SHOPPING_ACTIVITY_PRIZE, String.valueOf(investInfo.getInvestId()));

        if (!redisWrapperClient.hexists(prizeKey, userInfo.getLoginName())) {
            return;
        }

        if (!loanDetailInfo.getActivityType().equals("NEWBIE")
                && !investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")) {
            zeroShoppingPrizeSelectMapper.create(new ZeroShoppingPrizeSelectModel(userInfo.getMobile(),
                    userInfo.getUserName(),
                    investInfo.getAmount(),
                    ZeroShoppingPrize.valueOf(redisWrapperClient.hget(prizeKey, userInfo.getLoginName()))));
        }
        redisWrapperClient.hdel(prizeKey, userInfo.getLoginName());
    }

}
