package angelina.quertz.service;

import angelina.quartz.api.common.util.ResponseUtil;
import angelina.quartz.api.schedule.domain.ScheduleOneTimeDTO;
import angelina.quartz.api.schedule.service.ScheduleService;
import angelina.quartz.service.AngelinaQuartzServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@SpringBootTest(classes = AngelinaQuartzServiceApplication.class)
@RunWith(value = SpringRunner.class)
public class ScheduleTest {

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void testJobTask(){

        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(10);
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        ScheduleOneTimeDTO scheduleOneTimeDTO=new ScheduleOneTimeDTO();
        scheduleOneTimeDTO.setJobDescription("我就是试试");
        scheduleOneTimeDTO.setJobGroup("test啦");
        scheduleOneTimeDTO.setJobName("y发个测试1");
        scheduleOneTimeDTO.setJobType("one time");
        scheduleOneTimeDTO.setMethodName("myMehtod");
        scheduleOneTimeDTO.setParam("我就是个参数");
        scheduleOneTimeDTO.setServiceClassName("angelina.quartz.service.example_service.Execute");
        scheduleOneTimeDTO.setStartTime(Date.from(zdt.toInstant()));

        ResponseUtil responseUtil=scheduleService.scheduleOneTime(scheduleOneTimeDTO);
        System.out.println(responseUtil.getCode());
    }

}
