package angelina.quartz.service.manage.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @author Angelina
 */
@Data
public class JobInfoDTO implements Serializable{

    private static final long serialVersionUID = -8027955893071475125L;

    @ApiModelProperty("任务Id")
    private long id;

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


    @ApiModelProperty("任务创建时间")
    private String createTime;
}
