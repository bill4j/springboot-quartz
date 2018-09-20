package angelina.quartz.service.manage.service;

import angelina.quartz.service.manage.domain.bo.JobInfoBO;
import angelina.quartz.service.manage.domain.dto.JobInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Angelina
 */
@Service
@Slf4j
public class TaskManageService {

    @Autowired
    private Scheduler scheduler;

    /**
     * 所有任务列表
     */
    public List<JobInfoBO> list(){

        List<JobInfoBO> list = new ArrayList<>();
        try {
            for(String groupJob: scheduler.getJobGroupNames()){
                for(JobKey jobKey: scheduler.getJobKeys(GroupMatcher.groupEquals(groupJob))){

                    List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                    for (Trigger trigger: triggers) {

                        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        String[] descriptions = jobDetail.getDescription().split("\\|");
                        JobInfoBO info = new JobInfoBO();
                        info.setJobName(jobKey.getName());
                        info.setJobGroup(jobKey.getGroup());
                        info.setJobDescription(descriptions.length > 1 ? descriptions[1] : "无描述");
                        info.setCreateTime(descriptions.length > 1 ? descriptions[0] : "无描述");
                        info.setJobStatus(triggerState.name().toLowerCase());
                        info.setJobType("simple 型任务");
                        info.setCronExpression("无");
                        info.setStartTime(trigger.getStartTime());
                        trigger.getStartTime();
                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            info.setCronExpression(cronTrigger.getCronExpression());
                            info.setJobType("cron 型任务");
                        }
                        list.add(info);
                    }
                }
            }
        } catch (SchedulerException e) {
            log.error("获取所有任务列表异常");
        }

        return list;
    }


    /**
     * 保存定时任务
     * @param info 任务信息
     */
    public void addJob(JobInfoDTO info) {


        try {
            if (checkExists(info.getJobName(), info.getJobGroup())) {
                log.warn("AddJob fail, job already exist, jobGroup:{}, jobName:{}", info.getJobGroup(), info.getJobName());
            }

            TriggerKey triggerKey = TriggerKey.triggerKey(info.getJobName(), info.getJobGroup());
            JobKey jobKey = JobKey.jobKey(info.getJobName(), info.getJobGroup());

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                    .withDescription(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .withSchedule(scheduleBuilder).build();

            Class<? extends Job> clazz = (Class<? extends Job>)Class.forName(info.getJobName());
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(jobKey)
                    .withDescription(info.getJobDescription()).build();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException | ClassNotFoundException e) {
            log.error("新增任务错误");
        }
    }

    /**
     * 修改定时任务
     * @param info 任务信息
     */
    public void edit(JobInfoDTO info) {

        try {
            if (!checkExists(info.getJobName(), info.getJobGroup())) {
                log.error("Job不存在, jobName:{},jobGroup:{}", info.getJobName(), info.getJobGroup());
                return ;
            }
            TriggerKey triggerKey = TriggerKey.triggerKey(info.getJobName(), info.getJobGroup());
            JobKey jobKey = new JobKey(info.getJobName(), info.getJobGroup());

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey)
                    .withDescription(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .withSchedule(cronScheduleBuilder).build();

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            jobDetail.getJobBuilder().withDescription(info.getJobDescription());
            HashSet<Trigger> triggerSet = new HashSet<>();
            triggerSet.add(cronTrigger);

            scheduler.scheduleJob(jobDetail, triggerSet, true);
        } catch (SchedulerException e) {
            log.error("编辑任务异常");
        }
    }

    /**
     * 删除定时任务
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    public void delete(String jobName, String jobGroup){

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                log.info("===> delete, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            log.error("删除任务异常");
        }
    }

    /**
     * 暂停定时任务
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    public void pause(String jobName, String jobGroup){

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.pauseTrigger(triggerKey);
                log.info("===> Pause success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            log.error("暂停任务异常");
        }
    }

    /**
     * 重新开始任务
     * @param jobName 任务名称
     * @param jobGroup 分组
     */
    public void resume(String jobName, String jobGroup){

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        try {
            if (checkExists(jobName, jobGroup)) {
                scheduler.resumeTrigger(triggerKey);
                log.info("===> Resume success, triggerKey:{}", triggerKey);
            }
        } catch (SchedulerException e) {
            log.error("任务启动异常");
        }
    }

    /**
     * 验证是否存在
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     * @throws SchedulerException 调度异常
     */
    private boolean checkExists(String jobName, String jobGroup) throws SchedulerException{

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        return scheduler.checkExists(triggerKey);
    }
}
