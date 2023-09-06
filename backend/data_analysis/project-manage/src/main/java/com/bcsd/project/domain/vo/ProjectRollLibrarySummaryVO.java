package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bcsd.common.utils.ArithUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ProjectRollLibrarySummaryVO {

    private String id;

    /*名称*/
    private String name;

    /*部门ID*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deptId;

    /*项目数量*/
    private Integer projectNum;

    /*新报*/
    @TableField("project_type_1")
    private Integer projectType1;

    /*续报*/
    @TableField("project_type_2")
    private Integer projectType2;

    /*延续*/
    @TableField("project_type_3")
    private Integer projectType3;

    /*续建*/
    @TableField("project_type_4")
    private Integer projectType4;

    /*项目总投资*/
    private String projectTotalInvest;

    /*拟申请专项补助资金(万元)*/
    private String applySpecialCapitalTotal;

    /*已下达专项补助资金(万元)*/
    private String planFinalIssueFund;

    /*拟申请专项资金(万元)-小计*/
    private String applySpecialCapitalSubtotal;

    /*拟申请专项资金(万元)-分年*/
    @TableField("apply_special_capital_1")
    private String applySpecialCapital1;
    @TableField("apply_special_capital_2")
    private String applySpecialCapital2;
    @TableField("apply_special_capital_3")
    private String applySpecialCapital3;

    /*子节点*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProjectRollLibrarySummaryVO> children;

    public ProjectRollLibrarySummaryVO() {
    }

    public ProjectRollLibrarySummaryVO(String name) {
        this.name = name;
        this.id = IdWorker.getIdStr();
        this.projectNum = 0;
        this.projectType1 = 0;
        this.projectType2 = 0;
        this.projectType3 = 0;
        this.projectType4 = 0;
        this.projectTotalInvest = "0";
        this.applySpecialCapitalTotal = "0";
        this.planFinalIssueFund = "0";
        this.applySpecialCapitalSubtotal = "0";
        this.applySpecialCapital1 = "0";
        this.applySpecialCapital2 = "0";
        this.applySpecialCapital3 = "0";
    }

    public void add(ProjectRollLibrarySummaryVO vo){
        this.projectNum += vo.getProjectNum();
        this.projectType1 += vo.getProjectType1();
        this.projectType2 += vo.getProjectType2();
        this.projectType3 += vo.getProjectType3();
        this.projectType4 += vo.getProjectType4();
        this.projectTotalInvest = ArithUtil.add(this.projectTotalInvest,vo.getProjectTotalInvest());
        this.applySpecialCapitalTotal = ArithUtil.add(this.applySpecialCapitalTotal,vo.getApplySpecialCapitalTotal());;
        this.planFinalIssueFund = ArithUtil.add(this.planFinalIssueFund,vo.getPlanFinalIssueFund());;
        this.applySpecialCapitalSubtotal = ArithUtil.add(this.applySpecialCapitalSubtotal,vo.getApplySpecialCapitalSubtotal());;
        this.applySpecialCapital1 = ArithUtil.add(this.applySpecialCapital1,vo.getApplySpecialCapital1());;
        this.applySpecialCapital2 = ArithUtil.add(this.applySpecialCapital2,vo.getApplySpecialCapital2());;
        this.applySpecialCapital3 = ArithUtil.add(this.applySpecialCapital3,vo.getApplySpecialCapital3());;
    }
}
