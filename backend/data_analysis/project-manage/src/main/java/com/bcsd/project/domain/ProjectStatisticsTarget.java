package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 项目统计指标
 * @author liuliang
 * @since 2023-03-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_statistics_target")
public class ProjectStatisticsTarget extends BaseInfo {

    private static final long serialVersionUID = 1L;

    private Long userId;

    /* 内容 */
    @NotBlank(message = "内容不能为空")
    private String content;

}
