package angelina.quartz.service.schedule.service;


import angelina.quartz.api.common.util.JsonUtil;
import angelina.quartz.api.common.util.ResponseUtil;
import angelina.quartz.api.schedule.domain.ScheduleCronDTO;
import angelina.quartz.api.schedule.domain.ScheduleOneTimeDTO;
import angelina.quartz.api.schedule.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Angelina
 */
@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler scheduler;


    /**
     * 检查定时任务接口调用参数是否正确
     * @param serviceClassName 接口路径及名称
     * @param methodName 方法名称
     * @param param 方法参数
     * @return 检查结果
     */
    private ResponseUtil<Class> checkScheduleParam(String serviceClassName, String methodName,String param) {

        if (StringUtils.isEmpty(serviceClassName) || StringUtils.isEmpty(methodName)) {
            return ResponseUtil.fail("接口或方法未定义");
        }

        Class classObject;
        try {
            classObject = Class.forName(serviceClassName);
        } catch (ClassNotFoundException e) {
            return ResponseUtil.fail("未找到相关接口");
        }

        try {
            Object serviceImplObject = context.getBean(classObject);
            if (!StringUtils.isEmpty(param)) {
                serviceImplObject.getClass().getMethod(methodName,String.class);
            } else {
                serviceImplObject.getClass().getMethod(methodName);
            }
        } catch (NoSuchMethodException e) {
            return ResponseUtil.fail("未找到相关类或方法");
        }

        return ResponseUtil.ok(classObject);
    }


    @Override
    public ResponseUtil scheduleOneTime(ScheduleOneTimeDTO scheduleOneTimeDTO) {
        ResponseUtil<Class> checkScheduleParamResponse = checkScheduleParam(scheduleOneTimeDTO.getServiceClassName(),
                scheduleOneTimeDTO.getMethodName(),
                scheduleOneTimeDTO.getParam());

        if (checkScheduleParamResponse.checkFail()) {
            return checkScheduleParamResponse;
        }
        String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity(scheduleOneTimeDTO.getJobName(),scheduleOneTimeDTO.getJobGroup())
                .storeDurably().withDescription(createDate + " | " + scheduleOneTimeDTO.getJobDescription())
                .build();

        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("class", checkScheduleParamResponse.getData());
        jobDataMap.put("method", scheduleOneTimeDTO.getMethodName());
        jobDataMap.put("params", scheduleOneTimeDTO.getParam());


        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(scheduleOneTimeDTO.getJobName(), scheduleOneTimeDTO.getJobGroup())
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(0))
                .startAt(scheduleOneTimeDTO.getStartTime())
                .withDescription(scheduleOneTimeDTO.getJobDescription())
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("执行调度出现异常,调度参数：{}", JsonUtil.objectToJson(scheduleOneTimeDTO),e);
            return ResponseUtil.fail("任务调度异常");
        }

        return ResponseUtil.ok();
    }

    /**
     * 条件定时任务接口，跟所条件执行，此方法有风险不能对外暴露
     * @param scheduleCronDTO 调度DTO
     * @return 调度返回结果
     */
    public ResponseUtil scheduleCron(ScheduleCronDTO scheduleCronDTO) {

        ResponseUtil<Class> checkScheduleParamResponse = checkScheduleParam(scheduleCronDTO.getServiceClassName(),
                scheduleCronDTO.getMethodName(),
                scheduleCronDTO.getParam());

        if (checkScheduleParamResponse.checkFail()) {
            return checkScheduleParamResponse;
        }
        String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                .withIdentity(scheduleCronDTO.getJobName(),scheduleCronDTO.getJobGroup())
                .storeDurably().withDescription(createDate + "|" + scheduleCronDTO.getJobDescription())
                .build();

        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("class", checkScheduleParamResponse.getData());
        jobDataMap.put("method", scheduleCronDTO.getMethodName());
        jobDataMap.put("params", scheduleCronDTO.getParam());
        jobDataMap.put("cronExpression",scheduleCronDTO.getCronExpression());


        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(scheduleCronDTO.getJobName(), scheduleCronDTO.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleCronDTO.getCronExpression()))
                .startAt(scheduleCronDTO.getStartTime())
                .withDescription(scheduleCronDTO.getJobDescription()).build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("执行Cron调度出现异常,调度参数：{}", JsonUtil.objectToJson(scheduleCronDTO),e);
            return ResponseUtil.fail("Cron调度异常");
        }
        return ResponseUtil.ok();
    }
}
