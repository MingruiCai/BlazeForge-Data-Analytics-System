package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施基本信息
 * @author liuliang
 * @since 2023-02-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("project_impl_basic_info")
public class ProjectImplBasicInfo extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long deptId;

    /*项目计划ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectPlanId;

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

    /*乡镇（街道）*/
    private String townshipStreet;

    /*村（社区）*/
    private String villageCommunity;

    /*建设地点1*/
    @TableField("build_location_1")
    private String buildLocation1;

    /*建设地点2*/
    @TableField("build_location_2")
    private String buildLocation2;

    /*地理信息*/
    private String geographyPosition;

    /*项目性质*/
    private String projectType;

    /*项目类别（1-工程建设类、0-非工程建设类）*/
    private Integer projectCategory;

    /*项目法人或责任单位*/
    private String projectLegalPerson;

    /*建设状态*/
    private Integer buildStatus;

    /*计划年度*/
    private String planYear;

    /*计划建设周期开始时间*/
    private Integer planBuildCycleBeginYear;

    /*计划建设周期结束时间*/
    private Integer planBuildCycleEndYear;

    /*首次安排专项资金年度*/
    private Integer firstArrangeFundYear;

    /*规划优化类别*/
    private String planOptimizeType;

    /*十四五类别*/
    @TableField("type_145")
    private String type145;

    /*总体建设规模*/
    private String overallConstructionScale;

    /*年度建设任务（与年度完成投资相匹配）*/
    private String yearBuildTask;

    /*专项资金（万元)*/
    private String specialFunds;

    /*配套资金（万元）*/
    private String counterpartFunds;

    /*项目状态*/
    private String projectStatus;

    /* 用户ID */
    private Long userId;

    /*项目总投资（万元）*/
    private String projectTotalInvest;

    /* 用户名称 */
    @TableField(exist = false)
    private String userName;

    /*已安排专项资金（万元）*/
    @TableField(exist = false)
    private String scheduledArrangeFund;

    /*部门ID集合*/
    @TableField(exist = false)
    private String deptIds;
}
