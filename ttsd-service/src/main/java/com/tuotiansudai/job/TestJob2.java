package com.tuotiansudai.job;

import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;

@Component
public class TestJob2 implements InterruptableJob {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
//        try {
            int i = 0;
            while (i < 1000) {
                System.out.println(MessageFormat.format("{0} => {1, time, HH:mm:ss.SSS}", i, new Date()));
                if (i > 30) {
                    throw new NullPointerException("");
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
//        } catch (Exception e) {
//            JobExecutionException executionException = new JobExecutionException(e);
//            executionException.setRefireImmediately(true);
//            throw executionException;
//        }
    }

    public static void main(String[] args) throws JobExecutionException {
        TestJob2 job = new TestJob2();
        job.execute(null);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        System.out.println("job interrupt");
    }
}
