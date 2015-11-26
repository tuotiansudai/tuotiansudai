package com.tuotiansudai.job;

import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AutoReFreshAreaByMobileJob implements Job {
    static Logger logger = Logger.getLogger(AutoReFreshAreaByMobileJob.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.debug("AutoReFleshAreaByMobileJob===========in");
        while(true){
            List<UserModel> userModels = userMapper.findUserByProvince();
            if(CollectionUtils.isEmpty(userModels)){
                break;
            }
            userService.refreshAreaByMobile(userModels);
        }
        logger.debug("AutoReFleshAreaByMobileJob===========out");
    }
}
