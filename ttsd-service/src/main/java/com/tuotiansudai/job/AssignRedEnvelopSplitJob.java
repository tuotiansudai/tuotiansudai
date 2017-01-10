package com.tuotiansudai.job;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class AssignRedEnvelopSplitJob implements Job {
    static Logger logger = Logger.getLogger(AssignRedEnvelopSplitJob.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;


    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("[RedEnvelopSplit] assign reward activity. start");
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Map<String, Integer> referrerCountMap = Maps.newConcurrentMap();

        List<UserModel> registerUserModels = getReferrerCount(startTime, endTime);
        for (UserModel userModel : registerUserModels) {
            if (referrerCountMap.get(userModel.getReferrer()) == null) {
                referrerCountMap.put(userModel.getReferrer(), 1);
                continue;
            }

            logger.info(MessageFormat.format("[RedEnvelopSplit] loginName:{0}, referrerCount{1}.", userModel.getReferrer(), referrerCountMap.get(userModel.getReferrer()) + 1));
            referrerCountMap.put(userModel.getReferrer(), (referrerCountMap.get(userModel.getReferrer()) + 1));
        }

        referrerCountMap.forEach((k, v) -> {
            logger.info(MessageFormat.format("[RedEnvelopSplit] assign redEnvelop loginName:{0}, couponId:{1}, level:{2}.", k, getCouponId(v), v));
            couponAssignmentService.assignUserCoupon(k, getCouponId(v));
        });

        logger.info("[RedEnvelopSplit] assign reward activity. end");
    }

    public String getCouponId(int referrerCount) {
        long sumAmount = 0l;

        if (referrerCount == 1) {
            sumAmount = 333l;
        } else if (referrerCount == 2) {
            sumAmount = 334l;
        } else if (referrerCount >= 3 && referrerCount < 5) {
            sumAmount = 335l;
        } else if (referrerCount >= 5 && referrerCount < 7) {
            sumAmount = 336l;
        } else if (referrerCount >= 7 && referrerCount < 10) {
            sumAmount = 337l;
        }
        if (referrerCount >= 10) {
            sumAmount = 338l;
        }
        return AmountConverter.convertCentToString(sumAmount);
    }

    private List<UserModel> getReferrerCount(Date startTime, Date endTime) {
        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, null);
        List<String> userChannels = Lists.newArrayList(UserChannel.values()).stream().map(userChannel -> userChannel.name()).collect(Collectors.toList());
        return userModels.stream().filter(userModel -> userChannels.contains(userModel.getChannel())).collect(Collectors.toList());
    }

}
