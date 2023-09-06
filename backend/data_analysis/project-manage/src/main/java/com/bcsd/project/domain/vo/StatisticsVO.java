package com.bcsd.project.domain.vo;

import com.bcsd.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticsVO {

    /*编号*/
    private String code;
    /*名称*/
    private String name;
    /*项目数量*/
    private Integer num;
    /*资金*/
    private String fund;
    /*目标*/
    private String target;
    /*百分比*/
    private String rate;

    /*待建-未开工*/
    private Integer dj;
    /*在建*/
    private Integer zj;
    /*完建*/
    private Integer wj;
    /*验收*/
    private Integer ys;

    /*竣工*/
    @JsonIgnore
    private Integer jg;
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

    //开工率（在建+完建/在建+完建+未开工）
    private String operationRate;
    //竣工验收率
    private String jungongRate;
    //决算率
    private String jueSuanRate;
    //专项资金拨付率
    private String paymentFundRate;
    //专项资金完成率
    private String completeFundRate;

    public StatisticsVO() {
    }

    public StatisticsVO(Integer num, String fund) {
        this.num = num;
        this.fund = fund;
    }

    public StatisticsVO(Integer dj, Integer zj, Integer wj, Integer ys) {
        this.dj = dj;
        this.zj = zj;
        this.wj = wj;
        this.ys = ys;
    }

    public void initStatus(){
        this.setDj(0);
        this.setZj(0);
        this.setWj(0);
        this.setYs(0);
    }

    @JsonIgnore
    public Double getFundToDouble() {
        return Double.valueOf(StringUtils.isBlank(fund)?"0":fund);
    }
}
