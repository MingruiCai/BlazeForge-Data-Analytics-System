package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.bcsd.common.utils.ArithUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDataSummaryVO {

    private String id;
    /*名称*/
    private String name;

    //项目数量
    private Integer projectNum;
    //项目概算总投资
    private String projectTotalInvest;
    //下达专项资金(累计/当年)
    private String planFinalIssueFund;
    /**--项目实施进展情况--*/
    //小计
    private Integer projectSubtotalNum;
    //未开工（待建）
    private Integer projectNoStartedNum;
    //在建
    private Integer projectStartedNum;
    //完建
    private Integer projectCompleteNum;
    //完成交工验收（交工验收日期）
    private Integer projectJiaoGongNum;
    //完成竣工验收（竣工验收日期）
    private Integer projectJunGongNum;
    /**-------------------------------*/
    //完成项目总投资（累计/当年）（完成的专项资金+配套资金）
    private String completeTotalInvestment;
    //项目完成专项资金（累计/当年）
    private String completeFund;
    /**专项资金拨付和暂存情况（万元）*/
    //财政部门累计拨付
    private String paymentTotalFund;
    //暂存专项资金-财政（下达专项资金-区县拨付）
    private String stagingFinanceFund;
    //专项资金拨付率（拨付专项资金/下达专项资金）
    private String financeFundAppropriateRate;
    //专项资金完成率（完成专项资金/下达专项资金）
    private String fundCompletionRate;

    /*子节点*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProjectDataSummaryVO> children;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String index;

    public ProjectDataSummaryVO() {
    }

    public ProjectDataSummaryVO(String name) {
        this.id = IdWorker.getIdStr();
        this.name = name;
        //项目数量
        this.projectNum = 0;
        //项目概算总投资
        this.projectTotalInvest = "0";
        //累计下达专项资金
        this.planFinalIssueFund = "0";
        //未开工（待建）
        this.projectNoStartedNum = 0;
        //在建
        this.projectStartedNum = 0;
        //完建
        this.projectCompleteNum = 0;
        //完成交工验收（交工验收日期）
        this.projectJiaoGongNum = 0;
        //完成竣工验收（竣工验收日期）
        this.projectJunGongNum = 0;
        //累计完成项目总投资（完成的专项资金+配套资金）
        this.completeTotalInvestment = "0";
        //项目完成专项资金
        this.completeFund = "0";
        //财政部门累计拨付
        this.paymentTotalFund = "0";
        //暂存专项资金-财政（下达专项资金-区县拨付）
        this.stagingFinanceFund = "0";
    }

    public void add(ProjectDataSummaryVO params){
        //项目数量
        this.projectNum += params.getProjectNum();
        //项目概算总投资
        this.projectTotalInvest = ArithUtil.add(this.projectTotalInvest,params.getProjectTotalInvest());
        //累计下达专项资金
        this.planFinalIssueFund = ArithUtil.add(this.planFinalIssueFund,params.getPlanFinalIssueFund());
        //未开工（待建）
        this.projectNoStartedNum += params.getProjectNoStartedNum();
        //在建
        this.projectStartedNum += params.getProjectStartedNum();
        //完建
        this.projectCompleteNum += params.getProjectCompleteNum();
        //完成交工验收（交工验收日期）
        this.projectJiaoGongNum += params.getProjectJiaoGongNum();
        //完成竣工验收（竣工验收日期）
        this.projectJunGongNum += params.getProjectJunGongNum();
        //累计完成项目总投资（完成的专项资金+配套资金）
        this.completeTotalInvestment = ArithUtil.add(this.completeTotalInvestment,params.getCompleteTotalInvestment());
        //项目完成专项资金
        this.completeFund = ArithUtil.add(this.completeFund,params.getCompleteFund());
        //财政部门累计拨付
        this.paymentTotalFund = ArithUtil.add(this.paymentTotalFund,params.getPaymentTotalFund());
        //暂存专项资金-财政（下达专项资金-区县拨付）
        this.stagingFinanceFund = ArithUtil.add(this.stagingFinanceFund,params.getStagingFinanceFund());
    }

    public Integer getProjectNoStartedNum() {
        if (projectNoStartedNum==null){
            return 0;
        }
        return projectNoStartedNum;
    }

    public Integer getProjectStartedNum() {
        if (projectStartedNum==null){
            return 0;
        }
        return projectStartedNum;
    }

    public Integer getProjectCompleteNum() {
        if (projectCompleteNum==null){
            return 0;
        }
        return projectCompleteNum;
    }

    public Integer getProjectSubtotalNum() {
        return projectNoStartedNum+projectStartedNum+projectCompleteNum;
    }

    public String getFinanceFundAppropriateRate() {
        //专项资金拨付率（拨付专项资金/下达专项资金）
        return ArithUtil.rate(ArithUtil.div(getPaymentTotalFund(),getPlanFinalIssueFund(),3));
    }

    public String getFundCompletionRate() {
        //专项资金完成率（完成专项资金/下达专项资金）
        return ArithUtil.rate(ArithUtil.div(getCompleteFund(),getPlanFinalIssueFund(),3));
    }
}
