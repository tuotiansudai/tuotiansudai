package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.mapper.WeChatHelpMapper;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
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

    public static final String INVEST_HELP_WAIT_SEND_CASH = "INVEST_HELP_WAIT_SEND_CASH";

    public static final String INVEST_HELP_SEND_CASH = "INVEST_HELP_SEND_CASH:{0}:{1}";

    public static final String EVERYONE_HELP_WAIT_SEND_CASH = "EVERYONE_HELP_WAIT_SEND_CASH";

    private final int lifeSecond = 180 * 24 * 60 * 60;


    @Scheduled(cron = "0 * * * * ?", zone = "Asia/Shanghai")
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
                logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour begin send cash", loanId);
                redisWrapperClient.hdel(INVEST_HELP_WAIT_SEND_CASH, String.valueOf(loanId));
                List<WeChatHelpModel> list = weChatHelpMapper.findByLoanId(loanId);
                for (WeChatHelpModel weChatHelpModel : list) {
                    if (weChatHelpModel.getReward() > 0 && !redisWrapperClient.exists(MessageFormat.format(INVEST_HELP_SEND_CASH, loanId, weChatHelpModel.getLoginName()))) {
                        try {
                            sendInvestHelpCashToCreator(loanId, weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                        } catch (Exception e) {
                            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send cash, user:{}, cash:{} fail",loanId, weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                        }

                        try {
                            long cash = weChatHelpModel.getReward() / weChatHelpModel.getHelpUserCount();

                        }catch (Exception e){
                            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send cash, user:{}, cash:{} fail",loanId, weChatHelpModel.getLoginName(), weChatHelpModel.getReward());
                        }
                    }
                }
            }
        }
    }

    private void sendEveryoneHelpCash(){
        Map<String, String> everyoneHelps = redisWrapperClient.hgetAll(EVERYONE_HELP_WAIT_SEND_CASH);
        for (Map.Entry<String, String> entry : everyoneHelps.entrySet()) {
            long id = Long.parseLong(entry.getKey());
            Date sendTime = DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
            if (new Date().after(sendTime)) {
                logger.info("[Invite_Help_Activity] everyone help {} out exceed 24 hour begin send cash", id);
                redisWrapperClient.hdel(EVERYONE_HELP_WAIT_SEND_CASH, String.valueOf(id));

            }
        }


    }

    private void sendInvestHelpCashToCreator(long loanId, String loginName, long cash){
        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash start, user:{}, cash:{}", loanId, loginName, cash);
        String key = MessageFormat.format(INVEST_HELP_SEND_CASH, String.valueOf(loanId), loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash user:{}, cash:{} success", loanId, loginName, cash);
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send cash user:{}, cash:{} fail", loanId, loginName, cash);
        }
        redisWrapperClient.setex(key,  lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【邀请助力活动】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(cash))));
        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash end, user:{}, cash:{}", loanId, loginName, cash);
    }

    public void sendCashToMinistrant(long loanId, String loginName, long cash){
//        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash start, user:{}, cash:{}", loanId, loginName, cash);
//        String key = MessageFormat.format(INVEST_HELP_SEND_CASH, String.valueOf(loanId), loginName);
//        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(cash),
//                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);
//        try {
//            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
//            if (response.getData().getStatus()) {
//                logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash user:{}, cash:{} success", loanId, loginName, cash);
//                redisWrapperClient.setex(key, lifeSecond, "success");
//                return;
//            }
//        } catch (Exception e) {
//            logger.error("[Invite_Help_Activity] loan {} out exceed 24 hour send cash user:{}, cash:{} fail", loanId, loginName, cash);
//        }
//        redisWrapperClient.setex(key,  lifeSecond, "fail");
//        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【邀请助力活动】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(cash))));
//        logger.info("[Invite_Help_Activity] loan {} out exceed 24 hour send cash end, user:{}, cash:{}", loanId, loginName, cash);
    }

}
