package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.LoanDetailInfo;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.AnnualizedInvestUtil;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Component
public class InvestSuccessCreateWeChatHelpMessageConsumer implements MessageConsumer {

    public static Logger logger = LoggerFactory.getLogger(InvestSuccessCreateWeChatHelpMessageConsumer.class);

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.startTime}\")}")
    private Date activityInviteHelpStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.invite.help.endTime}\")}")
    private Date activityInviteHelpEndTime;


    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_WeChatHelp;
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }
        Date date = new Date();
        if (date.before(activityInviteHelpStartTime) || date.after(activityInviteHelpEndTime)) {
            logger.info("[MQ] InvestSuccess_WeChatHelp, cash snowball activity not in the activity time range");
            return;
        }

        try {
            InvestSuccessMessage investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            UserInfo userInfo = investSuccessMessage.getUserInfo();
            InvestInfo investInfo = investSuccessMessage.getInvestInfo();
            LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();

            if (!loanDetailInfo.getActivityType().equals("NEWBIE")
                    && !investInfo.getTransferStatus().equals("SUCCESS")
                    && investInfo.getStatus().equals("SUCCESS")) {

                long annualizedAmount = AnnualizedInvestUtil.annualizedInvestAmount(investInfo.getAmount(), loanDetailInfo.getDuration());
                weChatHelpMapper.create(new WeChatHelpModel(loanDetailInfo.getLoanId(), investInfo.getInvestId(), investInfo.getAmount(),
                        annualizedAmount, userInfo.getLoginName(), userInfo.getMobile(), null, WeChatHelpType.INVEST_HELP, new Date(), DateTime.now().plusDays(1).toDate()));

            }
        } catch (IOException e) {
            logger.error("[MQ] InvestSuccess_WeChatHelp, json convert InvestSuccessMessage is fail, message:{}", message);
        }

    }
}
