package com.honey.scheduler.config;

import com.honey.scheduler.quartz.HoneyJobBeanFactory;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Properties;

import static com.honey.scheduler.constants.HoneySchedulerConstants.HONEY_DEFAULT_SCHEDULER_INSTANCE_NAME;
import static com.honey.scheduler.constants.HoneySchedulerConstants.HONEY_QUARTZ_PROPERTIES_INSTANCE_NAME;

/**
 * 构建{@link Scheduler}调度器。
 */
@Configuration
public class HoneySchedulerConfiguration {

    @Bean
    @ConditionalOnMissingBean(SchedulerFactoryBean.class)
    public SchedulerFactoryBean SchedulerFactoryBean(List<JobDetail> jobDetails, List<Trigger> triggers,
                                                     Properties schedulerProperties) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        // 设置实例名
        schedulerFactoryBean.setSchedulerName(schedulerProperties.getProperty(
                HONEY_QUARTZ_PROPERTIES_INSTANCE_NAME, HONEY_DEFAULT_SCHEDULER_INSTANCE_NAME));
        // 设置JobFactory
        schedulerFactoryBean.setJobFactory(honeyJobBeanFactory());
        // 注册JobDetail
        if (null != jobDetails && !jobDetails.isEmpty()) {
            schedulerFactoryBean.setJobDetails(jobDetails.toArray(new JobDetail[0]));
        }
        // 注册Trigger
        if (null != triggers && !triggers.isEmpty()) {
            schedulerFactoryBean.setTriggers(triggers.toArray(new Trigger[0]));
        }
        // 默认覆盖已经存在的定时任务
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        // 将配置设置给调度器
        schedulerFactoryBean.setQuartzProperties(schedulerProperties);

        return schedulerFactoryBean;
    }

    @Bean
    public HoneyJobBeanFactory honeyJobBeanFactory() {
        return new HoneyJobBeanFactory();
    }

}