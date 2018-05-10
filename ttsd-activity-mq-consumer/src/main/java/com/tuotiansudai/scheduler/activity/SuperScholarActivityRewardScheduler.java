package com.tuotiansudai.scheduler.activity;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SuperScholarRewardMapper;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
import com.tuotiansudai.activity.repository.model.SuperScholarRewardModel;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
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
    private SmsWrapperClient smsWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.super.scholar.endTime}\")}")
    private Date activityEndTime;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String SUPER_SCHOLAR_SEND_CASH = "SUPER_SCHOLAR_SEND_CASH";

    @Scheduled(cron = "0 0 * * * ?", zone = "Asia/Shanghai")
    public void sendSuperScholarReward() {

        Map<String, String> loanIds = redisWrapperClient.hgetAll(SUPER_SCHOLAR_SEND_CASH);

        for (Map.Entry<String, String> entry : loanIds.entrySet()) {
            if (new Date().after(DateTime.parse(entry.getValue(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate())) {
                redisWrapperClient.hdel(SUPER_SCHOLAR_SEND_CASH, entry.getKey());
                List<ActivityInvestModel> investModels = activityInvestMapper.findByLoanId(Long.parseLong(entry.getKey()));
                for (ActivityInvestModel model : investModels){
                    String investDate = DateTimeFormat.forPattern("yyyy-MM-dd").print(new DateTime(model.getCreatedTime()));
                    SuperScholarRewardModel superScholarRewardModel = superScholarRewardMapper.findByLoginNameAndAnswerTime(model.getLoginName(), investDate);
                    if (superScholarRewardModel == null){
                        return;
                    }
                    double rewardRate = superScholarRewardModel.getRewardRate();


                }
            }

        }



    }
}
