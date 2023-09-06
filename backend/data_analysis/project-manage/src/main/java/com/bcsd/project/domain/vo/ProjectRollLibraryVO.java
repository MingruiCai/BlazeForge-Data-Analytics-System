package com.bcsd.project.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.bcsd.common.utils.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 滚动项目库
 * @author liuliang
 * @since 2023-02-16
 */
@Getter
@Setter
@EqualsAndHashCode
public class ProjectRollLibraryVO{

    /* 序号 */
    @ExcelProperty(index = 0)
    private String no;

    /* 十四五规划类别 */
    @ExcelProperty(index = 1)
    private String type1;
    @ExcelProperty(index = 2)
    private String type2;
    @ExcelProperty(index = 3)
    private String type3;

    /* 范围（区域） */
    @ExcelProperty(index = 4)
    private String scopeRegion;

    /*单位名称*/
    @ExcelProperty(index = 5)
    private String unitName;

    /* 项目名称 */
    @ExcelProperty(index = 6)
    private String projectName;

    /* 项目批复单位 */
    @ExcelProperty(index = 7)
    private String projectReplyCompany;

    /*项目性质*/
    @ExcelProperty(index = 8)
    private String projectType;

    /*项目类别（1-工程建设类、0-非工程建设类）*/
    @ExcelProperty(index = 9)
    private String projectCategoryText;

    /* 建设内容和规模 */
    @ExcelProperty(index = 10)
    private String buildContentScale;

    /* 主要业绩 */
    @ExcelProperty(index = 11)
    private String mainAchievement;

    /* 计划建设周期开始年份 */
    @ExcelProperty(index = 12)
    private String planBuildCycleBeginYear;

    /* 计划建设周期结束年份 */
    @ExcelProperty(index = 13)
    private String planBuildCycleEndYear;

    /* 项目总投资（万元） */
    @ExcelProperty(index = 14)
    private String projectTotalInvest;

    /* 拟申请专项资金（万元）总额 */
    @ExcelProperty(index = 15)
    private String applySpecialCapitalTotal;

    /* 拟申请专项资金（万元）小计 */
    @ExcelProperty(index = 16)
    private String applySpecialCapitalSubtotal;

    @ExcelProperty(index = 17)
    private String apply1;

    @ExcelProperty(index = 18)
    private String apply2;

    @ExcelProperty(index = 19)
    private String apply3;

    public String getApply1() {
        if(StringUtils.isBlank(this.apply1)){
            return "0";
        }
        return apply1;
    }

    public String getApply2() {
        if(StringUtils.isBlank(this.apply2)){
            return "0";
        }
        return apply2;
    }

    public String getApply3() {
        if(StringUtils.isBlank(this.apply3)){
            return "0";
        }
        return apply3;
    }
}
