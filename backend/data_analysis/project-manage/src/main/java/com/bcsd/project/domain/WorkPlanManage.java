package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 规划管理
 * @author liuliang
 * @since 2023-03-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_work_plan_manage")
public class WorkPlanManage extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /* 类型 1-145 2-库区、区县*/
    @NotNull(message = "类型不能为空")
    private Integer type;

    /* 内容 */
    @NotBlank(message = "内容不能为空")
    private String content;



}
