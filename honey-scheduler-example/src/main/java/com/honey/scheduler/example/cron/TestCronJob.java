package com.honey.scheduler.example.cron;

import com.honey.scheduler.annotation.HoneyCronJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@HoneyCronJob(jobGroup = "testCronJobGroup", jobName = "testCronJobName" ,
        triggerGroup = "testCronTriggerGroup", triggerName = "testCronTriggerName",
        cron = "*/10 * * * * ?")
public class TestCronJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        System.out.println(LocalDateTime.now().toString() + " Cron-Hello");
    }

}