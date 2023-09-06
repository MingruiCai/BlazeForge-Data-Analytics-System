package com.bcsd.project.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProjectImplStatisticsVO {

    /*时间范围 1-当年 2-累计*/
    private Integer timeScope = 2;
    /*项目性质 1-新建 2-续建*/
    private Integer projectType;
    /*是否分年*/
    private Boolean isYearly = false;
    /**
     * 统计类型
     * 100 - 分县
     * 010 - 分类
     * 001 - 范围
     * 110 - 分县+分类
     * 101 - 分县+范围
     * 011 - 分类+范围
     * 111 - 分县+分类+范围
     */
    private String statisticsType = "100";
    /*计划年度集合*/
    private List<String> planYears;
    /*年*/
    private String year;
    /*月*/
    private String month;

    /**--------------过滤条件------------------*/
    //区县
    private List<String> districts;
    //分类
    private List<String> types;
    //范围
    private List<String> scopes;

}
