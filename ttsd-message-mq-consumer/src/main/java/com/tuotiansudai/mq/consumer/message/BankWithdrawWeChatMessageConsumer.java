package com.tuotiansudai.mq.consumer.message;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.WeChatClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class BankWithdrawWeChatMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(BankWithdrawWeChatMessageConsumer.class);

    private final WeChatClient weChatClient = WeChatClient.getClient();

    @Override
    public MessageQueue queue() {
        return MessageQueue.Withdraw_WeChatMessage;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] Withdraw_PushMessage message is empty");
            return;
        }
        try {
            BankWithdrawMessage bankWithdrawMessage = new Gson().fromJson(message, BankWithdrawMessage.class);
            if (!bankWithdrawMessage.isStatus() || Strings.isNullOrEmpty(bankWithdrawMessage.getOpenId())) {
                return;
            }

            weChatClient.sendTemplateMessage(WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("openid", bankWithdrawMessage.getOpenId())
                    .put("first", "您的账户发起的提现申请现已到账")
                    .put("keyword1", String.valueOf(bankWithdrawMessage.getBankOrderDate()))
                    .put("keyword2", MessageFormat.format("{0}元", AmountConverter.convertCentToString(bankWithdrawMessage.getAmount())))
                    .put("keyword3", new DateTime().toString("yyyy-MM-dd HH:mm"))
                    .put("remark", "如有疑问，可随时致电客服400-169-1188（客服时间：工作日9:00-20:00）。")
                    .build()));
        } catch (Exception e) {
            logger.error(MessageFormat.format("[MQ] consume message error, message: {0}", message), e);
        }
    }
}
