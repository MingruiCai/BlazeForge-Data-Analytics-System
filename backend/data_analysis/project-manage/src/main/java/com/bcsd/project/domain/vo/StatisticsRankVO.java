package com.bcsd.project.domain.vo;

import com.bcsd.common.utils.ArithUtil;
import com.bcsd.common.utils.DictUtils;
import com.bcsd.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsRankVO {

    /*编号*/
    private String code;
    /*名称*/
    private String name;
    /*排名*/
    private Integer index;
    //得分
    private Double score;
    //开工率（在建+完建/在建+完建+未开工）
    private String operationRate;
    //完工率
    private String completeRate;
    //决算率
    private String jueSuanRate;
    //专项资金拨付率
    private String paymentFundRate;
    //专项资金完成率
    private String completeFundRate;

    /*待建-未开工*/
    @JsonIgnore
    private Integer dj;
    /*在建*/
    @JsonIgnore
    private Integer zj;
    /*完建*/
    @JsonIgnore
    private Integer wj;
    /*决算*/
    @JsonIgnore
    private Integer js;
    //下达专项资金
    @JsonIgnore
    private String planFinalIssueFund;
    //完成专项资金
    @JsonIgnore
    private String completeFund;
    //拨付专项资金
    @JsonIgnore
    private String paymentFund;

    public StatisticsRankVO() {}

    public StatisticsRankVO(Integer dj, Integer zj, Integer wj, Integer js, String planFinalIssueFund, String completeFund, String paymentFund) {
        this.dj = dj;
        this.zj = zj;
        this.wj = wj;
        this.js = js;
        this.planFinalIssueFund = planFinalIssueFund;
        this.completeFund = completeFund;
        this.paymentFund = paymentFund;
    }
}
