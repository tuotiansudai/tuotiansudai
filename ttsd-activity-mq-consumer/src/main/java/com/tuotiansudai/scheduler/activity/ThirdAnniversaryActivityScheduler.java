package com.tuotiansudai.scheduler.activity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.ThirdAnniversaryDrawMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class ThirdAnniversaryActivityScheduler {
    static Logger logger = LoggerFactory.getLogger(ThirdAnniversaryActivityScheduler.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String THIRD_ANNIVERSARY_WAIT_SEND_REWARD = "THIRD_ANNIVERSARY_WAIT_SEND_REWARD";

    private final String THIRD_ANNIVERSARY_TOP_FOUR_TEAM = "THIRD_ANNIVERSARY_TOP_FOUR_TEAM";

    private final String THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS = "THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS:{0}";

    private final String THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS = "THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS:{0}";

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    @Autowired
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

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

    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Shanghai")
    public void sendCash() {
        sendHelpCash();
        sendTopFourCash();
    }

    private void sendHelpCash() {
        Map<String, String> investHelps = redisWrapperClient.hgetAll(THIRD_ANNIVERSARY_WAIT_SEND_REWARD);
        for (Map.Entry<String, String> entry : investHelps.entrySet()) {
            long weChatHelpId = Long.parseLong(entry.getKey());
            Date sendTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(sendTime)) {
                redisWrapperClient.hdel(THIRD_ANNIVERSARY_WAIT_SEND_REWARD, String.valueOf(weChatHelpId));
                WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(weChatHelpId);
                List<WeChatHelpInfoModel> helpInfoModels = weChatHelpInfoMapper.findByHelpId(weChatHelpId);
                long annualizedAmount = activityInvestMapper.findAllByActivityLoginNameAndTime(weChatHelpModel.getLoginName(), ActivityCategory.THIRD_ANNIVERSARY_ACTIVITY.name(), activityStartTime, weChatHelpModel.getEndTime()).stream().mapToLong(ActivityInvestModel::getAnnualizedAmount).sum();
                long cash = (long) (annualizedAmount * rates.get(helpInfoModels.size()));
                if (helpInfoModels.size() > 0 && cash > 0 && !redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS, weChatHelpModel.getLoginName()))) {
                    try {
                        sendCash(weChatHelpModel.getLoginName(), cash, true);
                        weChatHelpModel.setCashBack(true);
                        weChatHelpMapper.update(weChatHelpModel);
                    } catch (Exception e) {
                        logger.error("[third_anniversary_activity] send help cash to creator, user:{} fail, message:{}", weChatHelpModel.getLoginName(), e.getMessage());
                    }
                    sendHelpCashToFriend(helpInfoModels, cash / 3);
                }
            }
        }
    }

    private void sendHelpCashToFriend(List<WeChatHelpInfoModel> helpInfoModels, long cash){
        for (WeChatHelpInfoModel model : helpInfoModels){
            if (!redisWrapperClient.exists(MessageFormat.format(THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS, model.getLoginName()))) {
                try {
                    sendCash(model.getLoginName(), cash, true);
                    model.setStatus(WeChatHelpUserStatus.SUCCESS);
                    model.setCashBackTime(new Date());
                    weChatHelpInfoMapper.update(model);
                } catch (Exception e) {
                    logger.error("[third_anniversary_activity] send help cash to friend, user:{} fail, message:{}", model.getLoginName(), e.getMessage());
                }
            }
        }
    }

    private void sendTopFourCash() {
        if (!redisWrapperClient.exists(THIRD_ANNIVERSARY_TOP_FOUR_TEAM)){
            return;
        }
        List<String> topFourTeams = Arrays.asList(redisWrapperClient.get(THIRD_ANNIVERSARY_TOP_FOUR_TEAM).split(","));
        redisWrapperClient.del(THIRD_ANNIVERSARY_TOP_FOUR_TEAM);
        List<String> collectSuccessLoginNames = thirdAnniversaryDrawMapper.findLoginNameByCollectTopFour(topFourTeams);

        long avgCash = topFourCash / collectSuccessLoginNames.size();

        for (String loginName : collectSuccessLoginNames){
            try {
                sendCash(loginName, avgCash, false);
            } catch (Exception e) {
                logger.error("[third_anniversary_activity] send top four cash, user:{} fail, message:{}", loginName, e.getMessage());
            }
        }
    }

    private void sendCash(String loginName, long cash, boolean isHelp) {
        String key = isHelp ? MessageFormat.format(THIRD_ANNIVERSARY_SEND_HELP_CASH_SUCCESS, loginName) : MessageFormat.format(THIRD_ANNIVERSARY_SEND_TEAM_CASH_SUCCESS, loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.THIRD_ANNIVERSARY_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("[third_anniversary_activity] send cash user:{} fail, isHelp:{}, message:{}", loginName, isHelp, e.getMessage());
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【3周年活动】用户:{0},发送现金失败, 业务处理异常", loginName)));
    }
}
