package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.vo.FileVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 计划管理
 *
 * @author liuliang
 * @since 2023-02-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_plan_manage")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectPlanManage extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /** 部门ID */
    private Long deptId;

    /*滚动项目ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "滚动项目ID不能为空")
    private Long rollLibraryId;

    /*项目编号*/
    @NotBlank(message = "项目编号不能为空")
    private String projectNo;

    /*项目名称*/
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /* 范围（区域） */
    private String scopeRegion;

    /*单位名称*/
    private String unitName;

    /*市/区县*/
    @NotBlank(message = "市/区县不能为空")
    private String cityDistrict;

    /*计划建设周期开始时间*/
    private Integer planBuildCycleBeginYear;

    /*计划建设周期结束时间*/
    private Integer planBuildCycleEndYear;

    /* 规划优化类别 */
    private String planOptimizeType;

    /*十四五类别*/
    @TableField("type_145")
    private String type145;

    /*项目性质*/
    private String projectType;

    /*项目类别（1-工程建设类、0-非工程建设类）*/
    private Integer projectCategory;

    /*批次 1-提前批 2-第二批*/
    @NotNull(message = "批次不能为空")
    private Integer batch;

    /*建设地点*/
    private String buildLocation;

    /*项目法人或责任单位*/
    private String projectLegalPerson;

    /*计划年度*/
    @NotNull(message = "计划年度不能为空")
    private Integer planYear;

    /*年度建设任务*/
    private String yearBuildTask;

    /*项目总投资（万元）*/
    private String projectTotalInvest;

    /* 专项资金 */
    @TableField(exist = false)
    private String specialFunds;

    /*首次安排专项资金年度*/
    private Integer firstArrangeFundYear;

    /*已安排专项资金（万元）*/
    private String scheduledArrangeFund;

    /*已完成专项资金（万元）*/
    private String completedArrangeFund;

    /*本年度计划完成投资（万元）*/
    private String yearPlanCompletedInvest;

    /*本次安排专项资金（万元）*/
    private String currentScheduledArrangeFund;

    /* 审核状态 */
    private String state;

    /* 评级 */
    private String ratingLevel;

    /* 用户ID */
    private Long userId;

    /*入库申请表附件*/
    @JsonIgnore
    private String applicationFiles;

    /*审核附件JSON*/
    @JsonIgnore
    private String approvalFilesJson;

    /*入库申请表附件*/
    @TableField(exist = false)
    private JSONArray applicationFileList;

    /*审核附件*/
    @TableField(exist = false)
    private List<FileVO> approvalFiles;

    /*用户名称*/
    @TableField(exist = false)
    private String userName;

    /*批次显示名称*/
    @TableField(exist = false)
    private String batchName;

    /*拟申请专项资金*/
    @TableField(exist = false)
    private String applySpecialCapitalTotal;

    @TableField(exist = false)
    @JsonIgnore
    private String type145End;

    @TableField(exist = false)
    private Double projectTotalInvestMin;

    @TableField(exist = false)
    private Double projectTotalInvestMax;

    /*操作记录*/
    @TableField(exist = false)
    List<Map> logs;

    public JSONArray getApplicationFileList() {
        if (StringUtils.isNotBlank(applicationFiles)){
            return JSON.parseArray(applicationFiles);
        }
        return applicationFileList==null?new JSONArray():applicationFileList;
    }

    public List<FileVO> getApprovalFiles() {
        if (StringUtils.isNotBlank(approvalFilesJson)){
            return JSON.parseArray(approvalFilesJson,FileVO.class);
        }
        return approvalFiles;
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

    public Double getProjectTotalInvestMin() {
        if (StringUtils.isNull(projectTotalInvestMin)&&StringUtils.isNotNull(projectTotalInvestMax)){
            return 0d;
        }
        return projectTotalInvestMin;
    }

    public Double getProjectTotalInvestMax() {
        if (StringUtils.isNotNull(projectTotalInvestMin)&&StringUtils.isNull(projectTotalInvestMax)){
            return 99999999999d;
        }
        if (StringUtils.isNotNull(projectTotalInvestMax)){
            return ArithUtil.sub(projectTotalInvestMax,0.0001);
        }
        return projectTotalInvestMax;
    }

    public String getSpecialFunds() {
        return ArithUtil.add(getScheduledArrangeFund(),getCurrentScheduledArrangeFund());
    }

}
