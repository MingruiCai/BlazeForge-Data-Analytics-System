package com.bcsd.project.domain.vo;

import lombok.Data;

/**
 * 项目实施基本信息
 * @author liuliang
 * @since 2023-02-22
 */
@Data
public class ProjectEarlyWarningVO{

    private static final long serialVersionUID = 1L;

    /*省 */
    private String province;
    /*区县*/
    private String cityDistrict;
    /*项目编号*/
    private String projectNo;
    /*项目名称*/
    private String projectName;
    /*项目总投资（万元）*/
    private String projectTotalInvest;
    /*当前形象进度*/
    private String dangqianxingxiangjindu;
    /*目标形象进度*/
    private String mubiaoxingxiangjindu;
    /*偏差值*/
    private String deviation;
    //专项资金拨付率
    private String paymentFundRate;
    //专项资金拨付目标
    private String paymentFundTarget;
    //专项资金完成率
    private String completeFundRate;
    //专项资金完成目标
    private String completeFundTarget;





}
