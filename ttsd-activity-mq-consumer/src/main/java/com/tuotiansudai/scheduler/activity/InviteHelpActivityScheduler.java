package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpInfoMapper;
import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.WeChatHelpInfoModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.activity.repository.model.WeChatHelpUserStatus;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class InviteHelpActivityScheduler {
    static Logger logger = LoggerFactory.getLogger(InviteHelpActivityScheduler.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private WeChatHelpMapper weChatHelpMapper;

    @Autowired
    private WeChatHelpInfoMapper weChatHelpInfoMapper;

    public static final String INVEST_HELP_WAIT_SEND_CASH = "INVEST_HELP_WAIT_SEND_CASH";

    public static final String EVERYONE_HELP_WAIT_SEND_CASH = "EVERYONE_HELP_WAIT_SEND_CASH";

    public static final String INVEST_HELP_SEND_CASH_TO_CREATOR = "INVEST_HELP_SEND_CASH:{0}:{1}";

    public static final String EVERYONE_HELP_SEND_CASH_TO_CREATOR = "EVERYONE_HELP_SEND_CASH:{0}";

    public static final String SEND_CASH_TO_MINISTRANT = "SEND_CASH_TO_MINISTRANT:{0}:{1}";

    private final int lifeSecond = 180 * 24 * 60 * 60;

    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Shanghai")
    public void sendCash() {
        sendInvestHelpCash();
        sendEveryoneHelpCash();
    }

    private void sendInvestHelpCash() {
        Map<String, String> investHelps = redisWrapperClient.hgetAll(INVEST_HELP_WAIT_SEND_CASH);

        for (Map.Entry<String, String> entry : investHelps.entrySet()) {
            long loanId = Long.parseLong(entry.getKey());
            Date sendTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(sendTime)) {
                redisWrapperClient.hdel(INVEST_HELP_WAIT_SEND_CASH, String.valueOf(loanId));
                List<WeChatHelpModel> list = weChatHelpMapper.findByLoanId(loanId);
                for (WeChatHelpModel weChatHelpModel : list) {
                    if (weChatHelpModel.getReward() > 0 && !redisWrapperClient.exists(MessageFormat.format(INVEST_HELP_SEND_CASH_TO_CREATOR, loanId, weChatHelpModel.getLoginName()))) {
                        try {
                            sendInvestHelpCashToCreator(loanId, weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                            weChatHelpModel.setCashBack(true);
                            weChatHelpMapper.update(weChatHelpModel);
                        } catch (Exception e) {
                            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send creator cash, user:{}, cash:{} fail", loanId, weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                        }
                        sendCashMinistrantByHelpModel(weChatHelpModel);
                    }
                }
            }
        }
    }

    private void sendEveryoneHelpCash() {
        Map<String, String> everyoneHelps = redisWrapperClient.hgetAll(EVERYONE_HELP_WAIT_SEND_CASH);
        for (Map.Entry<String, String> entry : everyoneHelps.entrySet()) {
            long id = Long.parseLong(entry.getKey());
            Date sendTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(sendTime)) {
                redisWrapperClient.hdel(EVERYONE_HELP_WAIT_SEND_CASH, String.valueOf(id));
                WeChatHelpModel weChatHelpModel = weChatHelpMapper.findById(id);
                if (weChatHelpModel.getLoginName() == null || weChatHelpModel.getReward() <= 0) {
                    continue;
                }
                if (!redisWrapperClient.exists(MessageFormat.format(EVERYONE_HELP_SEND_CASH_TO_CREATOR, weChatHelpModel.getLoginName()))) {
                    try {
                        sendEveryoneHelpCashToCreator(weChatHelpModel);
                        weChatHelpModel.setCashBack(true);
                        weChatHelpMapper.update(weChatHelpModel);
                    } catch (Exception e) {
                        logger.info("[Invite_Help_Activity] send everyone help {} creator cash fail, error:{}", weChatHelpModel.getId(), e.getMessage());
                    }
                }
            }
        }
    }

    private void sendInvestHelpCashToCreator(long loanId, String loginName, long cash) {
        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send creator cash start, user:{}, cash:{}", loanId, loginName, cash);
        String key = MessageFormat.format(INVEST_HELP_SEND_CASH_TO_CREATOR, String.valueOf(loanId), loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send creator cash user:{}, cash:{} success", loanId, loginName, cash);
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send creator cash user:{}, cash:{} fail", loanId, loginName, cash);
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【邀请助力活动】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(cash))));
        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send creator cash end, user:{}, cash:{}", loanId, loginName, cash);
    }

    private void sendEveryoneHelpCashToCreator(WeChatHelpModel weChatHelpModel) {
        logger.info("[Invite_Help_Activity] everyone help {} send creator cash start, user:{}, cash:{}", weChatHelpModel.getId(), weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
        String key = MessageFormat.format(EVERYONE_HELP_SEND_CASH_TO_CREATOR, weChatHelpModel.getLoginName());
        TransferCashDto transferCashDto = new TransferCashDto(weChatHelpModel.getLoginName(), String.valueOf(IdGenerator.generate()), String.valueOf(weChatHelpModel.getReward()),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[Invite_Help_Activity] everyone help {} send creator cash success, user:{}, cash:{}", weChatHelpModel.getId(), weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
            if (response.getData().getCode().equals(String.valueOf(HttpStatus.BAD_REQUEST))){
                redisWrapperClient.setex(key, lifeSecond, "fail");
                logger.info("[Invite_Help_Activity] everyone help {} send creator cash fail, user:{}, cash:{}, message:{}", weChatHelpModel.getId(), weChatHelpModel.getLoginName(), weChatHelpModel.getReward(), response.getData().getMessage());
                return;
            }
        } catch (Exception e) {
            logger.error("[Invite_Help_Activity] everyone help {} send creator cash fail, user:{}, cash:{}", weChatHelpModel.getId(), weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【邀请助力活动】用户:{0}, 助力Id:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", weChatHelpModel.getLoginName(), weChatHelpModel.getId(), weChatHelpModel.getReward())));
        logger.info("[Invite_Help_Activity] everyone help {} send creator cash end, user:{}, cash:{}", weChatHelpModel.getId(), weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
    }

    private void sendCashMinistrantByHelpModel(WeChatHelpModel weChatHelpModel) {
        long cash = weChatHelpModel.getReward() / weChatHelpModel.getHelpUserCount();
        if (cash <= 0) {
            return;
        }
        List<WeChatHelpInfoModel> list = weChatHelpInfoMapper.findByHelpId(weChatHelpModel.getId());
        for (WeChatHelpInfoModel weChatHelpInfoModel : list) {
            if (!redisWrapperClient.exists(MessageFormat.format(SEND_CASH_TO_MINISTRANT, weChatHelpInfoModel.getId(), weChatHelpInfoModel.getOpenId()))) {
                try {
                    sendCashToMinistrant(weChatHelpModel.getId(), weChatHelpInfoModel, cash);
                } catch (Exception e) {
                    logger.error("[Invite_Help_Activity] send ministrant cash helpId:{}, openId:{}, cash{} fail", weChatHelpModel.getId(), weChatHelpInfoModel.getOpenId(), cash);
                }
            }
        }
    }

    private void sendCashToMinistrant(long id, WeChatHelpInfoModel weChatHelpInfoModel, long cash) {
        logger.info("[Invite_Help_Activity] send ministrant cash start, helpId:{}, openId:{}, cash:{}", id, weChatHelpInfoModel.getOpenId(), cash);
        String key = MessageFormat.format(SEND_CASH_TO_MINISTRANT, weChatHelpInfoModel.getId(), weChatHelpInfoModel.getOpenId());

        InviteHelpActivityPayCashDto inviteHelpActivityPayCashDto = new InviteHelpActivityPayCashDto(weChatHelpInfoModel.getOpenId(), null, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);

        weChatHelpInfoModel.setCashBackTime(new Date());
        try {
            BaseDto<PayDataDto> response = payWrapperClient.InviteHelpActivityTransferCash(inviteHelpActivityPayCashDto);
            if (response.getData().getStatus()) {
                logger.info("[Invite_Help_Activity] send ministrant cash, helpId:{}, openId:{}, cash:{} success", id, weChatHelpInfoModel.getOpenId(), cash);
                redisWrapperClient.setex(key, lifeSecond, "success");
                weChatHelpInfoModel.setStatus(WeChatHelpUserStatus.SUCCESS);
                weChatHelpInfoMapper.update(weChatHelpInfoModel);
                return;
            }
            if (response.getData().getCode().equals(String.valueOf(HttpStatus.BAD_REQUEST))){
                redisWrapperClient.setex(key, lifeSecond, "fail");
                weChatHelpInfoModel.setRemark(response.getData().getMessage());
                weChatHelpInfoModel.setStatus(WeChatHelpUserStatus.FAIL);
                weChatHelpInfoMapper.update(weChatHelpInfoModel);
                return;
            }

        } catch (Exception e) {
            weChatHelpInfoModel.setRemark(e.getMessage());
            logger.error("[Invite_Help_Activity] send ministrant cash, helpId:{}, openId:{}, cash:{} fail", id, weChatHelpInfoModel.getOpenId(), cash);
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【邀请助力活动】助力id: {0}, openId:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", id, weChatHelpInfoModel.getOpenId(), cash)));
        logger.info("[Invite_Help_Activity] send ministrant cash end, helpId:{}, openId:{}, cash:{}", id, weChatHelpInfoModel.getOpenId(), cash);
    }

}
