package com.honey.scheduler.annotation;

import com.honey.scheduler.config.HoneyJobRegistrar;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SimpleTrigger;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Import(HoneyJobRegistrar.class)
public @interface HoneySimpleJob {

    /**
     * 任务的group。
     */
    String jobGroup();

    /**
     * 任务的name。
     */
    String jobName();

    /**
     * 任务触发器的group。<br/>
     * 不配置时取jobGroup的值。
     */
    String triggerGroup() default StringUtils.EMPTY;

    /**
     * 任务触发器的name。<br/>
     * 不配置时取jobName的值。
     */
    String triggerName() default StringUtils.EMPTY;

    /**
     * Trigger的重复触发间隔。<br/>
     * 单位是毫秒。
     */
    String repeatInterval();

    /**
     * Trigger的重复触发次数。<br/>
     * 配置成-1表示一直执行。
     */
    String repeatCount();

    /**
     * 开始时间点。<br/>
     * 格式为yyyy-MM-dd HH:mm:ss。<br/>
     * 不配置时取当前时间。
     */
    String start() default StringUtils.EMPTY;

    /**
     * 结束时间点。<br/>
     * 格式为yyyy-MM-dd HH:mm:ss。
     */
    String end() default StringUtils.EMPTY;

    /**
     * Misfire策略。<br/>
     * 策略取值见{@link SimpleTrigger}。
     */
    int misfire() default 0;

}