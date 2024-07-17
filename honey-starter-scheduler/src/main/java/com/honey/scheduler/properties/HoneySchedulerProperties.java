package com.honey.scheduler.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Quartz定时任务框架的配置属性。<br/>
 * 配置的字段与原生Quartz的配置字段保持一致。
 */
@ConfigurationProperties(prefix = "org")
public class HoneySchedulerProperties {

    private Map<String, String> quartz = new HashMap<>();

    public Map<String, String> getQuartz() {
        return quartz;
    }

    public void setQuartz(Map<String, String> quartz) {
        this.quartz = quartz;
    }

}