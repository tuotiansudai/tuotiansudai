package com.tuotiansudai.web.utils;

import com.tuotiansudai.utils.JobManager;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SimpleScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
public class QuartzTest {
    @Autowired
    private JobManager jobManager;

    @Test
    public void shouldDoOnce() throws Exception {
        String k = "testKey";
        String v = String.valueOf(System.currentTimeMillis());
        jobManager.newJob(TestJob.class)
                .addJobData(k,v)
                .withIdentity("testName", "testGroup")
                .withDescription("some Description")
                .runOnceAt(DateUtils.addSeconds(new Date(), 2))
                .submit();
        TestJob.targetValue = "oldValue";

        assert "oldValue".equals(TestJob.targetValue);

        Thread.sleep(3000);

        assert v.equals(TestJob.targetValue);
    }

    public static class TestJob implements Job {
        static String targetValue = null;

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            targetValue = context.getJobDetail().getJobDataMap().getString("testKey");
        }
    }
}
