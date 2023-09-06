package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 滚动项目库
 * @author liuliang
 * @since 2023-02-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_roll_library")
public class ProjectRollLibrary extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /** 滚动周期ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long rollCycleId;

    /** 部门ID */
    private Long deptId;

    /** 项目编号 */
    private String projectNo;

    /* 项目名称 */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /* 规划优化类别 */
    private String planOptimizeType;

    /*十四五类别*/
    @TableField("type_145")
    private String type145;

    /*项目性质*/
    private String projectType;

    /*项目类别（1-工程建设类、0-非工程建设类）*/
    private Integer projectCategory;

    /* 项目批复单位 */
    private String projectReplyCompany;

    /* 范围（区域） */
    private String scopeRegion;

    /*前期工作深度*/
    private String earlyWorkDepth;

    /*单位名称*/
    private String unitName;

    /* 市/区县 */
    @NotBlank(message = "市/区县不能为空")
    private String cityDistrict;

    /*建设地点*/
    private String buildLocation;

    /*项目法人或责任单位*/
    private String projectLegalPerson;

    /* 建设内容和规模 */
    private String buildContentScale;

    /* 主要业绩 */
    private String mainAchievement;

    /* 计划建设周期开始年份 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer planBuildCycleBeginYear;

    /* 计划建设周期结束年份 */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer planBuildCycleEndYear;

    /* 项目总投资（万元） */
    private String projectTotalInvest;

    /* 拟申请专项资金（万元） */
    private String applySpecialCapital;

    /* 拟申请专项资金（万元）小计 */
    private String applySpecialCapitalSubtotal;

    /* 拟申请专项资金（万元）总额 */
    private String applySpecialCapitalTotal;

    /* 申请费用类别:直接费、其他费 */
    private String applyCostType;

    /* 审核状态 */
    private String state;

    /* 用户ID */
    private Long userId;

    /*入库申请表附件*/
    @JsonIgnore
    private String applicationFiles;

    /*入库申请表附件*/
    @TableField(exist = false)
    private JSONArray applicationFileList;

    /* 用户名称 */
    @TableField(exist = false)
    private String userName;

    /* 已下达资金 */
    @TableField(exist = false)
    private String planFinalIssueFund;

    /* 剩余资金 */
    @TableField(exist = false)
    private String surplusFund;

    /*操作记录*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Map> logs;

    @TableField(exist = false)
    @JsonIgnore
    private String type145End;

    public JSONArray getApplicationFileList() {
        if (StringUtils.isNotBlank(applicationFiles)){
            return JSON.parseArray(applicationFiles);
        }
        return applicationFileList==null?new JSONArray():applicationFileList;
    }


    public String getSurplusFund() {
        return ArithUtil.sub(getApplySpecialCapitalTotal(),getPlanFinalIssueFund());
    }

    public String getType145End() {
        if (StringUtils.isNotBlank(type145)){
            if ("00".equals(type145.substring(4,6))){
                return type145.substring(0,4)+"9999";
            }else if ("00".equals(type145.substring(6,8))){
                return type145.substring(0,6)+"99";
            }else{
                return type145;
            }
        }
        return type145End;
    }
}
