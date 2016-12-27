package com.tuotiansudai.job;


import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignWeiXinRedEnvelopJob implements Job {
    static Logger logger = Logger.getLogger(AssignWeiXinRedEnvelopJob.class);

    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
