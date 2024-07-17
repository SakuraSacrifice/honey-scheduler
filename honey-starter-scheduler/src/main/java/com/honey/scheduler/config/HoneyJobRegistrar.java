package com.honey.scheduler.config;

import com.honey.scheduler.annotation.HoneyCronJob;
import com.honey.scheduler.annotation.HoneySimpleJob;
import com.honey.scheduler.constants.HoneySchedulerConstants;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ClassUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.honey.scheduler.constants.HoneySchedulerConstants.*;

/**
 * 将由{@link HoneySimpleJob}和{@link HoneyCronJob}注解修饰的任务注册为
 * Spring容器的bean。<br/>
 * 注解中的配置内容支持配置解析。
 */
public class HoneyJobRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (importingClassMetadata.hasAnnotation(HoneySimpleJob.class.getName())) {
            processSimpleJob(importingClassMetadata, registry);
        } else if (importingClassMetadata.hasAnnotation(HoneyCronJob.class.getName())) {
            processCronJob(importingClassMetadata, registry);
        }
    }

    private void processSimpleJob(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(HoneySimpleJob.class.getName()));

        if (null == annotationAttributes) {
            return;
        }

        // 得到任务的Class对象
        Class<?> clazz;
        String className = importingClassMetadata.getClassName();
        try {
            clazz = ClassUtils.forName(className,
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            throw new IllegalStateException();
        }

        // 任务类必须是QuartzJobBean类型
        if (!QuartzJobBean.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException();
        }

        String jobGroup = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_JOB_GROUP), true);
        String jobName = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_JOB_NAME), true);
        String triggerGroup = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_TRIGGER_GROUP), true);
        String triggerName = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_TRIGGER_NAME), true);
        Date startTime = parseDateStr(resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_START), false), true);
        Date endTime = parseDateStr(resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_END), false), false);
        String interval = resolvePlaceholder(annotationAttributes.getString(HONEY_SIMPLE_JOB_REPEAT_INTERVAL), false);
        long repeatInterval = StringUtils.isEmpty(interval) ? 0 : Long.parseLong(interval);
        String repeat = resolvePlaceholder(annotationAttributes.getString(HONEY_SIMPLE_JOB_REPEAT_COUNT), false);
        int repeatCount = StringUtils.isEmpty(repeat) ? SimpleTrigger.REPEAT_INDEFINITELY : Integer.parseInt(repeat);
        int misfire = annotationAttributes.getNumber(HONEY_JOB_MISFIRE);

        BeanDefinition jobDetailBeanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(JobDetailFactoryBean.class)
                .addPropertyValue("group", jobGroup)
                .addPropertyValue("name", jobName)
                .addPropertyValue("jobClass", clazz)
                .addPropertyValue("durability", true)
                .addPropertyValue("requestsRecovery", true)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        registry.registerBeanDefinition(className, jobDetailBeanDefinition);

        if (registry instanceof BeanFactory) {
            JobDetail jobDetail = ((BeanFactory) registry).getBean(className, JobDetail.class);
            BeanDefinition triggerBeanDefinition = BeanDefinitionBuilder
                    .genericBeanDefinition(SimpleTriggerImpl.class)
                    .addPropertyValue("group", StringUtils.isEmpty(triggerGroup) ? jobGroup : triggerGroup)
                    .addPropertyValue("name", StringUtils.isEmpty(triggerName) ? jobName : triggerName)
                    .addPropertyValue("jobKey", jobDetail.getKey())
                    .addPropertyValue("startTime", startTime)
                    .addPropertyValue("endTime", endTime)
                    .addPropertyValue("repeatInterval", repeatInterval)
                    .addPropertyValue("repeatCount", repeatCount)
                    .addPropertyValue("misfireInstruction", misfire)
                    .setScope(BeanDefinition.SCOPE_SINGLETON)
                    .getBeanDefinition();
            registry.registerBeanDefinition(assembleTriggerBeanName(className), triggerBeanDefinition);
        }
    }

    private void processCronJob(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(HoneyCronJob.class.getName()));

        if (null == annotationAttributes) {
            return;
        }

        // 得到任务的Class对象
        Class<?> clazz;
        String className = importingClassMetadata.getClassName();
        try {
            clazz = ClassUtils.forName(className,
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            throw new IllegalStateException();
        }

        // 任务类必须是QuartzJobBean类型
        if (!QuartzJobBean.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException();
        }

        String jobGroup = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_JOB_GROUP), true);
        String jobName = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_JOB_NAME), true);
        String triggerGroup = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_TRIGGER_GROUP), true);
        String triggerName = resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_TRIGGER_NAME), true);
        String cron = resolvePlaceholder(annotationAttributes.getString(HONEY_CRON_JOB_CRON), true);
        Date startTime = parseDateStr(resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_START), false), true);
        Date endTime = parseDateStr(resolvePlaceholder(annotationAttributes.getString(HONEY_JOB_END), false), false);
        int misfire = annotationAttributes.getNumber(HONEY_JOB_MISFIRE);

        BeanDefinition jobDetailBeanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(JobDetailFactoryBean.class)
                .addPropertyValue("group", jobGroup)
                .addPropertyValue("name", jobName)
                .addPropertyValue("jobClass", clazz)
                .addPropertyValue("durability", true)
                .addPropertyValue("requestsRecovery", true)
                .setScope(BeanDefinition.SCOPE_SINGLETON)
                .getBeanDefinition();
        registry.registerBeanDefinition(className, jobDetailBeanDefinition);

        if (registry instanceof BeanFactory) {
            JobDetail jobDetail = ((BeanFactory) registry).getBean(className, JobDetail.class);
            BeanDefinition triggerBeanDefinition = BeanDefinitionBuilder
                    .genericBeanDefinition(CronTriggerImpl.class)
                    .addPropertyValue("group", StringUtils.isEmpty(triggerGroup) ? jobGroup : triggerGroup)
                    .addPropertyValue("name", StringUtils.isEmpty(triggerName) ? jobName : triggerName)
                    .addPropertyValue("jobKey", jobDetail.getKey())
                    .addPropertyValue("startTime", startTime)
                    .addPropertyValue("endTime", endTime)
                    .addPropertyValue("cronExpression", cron)
                    .addPropertyValue("misfireInstruction", misfire)
                    .setScope(BeanDefinition.SCOPE_SINGLETON)
                    .getBeanDefinition();
            registry.registerBeanDefinition(assembleTriggerBeanName(className), triggerBeanDefinition);
        }
    }

    private String resolvePlaceholder(String text, boolean notAllowEmpty) {
        String resultText = text;
        if (StringUtils.isNotEmpty(resultText)) {
            resultText = this.environment.resolvePlaceholders(resultText);
        }
        if (notAllowEmpty && StringUtils.isEmpty(resultText)) {
            throw new IllegalStateException();
        }
        return resultText;
    }

    private Date parseDateStr(String dateStr, boolean nowWhenEmpty) {
        if (StringUtils.isEmpty(dateStr)) {
            return nowWhenEmpty ? new Date() : null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(HoneySchedulerConstants.HONEY_QUARTZ_DATE_FORMAT);
        sdf.setLenient(false);
        try {
            return new Date(sdf.parse(dateStr).getTime());
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    private String assembleTriggerBeanName(String jobClassName) {
        return jobClassName + ".trigger";
    }

}