package com.honey.scheduler.quartz;

import org.quartz.JobDetail;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * 将任务对象注册为容器中的bean。<br/>
 * {@link SpringBeanJobFactory}能够将{@link JobDetail}注册为容器中的bean，
 * 但是依赖注入是构造函数的方式，在{@link HoneyJobBeanFactory}中主要是设置
 * 能够支持注解方式的依赖注入。
 */
public class HoneyJobBeanFactory extends SpringBeanJobFactory implements ApplicationContextAware {

    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

}