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
import com.tuotiansudai.repository.model.IphoneXActivityView;
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

@Component
public class IphoneXActivitySendCashScheduler {

    private static Logger logger = LoggerFactory.getLogger(IphoneXActivitySendCashScheduler.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.startTime}\")}")
    private Date activityIphoneXStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.endTime}\")}")
    private Date activityIphoneXEndTime;

    private final List<IphoneXActivitySendCashScheduler.CashReward> cashRewards = Lists.newArrayList(
            new IphoneXActivitySendCashScheduler.CashReward(8800l, 10000000l, 20000000l),
            new IphoneXActivitySendCashScheduler.CashReward(38800l, 20000000l, 40000000l),
            new IphoneXActivitySendCashScheduler.CashReward(78800l, 40000000l, 60000000l),
            new IphoneXActivitySendCashScheduler.CashReward(128800l, 60000000l, 80000000l),
            new IphoneXActivitySendCashScheduler.CashReward(188800l, 80000000l, 100000000l));

    @Scheduled(cron = "0 0 16 13 10 ?", zone = "Asia/Shanghai")
    public void iphoneXActivitySendCash(){
        logger.info("[iphoneX activity] send cash start");

        if (DateTime.now().getYear() != 2017) {
            logger.info("[iphoneX activity] is over ");
            return;
        }

        Map<String, Long> map = new HashMap<>();
        List<IphoneXActivityView> list = investMapper.findAmountOrderByNameAndProductType(activityIphoneXStartTime, activityIphoneXEndTime);

        for (IphoneXActivityView iphoneXActivityView : list) {
            map.put(iphoneXActivityView.getLoginName(),
                    map.containsKey(iphoneXActivityView.getLoginName()) ? map.get(iphoneXActivityView.getLoginName()) + getAnnualizedAmount(iphoneXActivityView) : getAnnualizedAmount(iphoneXActivityView));
        }

        if (map.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Long> entry : map.entrySet()) {
            if (entry.getValue() == 0){
                continue;
            }
            long cash = getSumCashReward(entry.getValue());
            logger.info(MessageFormat.format("[iPhoneX activity] loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
            if (cash == 0){
                continue;
            }
            try {
                logger.info(MessageFormat.format("iphoneX activity send cash start, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
                sendCashPrize(entry, cash);
                logger.info(MessageFormat.format("iPhoneX activity send cash end, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash)));
            } catch (Exception e) {
                logger.error("iphoneX activity send cash fail, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash));
            }
        }
        logger.info("[iphoneX activity] send cash end");
    }

    public void sendCashPrize(Map.Entry<String, Long> entry, long cash){
        TransferCashDto transferCashDto = new TransferCashDto(entry.getKey(), String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.IPHONEX_ACTIVITY_CASH_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("iPhoneX activity send cash prize success, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash));
                return;
            }
        } catch (Exception e) {
            logger.error("iPhoneX activity send cash prize fail, loginName:{0}, cash:{1}", entry.getKey(), String.valueOf(cash));
        }

        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【iPhoneX活动结束发放现金奖励】用户:{0}, 获得现金:{1}, 发送现金失败, 业务处理异常", entry.getKey(), String.valueOf(cash))));
    }

    public Long getAnnualizedAmount(IphoneXActivityView iphoneXActivityView) {
        long amount = 0l;
        switch (iphoneXActivityView.getProductType()) {
            case _90:
                amount = (iphoneXActivityView.getSumAmount()) / 4;
                break;
            case _180:
                amount = (iphoneXActivityView.getSumAmount()) / 2;
                break;
            case _360:
                amount = iphoneXActivityView.getSumAmount();
                break;
        }
        return amount;
    }

    public long getSumCashReward(long annualizedAmount) {
        Optional<CashReward> reward = cashRewards.stream().filter(ipnoneXReward -> ipnoneXReward.getStartAmount() <= annualizedAmount && annualizedAmount < ipnoneXReward.getEndAmount()).findAny();
        return reward.isPresent() ? reward.get().getReward() : 0;
    }

    class CashReward {
        private Long reward;
        private Long startAmount;
        private Long endAmount;

        public CashReward(Long reward, Long startAmount, Long endAmount) {
            this.reward = reward;
            this.startAmount = startAmount;
            this.endAmount = endAmount;
        }

        public Long getReward() {
            return reward;
        }

        public Long getStartAmount() {
            return startAmount;
        }

        public Long getEndAmount() {
            return endAmount;
        }
    }
}
