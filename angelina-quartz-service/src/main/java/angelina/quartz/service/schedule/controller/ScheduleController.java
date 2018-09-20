package angelina.quartz.service.schedule.controller;

import angelina.quartz.api.common.util.ResponseUtil;
import angelina.quartz.api.schedule.domain.ScheduleCronDTO;
import angelina.quartz.api.schedule.domain.ScheduleOneTimeDTO;
import angelina.quartz.api.schedule.service.ScheduleService;
import angelina.quartz.service.schedule.service.ScheduleServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Angelina
 */
@RestController
@Slf4j
@Api(value = "调度任务实现", description = "调度任务实现。")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleServiceImpl service;

    /**
     * 定时调度任务接口，只执行一次
     * @param scheduleOneTimeDTO 执行调度相关参数
     * @return 执行结果
     */
    @RequestMapping(value = "/api/angelina/quartz/service/schedule/scheduleOnTime", method = RequestMethod.POST)
    public ResponseUtil scheduleOneTime(@RequestBody ScheduleOneTimeDTO scheduleOneTimeDTO){
        return scheduleService.scheduleOneTime(scheduleOneTimeDTO);
    }

    private ResponseUtil scheduleCron(ScheduleCronDTO scheduleCronDTO){
        return service.scheduleCron(scheduleCronDTO);
    }

}
