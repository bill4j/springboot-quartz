package angelina.quartz.service.manage.domain.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Angelina
 */
@Data
public class JobInfoBO implements Serializable {

    private static final long serialVersionUID = -4784979990711707365L;


    @ApiModelProperty("任务类型")
    private String jobType;

    @ApiModelProperty("任务名称")
    private String jobName;


    @ApiModelProperty("任务分组")
    private String jobGroup;


    @ApiModelProperty("任务描述")
    private String jobDescription;


    @ApiModelProperty("任务状态")
    private String jobStatus;


    @ApiModelProperty("任务表达式")
    private String cronExpression;


    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date startTime;

    @ApiModelProperty("任务创建时间")
    private String createTime;
}
