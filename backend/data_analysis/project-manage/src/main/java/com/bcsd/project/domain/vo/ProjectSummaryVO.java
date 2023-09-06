package com.bcsd.project.domain.vo;

import com.bcsd.common.utils.ArithUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProjectSummaryVO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String column1;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String column2;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String column3;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String column4;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String column5;

    //项目总投资（万元）
    private String projectTotalInvest;
    //省市已下达专项补助资金（万元）
    private String planFinalIssueFund;
    /**计划安排项目（个）*/
    //项目个数
    private Integer projectTotalNum;
    //取消
    private Integer projectCancelNum;
    /**需实施项目实施进展情况 */
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
    //完成决算（竣工财务决算批复日期）
    private Integer projectFinalNum;
    /**-------------------------------*/
    //累计完成项目总投资（完成的专项资金+配套资金）
    private String completeTotalInvestment;
    /**专项资金拨付使用情况（万元） */
    //项目完成专项资金
    private String completeFund;
    //区县拨付专项资金
    private String paymentFund;
    //使用专项资金
    private String useFund;
    /**暂存专项资金 */
    //暂存专项资金-小计
    private String stagingSubtotalFund;
    //暂存专项资金-财政（下达专项资金-区县拨付）
    private String stagingFinanceFund;
    //暂存专项资金-业主（完成专项资金-区县拨付专项资金）
    private String stagingOwnerFund;

    /**----------------------------------------------------------------------------*/
    /**项目完成率*/
    //开工率（在建+完建/在建+完建+未开工）
    private String operationRate;
    //待建率（未开工/在建+完建+未开工）
    private String projectNoStartedNumRate;
    //在建率（在建/在建+完建+未开工）
    private String projectStartedNumRate;
    //完工率（完建/在建+完建+未开工）
    private String projectCompleteNumRate;
    //完工项目验收率（完成交工验收/完建）
    private String projectJiaoGongNumRate;
    //完工项目竣工验收率（完成竣工验收/完建）
    private String projectJunGongNumRate;
    //完工项目决算率（完成决算/完建）
    private String projectFinalNumRate;
    /**资金完成率*/
    //资金完成率（累计完成项目总投资/省市已下达专项补助资金）
    private String fundCompletionRate;
    //财政资金拨付率（项目完成专项资金/省市已下达专项补助资金）
    private String financeFundAppropriateRate;
    //财政资金暂存率（暂存专项资金-小计/省市已下达专项补助资金）
    private String financeFundStagingRate;

    public ProjectSummaryVO() {
    }

    public ProjectSummaryVO(String column1, String column2, String column3, String column4, String column5) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
        this.column4 = column4;
        this.column5 = column5;
        this.projectTotalInvest = "0";
        this.planFinalIssueFund = "0";
        this.projectTotalNum = 0;
        this.projectCancelNum = 0;
        this.projectSubtotalNum = 0;
        this.projectNoStartedNum = 0;
        this.projectStartedNum = 0;
        this.projectCompleteNum = 0;
        this.projectJiaoGongNum = 0;
        this.projectJunGongNum = 0;
        this.projectFinalNum = 0;
        this.completeTotalInvestment = "0";
        this.completeFund = "0";
        this.paymentFund = "0";
        this.useFund = "0";
        this.stagingSubtotalFund = "0";
        this.stagingFinanceFund = "0";
        this.stagingOwnerFund = "0";
    }

    public Integer getProjectSubtotalNum() {
        //未开工（待建）+在建+完建
        return getProjectNoStartedNum()+getProjectStartedNum()+getProjectCompleteNum();
    }

    public String getStagingSubtotalFund() {
        //暂存专项资金-小计
        return ArithUtil.add(getStagingFinanceFund(),getStagingOwnerFund());
    }

    public String getStagingFinanceFund() {
        //暂存专项资金-财政（下达专项资金-区县拨付）
        return ArithUtil.sub(getPlanFinalIssueFund(),getPaymentFund());
    }

    public String getStagingOwnerFund() {
        //暂存专项资金-业主（完成专项资金-区县拨付专项资金）
        return ArithUtil.sub(getCompleteFund(),getPaymentFund());
    }

    public String getTotalNum(){
        return String.valueOf(getProjectNoStartedNum()+getProjectStartedNum()+getProjectCompleteNum());
    }

    public String getOperationRate() {
        //开工率（在建+完建/在建+完建+未开工）
        return ArithUtil.rate(ArithUtil.div(String.valueOf(getProjectStartedNum()+getProjectCompleteNum()),getTotalNum(),3));
    }

    public String getProjectNoStartedNumRate() {
        //待建率（未开工/在建+完建+未开工）
        return ArithUtil.rate(ArithUtil.div(getProjectNoStartedNum().toString(),getTotalNum(),3));
    }

    public String getProjectStartedNumRate() {
        //在建率（在建/在建+完建+未开工）
        return ArithUtil.rate(ArithUtil.div(getProjectStartedNum().toString(),getTotalNum(),3));
    }

    public String getProjectCompleteNumRate() {
        //完工率（完建/在建+完建+未开工）
        return ArithUtil.rate(ArithUtil.div(getProjectCompleteNum().toString(),getTotalNum(),3));
    }

    public String getProjectJiaoGongNumRate() {
        //完工项目验收率（完成交工验收/完建）
        return ArithUtil.rate(ArithUtil.div(getProjectJiaoGongNum().toString(),getProjectCompleteNum().toString(),3));
    }

    public String getProjectJunGongNumRate() {
        //完工项目竣工验收率（完成竣工验收/完建）
        return ArithUtil.rate(ArithUtil.div(getProjectJunGongNum().toString(),getProjectCompleteNum().toString(),3));
    }

    public String getProjectFinalNumRate() {
        //完工项目决算率（完成决算/完建）
        return ArithUtil.rate(ArithUtil.div(getProjectFinalNum().toString(),getProjectCompleteNum().toString(),3));
    }

    public String getFundCompletionRate() {
        //资金完成率（累计完成项目总投资/省市已下达专项补助资金）
        return ArithUtil.rate(ArithUtil.div(getCompleteTotalInvestment(),getPlanFinalIssueFund(),3));
    }

    public String getFinanceFundAppropriateRate() {
        //财政资金拨付率（项目完成专项资金/省市已下达专项补助资金）
        return ArithUtil.rate(ArithUtil.div(getCompleteFund(),getPlanFinalIssueFund(),3));
    }

    public String getFinanceFundStagingRate() {
        //财政资金暂存率（暂存专项资金-小计/省市已下达专项补助资金）
        return ArithUtil.rate(ArithUtil.div(getStagingSubtotalFund(),getPlanFinalIssueFund(),3));
    }

    /**
     * 相加
     * @param vo
     */
    public void add(ProjectSummaryVO vo){
        this.projectTotalInvest = ArithUtil.add(this.projectTotalInvest,vo.getProjectTotalInvest());
        this.planFinalIssueFund = ArithUtil.add(this.planFinalIssueFund,vo.getPlanFinalIssueFund());
        this.projectTotalNum += vo.getProjectTotalNum();
        this.projectCancelNum += vo.getProjectCancelNum();
        this.projectSubtotalNum += vo.getProjectSubtotalNum();
        this.projectNoStartedNum += vo.getProjectNoStartedNum();
        this.projectStartedNum += vo.getProjectStartedNum();
        this.projectCompleteNum += vo.getProjectCompleteNum();
        this.projectJiaoGongNum += vo.getProjectJiaoGongNum();
        this.projectJunGongNum += vo.getProjectJunGongNum();
        this.projectFinalNum += vo.getProjectFinalNum();
        this.completeTotalInvestment = ArithUtil.add(this.completeTotalInvestment,vo.getCompleteTotalInvestment());
        this.completeFund = ArithUtil.add(this.completeFund,vo.getCompleteFund());
        this.paymentFund = ArithUtil.add(this.paymentFund,vo.getPaymentFund());
        this.useFund = ArithUtil.add(this.useFund,vo.getUseFund());
        this.stagingSubtotalFund = ArithUtil.add(this.stagingSubtotalFund,vo.getStagingSubtotalFund());
        this.stagingFinanceFund = ArithUtil.add(this.stagingFinanceFund,vo.getStagingFinanceFund());
        this.stagingOwnerFund = ArithUtil.add(this.stagingOwnerFund,vo.getStagingOwnerFund());
    }


}
