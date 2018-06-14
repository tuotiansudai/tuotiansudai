package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.*;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ThirdAnniversaryActivityScheduler {
    static Logger logger = LoggerFactory.getLogger(ThirdAnniversaryActivityScheduler.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String THIRD_ANNIVERSARY_WAIT_SEND_REWARD = "THIRD_ANNIVERSARY_WAIT_SEND_REWARD";

    private final String THIRD_ANNIVERSARY_TOP_FOUR_TEAM = "THIRD_ANNIVERSARY_TOP_FOUR_TEAM";

    private final String THIRD_ANNIVERSARY_SEND_CASH_SUCCESS = "THIRD_ANNIVERSARY_SEND_{0}_CASH_SUCCESS:{1}";

    private final String THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE = "THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private ThirdAnniversaryHelpMapper thirdAnniversaryHelpMapper;

    @Autowired
    private ThirdAnniversaryHelpInfoMapper thirdAnniversaryHelpInfoMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private ActivityInvestAnnualizedMapper activityInvestAnnualizedMapper;

    @Autowired
    private ThirdAnniversaryDrawMapper thirdAnniversaryDrawMapper;

    private final int lifeSecond = 180 * 24 * 60 * 60;

    private final long topFourCash = 888800l;

    private final Map<Integer, Double> rates = Maps.newHashMap(ImmutableMap.<Integer, Double>builder()
            .put(0, 0D)
            .put(1, 0.001D)
            .put(2, 0.002D)
            .put(3, 0.005D)
            .build());

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.third.anniversary.endTime}\")}")
    private Date activityEndTime;

    //    @Scheduled(cron = "0 10 0 * * ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 */30 * * * ?", zone = "Asia/Shanghai")
    public void sendHelpCash() {
        Map<String, String> investHelps = redisWrapperClient.hgetAll(THIRD_ANNIVERSARY_WAIT_SEND_REWARD);
        for (Map.Entry<String, String> entry : investHelps.entrySet()) {
            long weChatHelpId = Long.parseLong(entry.getKey());
            Date endTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(endTime)) {
                redisWrapperClient.hdel(THIRD_ANNIVERSARY_WAIT_SEND_REWARD, String.valueOf(weChatHelpId));
                ThirdAnniversaryHelpModel thirdAnniversaryHelpModel = thirdAnniversaryHelpMapper.findById(weChatHelpId);
                List<ThirdAnniversaryHelpInfoModel> helpInfoModels = thirdAnniversaryHelpInfoMapper.findByHelpId(weChatHelpId);
                long annualizedAmount = activityInvestMapper.findAllByActivityLoginNameAndTime(thirdAnniversaryHelpModel.getLoginName(), ActivityCategory.THIRD_ANNIVERSARY.name(), activityStartTime, thirdAnniversaryHelpModel.getEndTime()).stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
                long cash = (long) (annualizedAmount * rates.get(helpInfoModels.size()));
                if (helpInfoModels.size() > 0 && cash > 0 && !redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, "INVITE", thirdAnniversaryHelpModel.getLoginName()))) {
                    try {
                        sendCash(thirdAnniversaryHelpModel.getLoginName(), cash, "INVITE");
                    } catch (Exception e) {
                        logger.error("[third_anniversary_activity] send help cash to creator, user:{} fail, message:{}", thirdAnniversaryHelpModel.getLoginName(), e.getMessage());
                    }
                    sendHelpCashToFriend(helpInfoModels, cash / helpInfoModels.size());
                }
            }
        }
    }

    private void sendHelpCashToFriend(List<ThirdAnniversaryHelpInfoModel> helpInfoModels, long cash) {
        for (ThirdAnniversaryHelpInfoModel model : helpInfoModels) {
            if (!redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, "HELP", model.getLoginName()))) {
                try {
                    sendCash(model.getLoginName(), cash, "HELP");
                } catch (Exception e) {
                    logger.error("[third_anniversary_activity] send help cash to friend, user:{} fail, message:{}", model.getLoginName(), e.getMessage());
                }
            }
        }
    }

    //    @Scheduled(cron = "0 0 8 17 7 ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 */30 * * * ?", zone = "Asia/Shanghai")
    public void sendTopFourCash() {

        if (DateTime.now().getYear() != 2018 || !redisWrapperClient.exists(THIRD_ANNIVERSARY_TOP_FOUR_TEAM)) {
            return;
        }
        List<String> topFourTeams = Arrays.asList(redisWrapperClient.get(THIRD_ANNIVERSARY_TOP_FOUR_TEAM).split(","));
        List<String> collectSuccessLoginNames = thirdAnniversaryDrawMapper.findLoginNameByCollectTopFour(topFourTeams);

        if (collectSuccessLoginNames.size() == 0) {
            return;
        }

        long avgCash = topFourCash / collectSuccessLoginNames.size();

        for (String loginName : collectSuccessLoginNames) {
            try {
                if (!redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, "TEAM", loginName))) {
                    sendCash(loginName, avgCash, "TEAM");
                }
            } catch (Exception e) {
                logger.error("[third_anniversary_activity] send top four cash, user:{} fail, message:{}", loginName, e.getMessage());
            }
        }
    }

    //    @Scheduled(cron = "0 0 8 1 8 ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 */30 * * * ?", zone = "Asia/Shanghai")
    private void sendSupportCash() {
        if (DateTime.now().getYear() != 2018) {
            return;
        }
        logger.info("[third_anniversary_activity] send support cash start");
        List<ActivityInvestAnnualizedView> annualizedViews = activityInvestAnnualizedMapper.findByActivityAndMobile(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, null);
        Map<String, String> supportMaps = redisWrapperClient.hgetAll(THIRD_ANNIVERSARY_SELECT_RED_OR_BLUE);
        List<String> redSupportLoginName = supportMaps.entrySet().stream().filter(entry -> entry.getValue().equals("RED")).map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> blueSupportLoginName = supportMaps.entrySet().stream().filter(entry -> entry.getValue().equals("BLUE")).map(Map.Entry::getKey).collect(Collectors.toList());

        long redSupportAmount = annualizedViews.stream().filter(view -> redSupportLoginName.contains(view.getLoginName())).mapToLong(ActivityInvestAnnualizedView::getSumAnnualizedAmount).sum();
        long blueSupportAmount = annualizedViews.stream().filter(view -> blueSupportLoginName.contains(view.getLoginName())).mapToLong(ActivityInvestAnnualizedView::getSumAnnualizedAmount).sum();

        double redRate = redSupportAmount > blueSupportAmount ? 0.008D : 0.005D;
        double blueRate = blueSupportAmount > redSupportAmount ? 0.008D : 0.005D;

        for (String loginName : redSupportLoginName) {
            ActivityInvestAnnualizedModel model = activityInvestAnnualizedMapper.findByActivityAndLoginName(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, loginName);
            long cash = model == null ? 0 : (long) (model.getSumAnnualizedAmount() * redRate);
            if (cash > 0 && !redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, "SUPPORT", loginName))) {
                try {
                    sendCash(loginName, cash, "SUPPORT");
                } catch (Exception e) {
                    logger.error("[third_anniversary_activity] send support red cash, user:{} fail, message:{}", loginName, e.getMessage());
                }
            }
        }
        for (String loginName : blueSupportLoginName) {
            ActivityInvestAnnualizedModel model = activityInvestAnnualizedMapper.findByActivityAndLoginName(ActivityInvestAnnualized.THIRD_ANNIVERSARY_ACTIVITY, loginName);
            long cash = model == null ? 0 : (long) (model.getSumAnnualizedAmount() * blueRate);
            if (cash > 0 && !redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, "SUPPORT", loginName))) {
                try {
                    sendCash(loginName, cash, "SUPPORT");
                } catch (Exception e) {
                    logger.error("[third_anniversary_activity] send support blue cash, user:{} fail, message:{}", loginName, e.getMessage());
                }
            }
        }
        logger.info("[third_anniversary_activity] send support cash end");
    }

    private void sendCash(String loginName, long cash, String type) {
        String key = MessageFormat.format(THIRD_ANNIVERSARY_SEND_CASH_SUCCESS, type, loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.THIRD_ANNIVERSARY_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("[third_anniversary_activity] send cash user:{} fail, type:{}, message:{}", loginName, type, e.getMessage());
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【3周年活动】用户:{0},发送现金失败, 业务处理异常", loginName)));
    }
}
