package com.honey.scheduler.constants;

public class HoneySchedulerConstants {

    public static final String HONEY_QUARTZ_PROPERTIES_FILE_PATH = "classpath:honey-quartz.properties";
    public static final String HONEY_QUARTZ_PROPERTIES_PREFIX = "org.quartz.";
    public static final String HONEY_QUARTZ_PROPERTIES_INSTANCE_NAME = "org.quartz.scheduler.instanceName";

    public static final String HONEY_QUARTZ_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String HONEY_DEFAULT_SCHEDULER_INSTANCE_NAME = "honey-default-instance";

    public static final String HONEY_JOB_JOB_GROUP = "jobGroup";
    public static final String HONEY_JOB_JOB_NAME = "jobName";
    public static final String HONEY_JOB_TRIGGER_GROUP = "triggerGroup";
    public static final String HONEY_JOB_TRIGGER_NAME = "triggerName";
    public static final String HONEY_JOB_START = "start";
    public static final String HONEY_JOB_END = "end";
    public static final String HONEY_JOB_MISFIRE = "misfire";

    public static final String HONEY_SIMPLE_JOB_REPEAT_INTERVAL = "repeatInterval";
    public static final String HONEY_SIMPLE_JOB_REPEAT_COUNT = "repeatCount";

    public static final String HONEY_CRON_JOB_CRON = "cron";

}