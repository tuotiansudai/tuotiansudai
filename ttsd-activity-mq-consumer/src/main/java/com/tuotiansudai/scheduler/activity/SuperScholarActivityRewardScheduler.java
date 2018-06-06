package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.mq.client.model.MessageQueue;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class SuperScholarActivityRewardScheduler {

    static Logger logger = LoggerFactory.getLogger(SuperScholarActivityRewardScheduler.class);

    @Autowired
    private SuperScholarRewardMapper superScholarRewardMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.endTime}\")}")
    private Date activityEndTime;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String SUPER_SCHOLAR_SEND_CASH_LOAN = "SUPER_SCHOLAR_SEND_CASH_LOAN";

    private final String SUPER_SCHOLAR_SEND_USER_CASH = "SUPER_SCHOLAR_SEND_USER_CASH:{0}:{1}";

    private final int lifeSecond = 180 * 24 * 60 * 60;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Shanghai")
    public void sendSuperScholarReward() {

        Map<String, String> loanIds = redisWrapperClient.hgetAll(SUPER_SCHOLAR_SEND_CASH_LOAN);

        for (Map.Entry<String, String> entry : loanIds.entrySet()) {
            if (new Date().after(DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())) {
                redisWrapperClient.hdel(SUPER_SCHOLAR_SEND_CASH_LOAN, entry.getKey());
                List<ActivityInvestModel> investModels = activityInvestMapper.findByLoanIdAndActivity(Long.parseLong(entry.getKey()), ActivityCategory.SUPER_SCHOLAR_ACTIVITY.name());
                for (ActivityInvestModel model : investModels) {
                    SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(model.getLoginName(), model.getCreatedTime());
                    if (superScholarRewardModel == null) {
                        continue;
                    }
                    double rewardRate = superScholarRewardModel.getRewardRate();
                    long reward = (long) (model.getAnnualizedAmount() * rewardRate);
                    if (reward > 0 && !redisWrapperClient.exists(MessageFormat.format(SUPER_SCHOLAR_SEND_USER_CASH, String.valueOf(model.getInvestId()), model.getLoginName()))) {
                        logger.info("SUPER_SCHOLAR_ACTIVITY SEND CASH BEGIN, invest:{}, user:{}, rewardModelId:{}", model.getInvestId(), model.getUserName(), superScholarRewardModel.getId());
                        try {
                            sendCash(model.getInvestId(), model.getLoginName(), reward);
                            superScholarRewardModel.setCashBack(true);
                            superScholarRewardMapper.update(superScholarRewardModel);
                        } catch (Exception e) {
                            logger.error("SUPER_SCHOLAR_ACTIVITY SEND CASH error, invest:{}, user:{}, rewardModelId:{}, error:{}", model.getInvestId(), model.getUserName(), superScholarRewardModel.getId(), e.getMessage());
                        }
                        logger.info("SUPER_SCHOLAR_ACTIVITY SEND CASH END, invest:{}, user:{}, rewardModelId:{}", model.getInvestId(), model.getUserName(), superScholarRewardModel.getId());
                    }
                }
            }

        }

    }

    private void sendCash(long investId, String loginName, long reward) {
        String key = MessageFormat.format(SUPER_SCHOLAR_SEND_USER_CASH, String.valueOf(investId), loginName);
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(reward),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.SUPER_SCHOLAR_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("[SUPER_SCHOLAR_ACTIVITY] invest:{}, user:{}, cash:{} send:success", investId, loginName, reward);
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("[SUPER_SCHOLAR_ACTIVITY] invest:{}, user:{}, cash:{} send:error:{}", investId, loginName, reward, e.getMessage());
        }
        redisWrapperClient.setex(key, lifeSecond, "fail");
        mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("【学霸加薪季活动】用户:{0}, 投资Id:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(investId), String.valueOf(reward)));
    }

}
