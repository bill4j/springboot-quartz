package angelina.quartz.service.manage.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Angelina
 */
@Data
public class SaveJobDTO implements Serializable{

    private static final long serialVersionUID = -4420558399963564978L;

    @ApiModelProperty("任务名称")
    private String jobName;

    @ApiModelProperty("任务类型")
    private String jobType;

    @ApiModelProperty("任务分组")
    private String jobGroup;


    @ApiModelProperty("任务描述")
    private String jobDescription;


    @ApiModelProperty("cron任务表达式")
    private String cronExpression;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @ApiModelProperty("任务开始执行时间")
    private Date startTime;


    @ApiModelProperty("执行接口类全路径及名称")
    private String serviceClassName;


    @ApiModelProperty("方法名称")
    private String methodName;


    @ApiModelProperty("执行方法参数")
    private String param;

}
