package com.tuotiansudai.web.utils;

import com.tuotiansudai.utils.JobManager;
import com.tuotiansudai.web.job.TestJob;
import com.tuotiansudai.web.repository.mapper.JobMapper;
import com.tuotiansudai.web.repository.mapper.TriggerMapper;
import com.tuotiansudai.web.repository.model.JobModel;
import com.tuotiansudai.web.repository.model.TriggerModel;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dispatcher-servlet.xml", "classpath:applicationContext.xml"})
@Transactional
public class QuartzTest {

    @Autowired
    JobMapper jobMapper;

    @Autowired
    TriggerMapper triggerMapper;

    @Test
    public void shouldSubmitJob() throws Exception {
        String jobName = "testName";
        String jobGroup = "testGroup";

        String k = "testKey";
        String v = String.valueOf(System.currentTimeMillis());
        JobManager.newJob(TestJob.class)
                .addJobData(k,v)
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
    }
}
