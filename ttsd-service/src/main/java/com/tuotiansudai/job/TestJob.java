package com.tuotiansudai.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            int i = 0;
            while (i < 60) {
                System.out.println(MessageFormat.format("{0} => {1, time, HH:mm:ss.SSS}", i, new Date()));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        } catch (Exception e) {
            JobExecutionException executionException = new JobExecutionException(e);
            //executionException.setRefireImmediately(true);
            throw executionException;
        }
    }

    public static void main(String[] args) throws JobExecutionException {
        TestJob job = new TestJob();
        job.execute(null);
    }
}

