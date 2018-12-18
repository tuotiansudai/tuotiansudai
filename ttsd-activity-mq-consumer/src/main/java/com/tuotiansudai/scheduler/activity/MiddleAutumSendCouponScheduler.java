package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.InvestNewmanTyrantMapper;
import com.tuotiansudai.activity.repository.model.NewmanTyrantView;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class MiddleAutumSendCouponScheduler {

    static Logger logger = LoggerFactory.getLogger(MiddleAutumSendCouponScheduler.class);

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private InvestNewmanTyrantMapper investNewmanTyrantMapper;

    @Value("#{'${activity.middleautum.nationalday.activity.period}'.split('\\~')}")
    private List<String> middleautumNationalDayActivityPeriod = Lists.newArrayList();

//  此活动暂不上线
//    @Scheduled(cron = "0 30 22 * * ?", zone = "Asia/Shanghai")
    public void sendCoupon() {
        Date tradingTime = new Date();
        logger.info("[MiddleAutumSendCouponScheduler start] time:{}",tradingTime.toString());
        if (isActivityTime(tradingTime)) {
            Date tradingStartTime = null;
            Date tradingEndTime = null;
            //基准比较时间---查询时间当天的22点
            tradingEndTime = new DateTime(tradingTime).withTime(22, 0, 0, 0).toDate();
            tradingStartTime = new DateTime(tradingStartTime).minusDays(1).toDate();
            List<NewmanTyrantView> newmanViews = investNewmanTyrantMapper.findNewmanTyrantByTradingTimeWithEnd(tradingStartTime, tradingEndTime, middleautumNationalDayActivityPeriod.get(0), middleautumNationalDayActivityPeriod.get(1));
            if (CollectionUtils.isEmpty(newmanViews) || newmanViews.size() <= 2) {
                logger.info("MiddleAutumSendCouponScheduler send coupon no List");
                return;
            }
            for (int i = 2; i < newmanViews.size() && i < 10; i++) {
                mqWrapperClient.sendMessage(MessageQueue.Coupon_Assigning, newmanViews.get(i).getLoginName() + ":" + 507);
            }
        }
    }

    private boolean isActivityTime(Date date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateTimeFormatter.parseDateTime(middleautumNationalDayActivityPeriod.get(0)).toDate();
        Date endTime = dateTimeFormatter.parseDateTime(middleautumNationalDayActivityPeriod.get(1)).plusDays(1).toDate();
        if (date.after(startTime) && date.before(endTime)) {
            return true;
        }
        return false;
    }
}
