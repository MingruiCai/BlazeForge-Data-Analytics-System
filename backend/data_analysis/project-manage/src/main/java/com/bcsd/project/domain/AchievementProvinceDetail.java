package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 滚动周期表
 *
 * @author liuliang
 * @since 2023-02-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("achievement_province_detail")
public class AchievementProvinceDetail extends BaseInfo {
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    /**区县年度绩效ID*/
    private Long achievementId;
    /**年度*/
    private Integer year;
    /**位置*/
    private Integer position;
    /**指标类型*/
    private String goalType;
    /**指标名称*/
    private String goalName;
    /**指标值*/
    private String goalValue;
    /**指标执行值*/
    private String goalExecute;
    /**指标未完成原因和改进措施*/
    private String goalRemark;
}
