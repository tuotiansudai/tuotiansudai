package com.tuotiansudai.job;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignSecondRedEnvelopSplitJob implements Job {
    static Logger logger = Logger.getLogger(AssignSecondRedEnvelopSplitJob.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MQWrapperClient mqClient;

    @Value("#{'${activity.weiXin.red.envelop.second.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    public static final String JOB_EXECUTE_TIME = "2017-02-05 23:59:59";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[SecondRedEnvelopSplit] assign reward activity. start");
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Map<String, Integer> referrerCountMap = Maps.newConcurrentMap();

        List<UserModel> registerUserModels = getReferrerCount(startTime, endTime);
        for (UserModel userModel : registerUserModels) {
            if (referrerCountMap.get(userModel.getReferrer()) == null) {
                referrerCountMap.put(userModel.getReferrer(), 1);
                continue;
            }

            logger.info(MessageFormat.format("[SecondRedEnvelopSplit] loginName:{0}, referrerCount{1}.", userModel.getReferrer(), referrerCountMap.get(userModel.getReferrer()) + 1));
            referrerCountMap.put(userModel.getReferrer(), (referrerCountMap.get(userModel.getReferrer()) + 1));
        }

        referrerCountMap.forEach((k, v) -> {
            logger.info(MessageFormat.format("[SecondRedEnvelopSplit] assign redEnvelop loginName:{0}, couponId:{1}, level:{2}.", k, getCouponId(v), v));
            mqClient.sendMessage(MessageQueue.CouponAssigning, k + ":" + getCouponId(v));
        });

        logger.info("[SecondRedEnvelopSplit] assign reward activity. end");
    }

    public long getCouponId(int referrerCount) {
        if (referrerCount == 1) {
            return 370l;
        } else if (referrerCount == 2) {
            return 371l;
        } else if (referrerCount == 3) {
            return 372l;
        } else if (referrerCount == 4) {
            return 373l;
        } else if (referrerCount == 5) {
            return 374l;
        } else if (referrerCount >= 6) {
            return 375l;
        }
        return 0;
    }

    private List<UserModel> getReferrerCount(Date startTime, Date endTime) {
        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, null);
        List<String> userChannels = Lists.newArrayList(UserChannel.values()).stream().map(userChannel -> userChannel.name()).collect(Collectors.toList());
        return userModels.stream().filter(userModel -> userChannels.contains(userModel.getChannel())).collect(Collectors.toList());
    }

}
