package com.honey.scheduler.example.simple;

import com.honey.scheduler.annotation.HoneySimpleJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@HoneySimpleJob(jobGroup = "testSimpleJobGroup", jobName = "testSimpleJobName" ,
        triggerGroup = "testSimpleTriggerGroup", triggerName = "testSimpleTriggerName",
        repeatInterval = "${test.simple.job.repeat-interval}",
        repeatCount = "${test.simple.job.repeat-count}")
public class TestSimpleJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        System.out.println(LocalDateTime.now().toString() + " Simple-Hello");
    }

}