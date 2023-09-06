package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目预警
 * @author liuliang
 * @since 2023-05-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_early_warning")
public class ProjectEarlyWarning extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long deptId;

    /*数据时间*/
    private String dataTime;

    /*类型 1-超期未开工 2-专项资金拨付率未达标 3-专项资金完成率未达标*/
    private Integer type;

    /*项目编号*/
    private String projectNo;

    /*项目名称*/
    private String projectName;

    /*目标值*/
    private String targetValue;

    /*实际值*/
    private String actualValue;

    /*偏差值*/
    private String deviationValue;

    /*状态 1-未处理 2-已转办 3-已处理*/
    private Integer state;


}
