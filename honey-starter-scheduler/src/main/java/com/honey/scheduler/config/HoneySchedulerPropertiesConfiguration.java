package com.honey.scheduler.config;

import com.honey.scheduler.properties.HoneySchedulerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Properties;

import static com.honey.scheduler.constants.HoneySchedulerConstants.HONEY_QUARTZ_PROPERTIES_FILE_PATH;
import static com.honey.scheduler.constants.HoneySchedulerConstants.HONEY_QUARTZ_PROPERTIES_PREFIX;

/**
 * 主要作用是将application.yml文件配置的属性与honey-quartz.properties
 * 文件配置的属性进行组合。<br/>
 * application.yml文件配置优先级大于honey-quartz.properties。
 */
@Configuration
@EnableConfigurationProperties({HoneySchedulerProperties.class})
public class HoneySchedulerPropertiesConfiguration {

    @Bean
    public Properties schedulerProperties(HoneySchedulerProperties honeySchedulerProperties) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(
                HoneySchedulerPropertiesConfiguration.class.getClassLoader());

        // 去classpath下加载honey-quartz.properties配置文件的属性
        Properties honeyQuartzProperties = new Properties();
        try {
            honeyQuartzProperties.load(resourcePatternResolver.getResource(HONEY_QUARTZ_PROPERTIES_FILE_PATH)
                    .getInputStream());
        } catch (Exception e) {
            // ignore
        }

        // 将application.yml文件配置的属性转换成Properties
        Properties ymlQuartzProperties = new Properties();
        Map<String, String> quartzProperties = honeySchedulerProperties.getQuartz();
        if (null != quartzProperties && !quartzProperties.isEmpty()) {
            for (Map.Entry<String, String> entry : quartzProperties.entrySet()) {
                ymlQuartzProperties.put(HONEY_QUARTZ_PROPERTIES_PREFIX + entry.getKey(), entry.getValue());
            }
        }

        // 组合application.yml配置文件属性与honey-quartz.properties配置文件属性
        // 注意application.yml文件配置优先级大于honey-quartz.properties
        CollectionUtils.mergePropertiesIntoMap(ymlQuartzProperties, honeyQuartzProperties);

        return honeyQuartzProperties;
    }

}