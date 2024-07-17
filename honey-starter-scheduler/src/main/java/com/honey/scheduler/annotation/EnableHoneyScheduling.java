package com.honey.scheduler.annotation;

import com.honey.scheduler.config.HoneySchedulerConfiguration;
import com.honey.scheduler.config.HoneySchedulerPropertiesConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 定时任务开启注解。<br/>
 * 在启动类上添加该注解以启动定时任务。
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HoneySchedulerPropertiesConfiguration.class, HoneySchedulerConfiguration.class})
public @interface EnableHoneyScheduling {
}