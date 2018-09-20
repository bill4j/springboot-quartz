package angelina.quartz.service.example_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Angelina
 */
@Service
@Slf4j
public class Execute {

    public void myMehtod(String methodParam){
        log.info("------------------哈哈，任务执行了本方法，参数为："+methodParam);
    }
}
