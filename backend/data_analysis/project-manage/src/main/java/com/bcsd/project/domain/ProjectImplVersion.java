package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.ArithUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施版本记录
 *
 * @author liuliang
 * @since 2023-02-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_version")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectImplVersion extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /**----------------------------基本信息-----------------------------*/
    /*项目ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;
    /*部门ID*/
    private Long deptId;
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
    /*地理信息*/
    private String geographyPosition;
    /*项目性质*/
    private String projectType;
    /*项目类别（1-工程建设类、0-非工程建设类）*/
    private Integer projectCategory;
    /*项目法人或责任单位*/
    private String projectLegalPerson;
    /*总体建设规模*/
    private String overallConstructionScale;
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
    /*专项资金（万元)*/
    private String specialFunds;
    /*配套资金（万元）*/
    private String counterpartFunds;
    /*项目总投资（万元）*/
    private String projectTotalInvest;
    /**----------------------------实施信息-----------------------------*/
    //工作阶段
    private String workPhase;
    /*项目建议书-批复日期*/
    private String ppAd;
    /*项目建议书-估算投资*/
    private String ppEi;
    /*可行性研究-批复日期*/
    private String fsAd;
    /*可行性研究-估算投资*/
    private String fsEi;
    /*初步设计-初设批复日期*/
    private String idIdad;
    /*初步设计-概算批复日期*/
    private String idFead;
    /*初步设计-概算投资（万元）-直接费*/
    private String idFeDf;
    /*初步设计-概算投资（万元）-其它费*/
    private String idFeOf;
    /*初步设计-概算投资（万元）-预备费*/
    private String idFeRf;
    /*初步设计-概算投资（万元）-总计*/
    private String idFeTotal;
    /*施工图设计-工程预算金额*/
    private String cddPba;
    /*施工图设计-施工图批复或通过审查日期*/
    private String cddCdad;
    /*施工图设计-施工图预算通过审核日期*/
    private String cddCdbad;
    /*施工图设计-施工招标模式*/
    private String cddCbm;
    /*施工图设计-预算评审完成日期*/
    private String cddBrcd;
    /*施工图招标-招标公告发布日期*/
    private String cdbBard;
    /*施工图招标-招标最高限价（万元）*/
    private String cdbBcp;
    /*施工图招标-开标日期*/
    private String cdbBod;
    /*施工图招标-中标候选人公示开始日期*/
    private String cdbBwcpsd;
    /*施工图招标-中标结果公示开始日期*/
    private String cdbBwrpsd;
    /*施工图招标-中标通知书发出日期*/
    private String cdbBwnid;
    /*施工图招标-合同签订日期*/
    private String cdbCsd;
    /*开工-开工日期*/
    private String swSwd;
    /*完工情况-完工日期*/
    private String ccCd;
    /*交工验收-验收日期*/
    private String haAd;
    /*工程结算-结算日期*/
    private String psSd;
    /*工程结算-结算审计金额（万元）*/
    private String psSaa;
    /*竣工验收-验收日期*/
    private String caAd;
    /*竣工财务决算-决算批复文号*/
    private String cfsFaano;
    /*竣工财务决算-决算批复时间*/
    private String cfsFaad;
    /*竣工财务决算-决算金额（万元）*/
    private String cfsFaa;
    /*竣工财务决算-决算金额-专项资金（万元）*/
    private String cfsFaaSf;
    /**----------------------------资金信息-----------------------------*/
    /*计划-省市下达专项资金（万元）*/
    private String planPcIssueFund;
    /*计划-省市下达专项资金调整量（万元）*/
    private String planPcIssueFundAdjustmentAmount;
    /*计划-区县下达专项资金（万元）*/
    private String planDcIssueFund;
    /*计划-区县下达专项资金调整量（万元）*/
    private String planDcIssueFundAdjustmentAmount;
    /*计划-最终下达资金（万元）*/
    private String planFinalIssueFund;
    /*拨付-专项资金（万元）*/
    private String paymentFund;
    /*拨付-配套资金（万元）*/
    private String paymentAssortFund;
    /*拨付-合计（万元）*/
    private String paymentTotalFund;
    /*完成-专项资金（万元）*/
    private String completeFund;
    /*完成-配套资金（万元）*/
    private String completeAssortFund;
    /*完成-合计（万元）*/
    private String completeTotalFund;
    /*使用-专项资金（万元）*/
    private String useFund;
    /*使用-配套资金（万元）*/
    private String useAssortFund;
    /*使用-合计（万元）*/
    private String useTotalFund;
    /**----------------------------进展情况------------------------------*/
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
    /*受益村、社区*/
    private String benefitVillage;
    /*受益人数*/
    private String benefitNumberOfPeople;
    /*受益三峡移民人数*/
    private String benefitSxNumberOfPeople;
    /*完成主要实物量*/
    private String completeMainNumber;
    /**--------------------------JSON详细信息----------------------------*/
    /*基本信息*/
    private String basicInfo;
    /*实施信息*/
    private String implInfo;
    /*资金信息*/
    private String fundsInfo;
    /*进展信息*/
    private String evolveInfo;
    /*文件资料*/
    private String filesInfo;
    /*VR信息*/
    private String vrInfo;

    /*版本描述*/
    private String versionDescription;

    /*数据来源 0-录入数据 1-导入数据（历史数据）*/
    private Integer dataSource;

    /*本年度计划完成投资（万元）*/
    @TableField(exist = false)
    private String yearPlanCompletedInvest;
    /*本次安排专项资金（万元）*/
    @TableField(exist = false)
    private String currentScheduledArrangeFund;
    /*部门ID集合*/
    @TableField(exist = false)
    private String deptIds;

    /**
     * 扩展字段
     */
    @TableField(exist = false)
    private String field1;
    @TableField(exist = false)
    private String field2;
    @TableField(exist = false)
    private String field3;
    @TableField(exist = false)
    private String field4;
    @TableField(exist = false)
    private String field5;
    @TableField(exist = false)
    private String field6;

    /**
     * 已开工形象进度
     */
    @TableField(exist = false)
    private String rateOfProgress;

    public String getRateOfProgress() {
        return ArithUtil.rate(ArithUtil.div(getPlanPcIssueFund(),getProjectTotalInvest()));
    }
}
