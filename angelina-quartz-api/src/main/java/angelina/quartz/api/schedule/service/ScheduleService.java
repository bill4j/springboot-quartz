package angelina.quartz.api.schedule.service;


import angelina.quartz.api.common.util.ResponseUtil;
import angelina.quartz.api.schedule.domain.ScheduleOneTimeDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Angelina
 */
@FeignClient(name = "angelina-quartz-service")
public interface ScheduleService {

    /**
     * 定时调度任务接口，只执行一次(不提供支持表达式的重复执行接口)
     * @param scheduleOneTimeDTO 执行调度相关参数
     * @return 执行结果
     */
    @RequestMapping(value = "api/schedule/scheduleOnTime", method = RequestMethod.POST)
    ResponseUtil scheduleOneTime(@RequestBody ScheduleOneTimeDTO scheduleOneTimeDTO);

}
