package com.bcsd.project.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticsInfoVO {

    //项目数量
    private Integer projectNum;
    //总投资
    private String projectTotalInvest;
    //下达专项资金
    private String planFinalIssueFund;
    //完成专项资金
    private String completeFund;
    //拨付专项资金
    private String paymentFund;
    //开工率
    private String operationRate;
    //完工率
    private String completeRate;
    //决算率
    private String jueSuanRate;
    //专项资金拨付率
    private String paymentFundRate;
    //专项资金完成率
    private String completeFundRate;
    //区县统计
    private List<StatisticsVO> cityDistrictList;
    //规划优化类别统计
    private List<StatisticsVO> planOptimizeTypeList;
    //分区域项目建设状态统计
    private List<StatisticsVO> scopeRegionList;
    //专项资金拨付率
    private List<StatisticsVO> paymentFundRateList;
    //专项资金完成率
    private List<StatisticsVO> completeFundRateList;
    //区县列表排名
    private List<StatisticsRankVO> cityDistrictRankList;
    //地图
    private List<StatisticsVO> mapList;

    public StatisticsInfoVO() {
        this.projectNum = 0;
        this.projectTotalInvest = "0";
        this.planFinalIssueFund = "0";
        this.completeFund = "0";
        this.paymentFund = "0";
        this.operationRate = "0";
        this.paymentFundRate = "0";
        this.completeRate = "0";
        this.completeFundRate = "0";
        this.jueSuanRate = "0";
        this.cityDistrictList = new ArrayList<>();
        this.planOptimizeTypeList = new ArrayList<>();
        this.scopeRegionList = new ArrayList<>();
        this.paymentFundRateList = new ArrayList<>();
        this.completeFundRateList = new ArrayList<>();
        this.cityDistrictRankList = new ArrayList<>();
        this.mapList = new ArrayList<>();
    }
}
