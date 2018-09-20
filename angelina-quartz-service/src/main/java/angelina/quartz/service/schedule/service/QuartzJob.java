package angelina.quartz.service.schedule.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Angelina
 */
@Slf4j
public class QuartzJob implements Job {

    @Autowired
    private ApplicationContext context;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        Class classObject = (Class) jobDataMap.get("class");
        String methodName = jobDataMap.getString("method");
        String params = jobDataMap.getString("params");

        Object serviceImplObject = context.getBean(classObject);
        try {
            if (StringUtils.isEmpty(params)) {
                Method method = serviceImplObject.getClass().getMethod(methodName);
                method.invoke(serviceImplObject);
            } else {
                Method method = serviceImplObject.getClass().getMethod(methodName,String.class);
                method.invoke(serviceImplObject, params);
            }
        }catch (NoSuchMethodException e) {
            log.error("方法执行异常，方法名称：{}，参数：{}",methodName,params,e);
        }catch (IllegalAccessException | InvocationTargetException e) {
            log.error("方法执行异常，方法名称：{}，参数：{}",methodName,params,e);
        }
    }
}
