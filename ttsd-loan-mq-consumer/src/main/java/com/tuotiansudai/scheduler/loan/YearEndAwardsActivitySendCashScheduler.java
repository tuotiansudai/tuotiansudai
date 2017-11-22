package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class YearEndAwardsActivitySendCashScheduler {

    private static Logger logger = LoggerFactory.getLogger(YearEndAwardsActivitySendCashScheduler.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.year.end.awards.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.year.end.awards.endTime}\")}")
    private Date activityEndTime;

    private final List<AnnualizedAmount> annualizedAmounts = Lists.newArrayList(
            new AnnualizedAmount(200000000l, 600000000l, 0.002),
            new AnnualizedAmount(600000000l, 800000000l, 0.004),
            new AnnualizedAmount(800000000l, 1500000000l, 0.006),
            new AnnualizedAmount(1500000000l, 2000000000l, 0.008),
            new AnnualizedAmount(2000000000l, Long.MAX_VALUE, 0.01));

    @Scheduled(cron = "0 0 10 1 1 ?", zone = "Asia/Shanghai")
    public void yearEndAwardsSendCash(){
        logger.info("[year end awards activity] send cash begin");

        if (DateTime.now().getYear() != 2018){
            logger.info("[year end awards activity] send cash is over");
            return;
        }

        List<InvestProductTypeView> list = investMapper.findAmountOrderByNameAndProductType(activityStartTime, activityEndTime, "岁末专享");
        Map<String, Long> amountMaps = list.stream().collect(Collectors.toMap(k -> k.getLoginName(), v -> v.getSumAmount() * v.getProductType().getDuration() / 360, (v, newV) -> v + newV));

        if (amountMaps.isEmpty()){
            logger.info("[year end awards activity] send cash end, amountMaps is null");
            return;
        }

        long sumAnnualizedAmount = amountMaps.values().stream().mapToLong(Long::longValue).sum();
        Optional<AnnualizedAmount> optional = annualizedAmounts.stream().filter(annualizedAmount -> annualizedAmount.getMinAmount() <= sumAnnualizedAmount && sumAnnualizedAmount < annualizedAmount.getMaxAmount()).findAny();
        double ratio = optional.map(o->o.getRatio()).orElse(0D);
        if (!optional.isPresent()) {
            logger.info("[year end awards activity] send cash end, annualizedAmount less 1000000000");
            return;
        }

        for (Map.Entry<String, Long> entry : amountMaps.entrySet()){
            if (entry.getValue() == 0){
                continue;
            }
            long cash = new Double(entry.getValue() * ratio).longValue();
            try {
                logger.info(MessageFormat.format("[year end awards activity] send cash start, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
                sendCash(entry.getKey(), cash);
                logger.info(MessageFormat.format("[year end awards activity] send cash end, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
            }catch (Exception e){
                logger.error("[year end awards activity] send cash fail, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash));
            }

        }
        logger.info("[year end awards activity] send cash end");
    }

    public void sendCash(String loginName, long cash){
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.YEAR_END_AWARDS_CASH_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[year end awards activity] send cash prize success, loginName:{0}, cash:{1}", loginName, String.valueOf(cash));
                return;
            }
        } catch (Exception e) {
            logger.error("[year end awards activity] send cash prize fail, loginName:{0}, cash:{1}", loginName, String.valueOf(cash));
        }
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【年终奖活动发放现金】用户:{0}, 获得现金:{1}, 发放现金失败, 业务处理异常", loginName, String.valueOf(cash))));
    }

    class AnnualizedAmount{
        private long minAmount;
        private long maxAmount;
        private double ratio;

        public AnnualizedAmount() {
        }

        public AnnualizedAmount(long minAmount, long maxAmount, double ratio) {
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.ratio = ratio;
        }

        public long getMinAmount() {
            return minAmount;
        }

        public long getMaxAmount() {
            return maxAmount;
        }

        public double getRatio() {
            return ratio;
        }
    }
}
