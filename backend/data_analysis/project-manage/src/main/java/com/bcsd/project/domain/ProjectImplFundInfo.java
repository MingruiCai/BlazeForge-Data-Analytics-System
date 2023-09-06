package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 项目实施资金情况
 *
 * @author liuliang
 * @since 2023-02-23
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_fund_info")
public class ProjectImplFundInfo extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;
    /*计划-省市下达专项资金（万元）*/
    private String planPcIssueFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> planPcIssueFundList;
    /*计划-省市下达专项资金调整量（万元）*/
    private String planPcIssueFundAdjustmentAmount;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> planPcIssueFundAdjustmentAmountList;
    /*计划-区县下达专项资金（万元）*/
    private String planDcIssueFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> planDcIssueFundList;
    /*计划-区县下达专项资金调整量（万元）*/
    private String planDcIssueFundAdjustmentAmount;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> planDcIssueFundAdjustmentAmountList;
    /*计划-最终下达资金（万元）*/
    private String planFinalIssueFund;
    /*拨付-专项资金（万元）*/
    private String paymentFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> paymentFundList;
    /*拨付-配套资金（万元）*/
    private String paymentAssortFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> paymentAssortFundList;
    /*拨付-合计（万元）*/
    private String paymentTotalFund;
    /*完成-专项资金（万元）*/
    private String completeFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> completeFundList;
    /*完成-配套资金（万元）*/
    private String completeAssortFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> completeAssortFundList;
    /*完成-合计（万元）*/
    private String completeTotalFund;
    /*使用-专项资金（万元）*/
    private String useFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> useFundList;
    /*使用-配套资金（万元）*/
    private String useAssortFund;
    @TableField(exist = false)
    private List<ProjectImplFundDetailInfo> useAssortFundList;
    /*使用-合计（万元）*/
    private String useTotalFund;

    public ProjectImplFundInfo init(){
        this.setPlanPcIssueFund("0");
        this.setPlanPcIssueFundAdjustmentAmount("0");
        this.setPlanDcIssueFund("0");
        this.setPlanDcIssueFundAdjustmentAmount("0");
        this.setPlanFinalIssueFund("0");
        this.setPaymentFund("0");
        this.setPaymentAssortFund("0");
        this.setPaymentTotalFund("0");
        this.setCompleteFund("0");
        this.setCompleteAssortFund("0");
        this.setCompleteTotalFund("0");
        this.setUseFund("0");
        this.setUseAssortFund("0");
        this.setUseTotalFund("0");
        return this;
    }

    public String getPlanPcIssueFund() {
        if (StringUtils.isBlank(planPcIssueFund)){
            return "0";
        }
        return planPcIssueFund;
    }

    public String getPlanPcIssueFundAdjustmentAmount() {
        if (StringUtils.isBlank(planPcIssueFundAdjustmentAmount)){
            return "0";
        }
        return planPcIssueFundAdjustmentAmount;
    }

    public String getPlanDcIssueFund() {
        if (StringUtils.isBlank(planDcIssueFund)){
            return "0";
        }
        return planDcIssueFund;
    }

    public String getPlanDcIssueFundAdjustmentAmount() {
        if (StringUtils.isBlank(planDcIssueFundAdjustmentAmount)){
            return "0";
        }
        return planDcIssueFundAdjustmentAmount;
    }

    public String getPlanFinalIssueFund() {
        if (StringUtils.isBlank(planFinalIssueFund)){
            return "0";
        }
        return planFinalIssueFund;
    }

    public String getPaymentFund() {
        if (StringUtils.isBlank(paymentFund)){
            return "0";
        }
        return paymentFund;
    }

    public String getPaymentAssortFund() {
        if (StringUtils.isBlank(paymentAssortFund)){
            return "0";
        }
        return paymentAssortFund;
    }

    public String getPaymentTotalFund() {
        if (StringUtils.isBlank(paymentTotalFund)){
            return "0";
        }
        return paymentTotalFund;
    }

    public String getCompleteFund() {
        if (StringUtils.isBlank(completeFund)){
            return "0";
        }
        return completeFund;
    }

    public String getCompleteAssortFund() {
        if (StringUtils.isBlank(completeAssortFund)){
            return "0";
        }
        return completeAssortFund;
    }

    public String getCompleteTotalFund() {
        if (StringUtils.isBlank(completeTotalFund)){
            return "0";
        }
        return completeTotalFund;
    }

    public String getUseFund() {
        if (StringUtils.isBlank(useFund)){
            return "0";
        }
        return useFund;
    }

    public String getUseAssortFund() {
        if (StringUtils.isBlank(useAssortFund)){
            return "0";
        }
        return useAssortFund;
    }

    public String getUseTotalFund() {
        if (StringUtils.isBlank(useTotalFund)){
            return "0";
        }
        return useTotalFund;
    }
}
