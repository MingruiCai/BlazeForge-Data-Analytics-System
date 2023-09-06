package com.bcsd.project.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuliang
 * @since 2023-02-16
 */
@Getter
@Setter
@EqualsAndHashCode
public class ArchiveProjectVO {

    /* 序号 */
    @ExcelProperty(index = 0)
    private String no;
    /*单位名称*/
    @ExcelProperty(index = 1)
    private String unitName;
    /* 项目名称 */
    @ExcelProperty(index = 2)
    private String projectName;
    /* 项目编号 */
    @ExcelProperty(index = 3)
    private String projectNo;
    /*开始年份 */
    @ExcelProperty(index = 4)
    private String beginYear;
    /*结束年份 */
    @ExcelProperty(index = 5)
    private String endYear;
    /*纬度 */
    @ExcelProperty(index = 6)
    private String latitude;
    /*经度*/
    @ExcelProperty(index = 7)
    private String longitude;
    /*规划类别 */
    @ExcelProperty(index = 8)
    private String type1;
    @ExcelProperty(index = 9)
    private String type2;
    /*项目法人或责任单位*/
    @ExcelProperty(index = 10)
    private String projectLegalPerson;
    /*批复主要建设内容*/
    @ExcelProperty(index = 11)
    private String overallConstructionScale;
    /*项目总投资（万元） */
    @ExcelProperty(index = 12)
    private String projectTotalInvest;
    /*省市分年下达专项资金情况（万元）*/
    @ExcelProperty(index = 13)
    private String fund2011;
    @ExcelProperty(index = 14)
    private String fund2012;
    @ExcelProperty(index = 15)
    private String fund2013;
    @ExcelProperty(index = 16)
    private String fund2014;
    @ExcelProperty(index = 17)
    private String fund2015;
    @ExcelProperty(index = 18)
    private String fund2016;
    @ExcelProperty(index = 19)
    private String fund2017;
    @ExcelProperty(index = 20)
    private String fund2018;
    @ExcelProperty(index = 21)
    private String fund2019;
    @ExcelProperty(index = 22)
    private String fund2020;
    @ExcelProperty(index = 23)
    private String fund2021;
    @ExcelProperty(index = 24)
    private String fund2022;
    @ExcelProperty(index = 25)
    private String fund2023;
    /*实施状态（未开工、在建、完建、验收）*/
    @ExcelProperty(index = 26)
    private String status;
    /*未开工项目前期工作状态*/
    @ExcelProperty(index = 27)
    private String prophaseWorkExtent;
    /*开工时间*/
    @ExcelProperty(index = 28)
    private String swSwd;
    /*完工时间*/
    @ExcelProperty(index = 29)
    private String ccCd;
    /*竣工验收时间*/
    @ExcelProperty(index = 30)
    private String caAd;
    /*投资完成-总投资（万元）*/
    @ExcelProperty(index = 31)
    private String completeTotalFund;
    /*其中三峡后续专项资金（万元）*/
    @ExcelProperty(index = 32)
    private String completeFund;
    /*财政拨付三峡后续专项资金（万元）*/
    @ExcelProperty(index = 33)
    private String paymentFund;
    /*实际支付三峡后续专项资金（万元）*/
    @ExcelProperty(index = 34)
    private String useFund;
    /*决算批复时间*/
    @ExcelProperty(index = 35)
    private String cfsFaad;
    /*决算批复文号*/
    @ExcelProperty(index = 36)
    private String cfsFaano;
    /*决算金额-总投资（万元）*/
    @ExcelProperty(index = 37)
    private String cfsFaa;
    /*决算金额-其中三峡后续专项资金（万元）*/
    @ExcelProperty(index = 38)
    private String cfsFaaSf;
    /*累计吸纳就业人数(人)*/
    @ExcelProperty(index = 39)
    private String employPersonTotal;
    /*其中：农村劳动力就业人数(人)*/
    @ExcelProperty(index = 40)
    private String employPersonVillageTotal;
    /*受益村、社区*/
    @ExcelProperty(index = 41)
    private String benefitVillage;
    /*受益人数*/
    @ExcelProperty(index = 42)
    private String benefitNumberOfPeople;
    /*受益三峡移民人数*/
    @ExcelProperty(index = 43)
    private String benefitSxNumberOfPeople;
    /*完成主要实物量*/
    @ExcelProperty(index = 44)
    private String completeMainNumber;

}
