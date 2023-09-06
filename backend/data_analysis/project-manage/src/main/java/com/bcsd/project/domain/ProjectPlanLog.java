package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 计划审核记录
 *
 * @author liuliang
 * @since 2023-06-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_plan_log")
public class ProjectPlanLog extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*计划ID*/
    private Long planId;

    /*描述*/
    private String description;

    /*步骤*/
    private String step;

    /*类型 1-申请 2-审核 3-驳回 4-结束*/
    private Integer type;

}
