package com.tuotiansudai.job;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.repository.mapper.UserMapper;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class AssignWeiXinRedEnvelopJob implements Job {
    static Logger logger = Logger.getLogger(AssignWeiXinRedEnvelopJob.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    private final static String ACTIVITY_WX_CHANNEL = "weixin";

    private final static Integer[] referrerLevels = {1, 2, 3, 5, 7, 10};

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Map<String, Integer> referrerCountMap = Maps.newConcurrentMap();

        List<UserModel> registerUserModels = userMapper.findUserModelByChannel(ACTIVITY_WX_CHANNEL, startTime, endTime);
        for(UserModel userModel : registerUserModels){
            if(referrerCountMap.get(userModel.getReferrer()) == null){
                referrerCountMap.put(userModel.getReferrer(), 1);
                continue;
            }

            referrerCountMap.put(userModel.getReferrer(), (referrerCountMap.get(userModel.getReferrer()) + 1));
        }

        referrerCountMap.forEach((k, v) -> {
            for(Integer level : referrerLevels){
                if(v >= level){
                    couponAssignmentService.assignUserCoupon(k, getCouponId(level));
                }
            }
        });
    }

    private Long getCouponId(Integer level){
        switch (level){
            case 1:
                return 333l;
            case 2:
                return 334l;
            case 3:
                return 335l;
            case 5:
                return 336l;
            case 7:
                return 337l;
            default:
                return 338l;
        }
    }
}
