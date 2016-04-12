package com.tuotiansudai.web.utils;

import com.tuotiansudai.util.JobManager;
import com.tuotiansudai.web.job.TestJob;
import com.tuotiansudai.web.repository.job.mapper.JobMapper;
import com.tuotiansudai.web.repository.job.mapper.TriggerMapper;
import com.tuotiansudai.web.repository.job.model.JobModel;
import com.tuotiansudai.web.repository.job.model.TriggerModel;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml", "classpath:spring-security.xml"})
public class QuartzTest {

    @Autowired
    JobMapper jobMapper;

    @Autowired
    JobManager jobManager;

    @Autowired
    TriggerMapper triggerMapper;

    @Test
    public void shouldSubmitJob() throws Exception {
        String jobName = "testName";
        String jobGroup = "testGroup";

        String k = "testKey";
        String v = String.valueOf(System.currentTimeMillis());
        jobManager.newJob(TestJob.class)
                .addJobData(k, v)
                .withIdentity(jobGroup, jobName)
                .withDescription("some Description")
                .runOnceAt(DateUtils.addSeconds(new Date(), 2))
                .submit();

        JobModel jobModel = jobMapper.findByKey(jobGroup, jobName);
        TriggerModel triggerModel = triggerMapper.findByKey(jobGroup, jobName);

        assert jobModel != null;
        assert triggerModel != null;

        assert jobModel.getJobClassName().equals(TestJob.class.getCanonicalName());

        assert triggerModel.getJobName().equals(jobModel.getJobName());
        assert triggerModel.getJobGroup().equals(jobModel.getJobGroup());

        jobManager.deleteJob(jobGroup, jobName);

        jobModel = jobMapper.findByKey(jobGroup, jobName);
        triggerModel = triggerMapper.findByKey(jobGroup, jobName);

        assert jobModel == null;
        assert triggerModel == null;
    }
}
