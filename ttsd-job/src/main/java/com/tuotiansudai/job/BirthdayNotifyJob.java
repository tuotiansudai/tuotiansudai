package com.tuotiansudai.job;

import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCouponNotifyDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BirthdayNotifyJob implements Job {

    static Logger logger = Logger.getLogger(BirthdayNotifyJob.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("trigger BirthdayNotifyJob job");
        List<String> userMobileList = userMapper.findUsersBirthdayMobile();
        for (String mobile : userMobileList) {
            SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
            notifyDto.setMobile(mobile.trim());
            smsWrapperClient.sendBirthdayNotify(notifyDto);
        }
    }
}
