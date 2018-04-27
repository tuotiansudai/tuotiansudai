package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.etcd.ETCDConfigReader;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.ActivityAmountGrade;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CashSnowballActivityScheduler {

    private static Logger logger = LoggerFactory.getLogger(CashSnowballActivityScheduler.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    private Date activityCashSnowballStartTime = DateTime.parse(ETCDConfigReader.getReader().getValue("activity.cash.snowball.startTime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

    private Date activityCashSnowballEndTime = DateTime.parse(ETCDConfigReader.getReader().getValue("activity.cash.snowball.endTime"), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

    @Scheduled(cron = "0 0 10 8 2 ?", zone = "Asia/Shanghai")
    public void cashSnowballActivityEndSendCash() {
        logger.info("[cash snowball activity] send cash begin");

        if (DateTime.now().getYear() != 2018) {
            logger.info("[cash snowball activity] send cash is over");
            return;
        }

        List<InvestProductTypeView> list = investMapper.findAmountOrderByNameAndProductType(activityCashSnowballStartTime, activityCashSnowballEndTime, "逢万返百");
        Map<String, Long> amountMaps = list.stream().collect(Collectors.toMap(k -> k.getLoginName(), v -> v.getSumAmount() * v.getProductType().getDuration() / 360, (v, newV) -> v + newV));

        if (amountMaps.isEmpty()) {
            logger.info("[cash snowball activity] send cash end, amountMaps is null");
            return;
        }

        for (Map.Entry<String, Long> entry : amountMaps.entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }
            long cash = ActivityAmountGrade.getAwardAmount("CASH_SNOWBALL", entry.getValue());
            if (cash == 0){
                continue;
            }
            try {
                logger.info(MessageFormat.format("[cash snowball activity] send cash start, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
                sendCash(entry.getKey(), cash);
                logger.info(MessageFormat.format("[cash snowball activity] send cash end, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
            } catch (Exception e) {
                logger.error("[cash snowball activity] send cash fail, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash));
            }
        }
        logger.info("[cash snowball activity] send cash end");
    }

    public void sendCash(String loginName, long cash) {
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.CASH_SNOWBALL_CASH_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[cash snowball activity] send cash prize success, loginName:{0}, cash:{1}", loginName, String.valueOf(cash));
                return;
            }
        } catch (Exception e) {
            logger.error("[cash snowball activity] send cash prize fail, loginName:{0}, cash:{1}", loginName, String.valueOf(cash));
        }
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【现金滚雪球活动结束发放现金】用户:{0}, 获得现金:{1}, 发放现金失败, 业务处理异常", loginName, String.valueOf(cash))));
    }
}
