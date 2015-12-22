package com.tuotiansudai.job;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.WithdrawMapper;
import com.tuotiansudai.repository.model.Environment;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Component
public class RefreshWithdrawAmountJob implements Job {

    static Logger logger = Logger.getLogger(RefreshWithdrawAmountJob.class);

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (Environment.PRODUCTION != environment) {
            logger.error(MessageFormat.format("{0} environment can not run refresh withdraw job", environment.name()));
            return;
        }

        String key = "job:refreshwithdraw";
        if (!Strings.isNullOrEmpty(redisWrapperClient.get(key))) {
            logger.error("Refresh withdraw job has already been run");
            return;
        }

        logger.info("Refresh withdraw data is starting");

        withdrawMapper.refreshFee();

        logger.info("Refresh withdraw data is completed");

        redisWrapperClient.set(key, "completed");
    }
}
