package angelina.quartz.service.manage.controller;

import angelina.quartz.api.common.util.ResponseUtil;
import angelina.quartz.api.schedule.domain.ScheduleCronDTO;
import angelina.quartz.api.schedule.domain.ScheduleOneTimeDTO;
import angelina.quartz.service.manage.domain.bo.JobInfoBO;
import angelina.quartz.service.manage.domain.dto.SaveJobDTO;
import angelina.quartz.service.manage.service.TaskManageService;
import angelina.quartz.service.schedule.service.ScheduleServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 任务管理controller
 *
 * @author Angelina
 */
@Controller
@Slf4j
@Api(value = "任务管理实现", description = "任务管理实现。")
public class TaskManageController {

    @Autowired
    private TaskManageService taskManageService;


    @Autowired
    private ScheduleServiceImpl scheduleService;

    /**
     * 首页
     *
     * @return 首页
     */
    @RequestMapping(value = "/manageCenter",method = RequestMethod.GET)
    public String index(ModelMap map) {

        List<JobInfoBO> jobInfoDTOS = taskManageService.list();
        map.put("rows", jobInfoDTOS);
        return "/index";
    }


    /**
     * 保存定时任务
     * @param saveJobDTO 任务信息
     */
    @ResponseBody
    @RequestMapping(value="/job/save", method=RequestMethod.POST, produces = "application/json; charset=UTF-8")
    public ResponseUtil save(@Validated SaveJobDTO saveJobDTO, BindingResult bindingResult){

        log.info("接收到的Job任务数据为:{}",saveJobDTO.toString());
        try {
            if ("1".equals(saveJobDTO.getJobType())) {
                ScheduleOneTimeDTO scheduleOneTimeDTO = new ScheduleOneTimeDTO();
                BeanUtils.copyProperties(saveJobDTO, scheduleOneTimeDTO);
                return scheduleService.scheduleOneTime(scheduleOneTimeDTO);
            }else{
                ScheduleCronDTO scheduleCronDTO = new ScheduleCronDTO();
                BeanUtils.copyProperties(saveJobDTO, scheduleCronDTO);
                return scheduleService.scheduleCron(scheduleCronDTO);
            }
        } catch (Exception e) {
            log.error("保存定时任务出错了",e);
            return ResponseUtil.fail("保存定时任务出错了");
        }
    }

    /**
     * 删除定时任务
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    @ResponseBody
    @RequestMapping(value="/job/delete",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    public ResponseUtil delete(String jobName,String jobGroup){

        try {
            taskManageService.delete(jobName, jobGroup);
        } catch (Exception e) {
            log.error("删除定时任务出错了");
            return ResponseUtil.fail("删除定时任务出错了");
        }
        return ResponseUtil.ok();
    }

    /**
     * 暂停定时任务
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    @ResponseBody
    @RequestMapping(value="/job/pause",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    public ResponseUtil pause(String jobName,String jobGroup){

        try {
            taskManageService.pause(jobName, jobGroup);
        } catch (Exception e) {
            log.error("暂停定时任务出错了");
            return ResponseUtil.fail("暂停定时任务出错了");
        }
        return ResponseUtil.ok();
    }

    /**
     * 重新开始定时任务
     * @param jobName 任务名称
     * @param jobGroup 任务分组
     */
    @ResponseBody
    @RequestMapping(value="/job/resume",method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
    public ResponseUtil resume(@RequestParam("jobName") String jobName,@RequestParam("jobGroup") String jobGroup){

        try {
            taskManageService.resume(jobName, jobGroup);
        } catch (Exception e) {
            log.error("重新开始任务出错了");
            return ResponseUtil.fail("重新开始任务出错了");
        }
        return ResponseUtil.ok();
    }

}
