package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施信息快照
 * @author liuliang
 * @since 2023-02-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_info_snapshot")
public class ProjectImplInfoSnapshot extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*数据版本ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long versionId;

    /*年*/
    private String dataYear;

    /*月*/
    private String dataMonth;

    /*项目编号*/
    private String projectNo;

    /*项目名称*/
    private String projectName;

    /*单位名称*/
    private String unitName;

    /*省市/区县*/
    private String cityDistrict;

    /*范围（区域）*/
    private String scopeRegion;

    /*建设状态*/
    private String buildStatus;

    /*计划年度*/
    private String planYear;

    /*首次安排专项资金年度*/
    private Integer firstArrangeFundYear;

    /*规划优化类别*/
    private String planOptimizeType;

    /*十四五类别*/
    @TableField("type_145")
    private String type145;

    @TableField(exist = false)
    private String yearMonth;

}
