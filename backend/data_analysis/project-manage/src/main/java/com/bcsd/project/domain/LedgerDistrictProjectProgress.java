package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.ArithUtil;
import com.bcsd.project.domain.vo.ProjectRollLibrarySummaryVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 项目进展台账
 * @author liuliang
 * @since 2023-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ledger_district_project_progress")
public class LedgerDistrictProjectProgress extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*区县进展台账ID*/
    private Long ledgerDistrictProgressId;

    /*数据年*/
    private Integer dataYear;

    /*数据周*/
    private Integer dataWeek;

    /*部门ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /*区县编号*/
    private String cityDistrict;

    /*项目编号*/
    private String projectNo;

    /*项目名称*/
    private String projectName;

    /*年度建设任务（与年度完成投资相匹配）*/
    private String yearBuildTask;

    /*项目性质*/
    private String projectType;

    /*项目法人或责任单位*/
    private String projectLegalPerson;

    /*开工年月*/
    private String beginDate;

    /*项目总投资*/
    private String projectTotalInvest;

    /*项目总投资-专项资金*/
    private String projectTotalInvestSpecialFund;

    /*年度计划投资*/
    private String yearPlanInvest;

    /*年度计划投资-专项资金*/
    private String yearPlanInvestSpecialFund;

    /*本月前已完成投资*/
    private String completedInvest;

    /*本月前已完成投资-专项资金*/
    private String completedInvestSpecialFund;

    /*本月新增完成投资*/
    private String addCompletedInvest;

    /*本月新增完成投资-专项资金*/
    private String addCompletedInvestSpecialFund;

    /*投资完成率*/
    private String investCompletedRate;

    /*投资完成率-专项资金投资完成率*/
    private String specialFundInvestCompletedRate;

    /*本月前已拨付资金*/
    private String payFund;

    /*本月前已拨付资金-专项资金*/
    private String paySpecialFund;

    /*本月新增拨付资金*/
    private String addPayFund;

    /*本月新增拨付资金-专项资金*/
    private String addPaySpecialFund;

    /*年度资金拨付率*/
    private String yearFundPayRate;

    /*年度资金拨付率-专项资金拨付率*/
    private String yearSpecialFundPayRate;

    /*本月前形象进度*/
    private String graphicProgress;

    /*本月新增形象进度*/
    private String addGraphicProgress;

    /*本月开工日期*/
    private String monthBeginDate;

    /*前期工作程度*/
    private String prophaseWorkExtent;

    /*未开工主要原因*/
    private String notStatedMainCause;

    /*拟开工日期*/
    private String planBeginDate;

    /*本月主要措施*/
    private String mainMethod;

    /*保障开工拟采取主要措施*/
    private String ensureBeginMainMethod;

    /*报告期当日吸纳就业人数*/
    private String employPerson;

    /*报告期当日吸纳就业人数-农村劳动力就业人数*/
    private String employPersonVillage;

    /*本年累计已吸纳就业人数*/
    private String employPersonTotal;

    /*本年累计已吸纳就业人数-农村劳动力就业人数*/
    private String employPersonVillageTotal;

    /*以工代赈项目*/
    private String workReplaceAid;

    /*本年累计已吸纳当地农村就业人数*/
    private String employPersonLocalVillageTotal;

    /*本年累计已发放劳动报酬*/
    private String alreadyProvideLabourRewardTotal;

    /*人口*/
    private String population;

    /*本年累计发放报酬*/
    private String provideLabourRewardTotal;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deptIds;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<LedgerDistrictProjectProgress> children;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer index;

    public LedgerDistrictProjectProgress() {
    }

    public LedgerDistrictProjectProgress(String name) {
        this.name = name;
        this.setId(IdWorker.getId());
        /*项目总投资*/
        this.projectTotalInvest = "0";
        /*项目总投资-专项资金*/
        this.projectTotalInvestSpecialFund = "0";
        /*年度计划投资*/
        this.yearPlanInvest = "0";
        /*年度计划投资-专项资金*/
        this.yearPlanInvestSpecialFund = "0";
        /*本月前已完成投资*/
        this.completedInvest = "0";
        /*本月前已完成投资-专项资金*/
        this.completedInvestSpecialFund = "0";
        /*本月新增完成投资*/
        this.addCompletedInvest = "0";
        /*本月新增完成投资-专项资金*/
        this.addCompletedInvestSpecialFund = "0";
        /*投资完成率*/
        this.investCompletedRate = "0";
        /*投资完成率-专项资金投资完成率*/
        this.specialFundInvestCompletedRate = "0";
        /*本月前已拨付资金*/
        this.payFund = "0";
        /*本月前已拨付资金-专项资金*/
        this.paySpecialFund = "0";
        /*本月新增拨付资金*/
        this.addPayFund = "0";
        /*本月新增拨付资金-专项资金*/
        this.addPaySpecialFund = "0";
        /*年度资金拨付率*/
        this.yearFundPayRate = "0";
        /*年度资金拨付率-专项资金拨付率*/
        this.yearSpecialFundPayRate = "0";
        /*报告期当日吸纳就业人数*/
        this.employPerson = "0";
        /*报告期当日吸纳就业人数-农村劳动力就业人数*/
        this.employPersonVillage = "0";
        /*本年累计已吸纳就业人数*/
        this.employPersonTotal = "0";
        /*本年累计已吸纳就业人数-农村劳动力就业人数*/
        this.employPersonVillageTotal = "0";
        /*本年累计已吸纳当地农村就业人数*/
        this.employPersonLocalVillageTotal = "0";
        /*本年累计已发放劳动报酬*/
        this.alreadyProvideLabourRewardTotal = "0";
        /*人口*/
        this.population = "0";
        /*本年累计发放报酬*/
        this.provideLabourRewardTotal = "0";
    }

    public LedgerDistrictProjectProgress(Long id,String projectNo) {
        this.projectNo = projectNo;
        /*项目总投资*/
        this.projectTotalInvest = "0";
        /*项目总投资-专项资金*/
        this.projectTotalInvestSpecialFund = "0";
        /*年度计划投资*/
        this.yearPlanInvest = "0";
        /*年度计划投资-专项资金*/
        this.yearPlanInvestSpecialFund = "0";
        /*本月前已完成投资*/
        this.completedInvest = "0";
        /*本月前已完成投资-专项资金*/
        this.completedInvestSpecialFund = "0";
        /*本月新增完成投资*/
        this.addCompletedInvest = "0";
        /*本月新增完成投资-专项资金*/
        this.addCompletedInvestSpecialFund = "0";
        /*投资完成率*/
        this.investCompletedRate = "0";
        /*投资完成率-专项资金投资完成率*/
        this.specialFundInvestCompletedRate = "0";
        /*本月前已拨付资金*/
        this.payFund = "0";
        /*本月前已拨付资金-专项资金*/
        this.paySpecialFund = "0";
        /*本月新增拨付资金*/
        this.addPayFund = "0";
        /*本月新增拨付资金-专项资金*/
        this.addPaySpecialFund = "0";
        /*年度资金拨付率*/
        this.yearFundPayRate = "0";
        /*年度资金拨付率-专项资金拨付率*/
        this.yearSpecialFundPayRate = "0";
    }

    public void add(LedgerDistrictProjectProgress params){
        /*项目总投资*/
        this.projectTotalInvest = ArithUtil.add(this.projectTotalInvest,params.getProjectTotalInvest());
        /*项目总投资-专项资金*/
        this.projectTotalInvestSpecialFund = ArithUtil.add(this.projectTotalInvestSpecialFund,params.getProjectTotalInvestSpecialFund());
        /*年度计划投资*/
        this.yearPlanInvest = ArithUtil.add(this.yearPlanInvest,params.getYearPlanInvest());
        /*年度计划投资-专项资金*/
        this.yearPlanInvestSpecialFund = ArithUtil.add(this.yearPlanInvestSpecialFund,params.getYearPlanInvestSpecialFund());
        /*本月前已完成投资*/
        this.completedInvest = ArithUtil.add(this.completedInvest,params.getCompletedInvest());
        /*本月前已完成投资-专项资金*/
        this.completedInvestSpecialFund = ArithUtil.add(this.completedInvestSpecialFund,params.getCompletedInvestSpecialFund());
        /*本月新增完成投资*/
        this.addCompletedInvest = ArithUtil.add(this.addCompletedInvest,params.getAddCompletedInvest());
        /*本月新增完成投资-专项资金*/
        this.addCompletedInvestSpecialFund = ArithUtil.add(this.addCompletedInvestSpecialFund,params.getAddCompletedInvestSpecialFund());
        /*投资完成率*/
        this.investCompletedRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getCompletedInvest(),getAddCompletedInvest()),getYearPlanInvest()));
        /*投资完成率-专项资金投资完成率*/
        this.specialFundInvestCompletedRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getCompletedInvestSpecialFund(),getAddCompletedInvestSpecialFund()),getYearPlanInvestSpecialFund()));
        /*本月前已拨付资金*/
        this.payFund = ArithUtil.add(this.payFund,params.getPayFund());
        /*本月前已拨付资金-专项资金*/
        this.paySpecialFund = ArithUtil.add(this.paySpecialFund,params.getPaySpecialFund());
        /*本月新增拨付资金*/
        this.addPayFund = ArithUtil.add(this.addPayFund,params.getAddPayFund());
        /*本月新增拨付资金-专项资金*/
        this.addPaySpecialFund = ArithUtil.add(this.addPaySpecialFund,params.getAddPaySpecialFund());
        /*年度资金拨付率*/
        this.yearFundPayRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getPayFund(),getAddPayFund()),getYearPlanInvest()));
        /*年度资金拨付率-专项资金拨付率*/
        this.yearSpecialFundPayRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getPaySpecialFund(),getAddPaySpecialFund()),getYearPlanInvestSpecialFund()));
        /*报告期当日吸纳就业人数*/
        this.employPerson = ArithUtil.add(this.employPerson,params.getEmployPerson());
        /*报告期当日吸纳就业人数-农村劳动力就业人数*/
        this.employPersonVillage = ArithUtil.add(this.employPersonVillage,params.getEmployPersonVillage());
        /*本年累计已吸纳就业人数*/
        this.employPersonTotal = ArithUtil.add(this.employPersonTotal,params.getEmployPersonTotal());
        /*本年累计已吸纳就业人数-农村劳动力就业人数*/
        this.employPersonVillageTotal = ArithUtil.add(this.employPersonVillageTotal,params.getEmployPersonVillageTotal());
        /*本年累计已吸纳当地农村就业人数*/
        this.employPersonLocalVillageTotal = ArithUtil.add(this.employPersonLocalVillageTotal,params.getEmployPersonLocalVillageTotal());
        /*本年累计已发放劳动报酬*/
        this.alreadyProvideLabourRewardTotal = ArithUtil.add(this.alreadyProvideLabourRewardTotal,params.getAlreadyProvideLabourRewardTotal());
        /*人口*/
        this.population = ArithUtil.add(this.population,params.getPopulation());
        /*本年累计发放报酬*/
        this.provideLabourRewardTotal = ArithUtil.add(this.provideLabourRewardTotal,params.getProvideLabourRewardTotal());
    }

    public void initRate(){
        /*投资完成率*/
        this.investCompletedRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getCompletedInvest(),getAddCompletedInvest()),getYearPlanInvest()));
        /*投资完成率-专项资金投资完成率*/
        this.specialFundInvestCompletedRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getCompletedInvestSpecialFund(),getAddCompletedInvestSpecialFund()),getYearPlanInvestSpecialFund()));
        /*年度资金拨付率*/
        this.yearFundPayRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getPayFund(),getAddPayFund()),getYearPlanInvest()));
        /*年度资金拨付率-专项资金拨付率*/
        this.yearSpecialFundPayRate = ArithUtil.rate(ArithUtil.div(ArithUtil.add(getPaySpecialFund(),getAddPaySpecialFund()),getYearPlanInvestSpecialFund()));
        /*本月前形象进度*/
        this.graphicProgress = ArithUtil.rate(ArithUtil.div(getCompletedInvest(),getYearPlanInvest()));
        /*本月新增形象进度*/
        this.addGraphicProgress = ArithUtil.rate(ArithUtil.div(getAddCompletedInvest(),getYearPlanInvest()));
    }
}
