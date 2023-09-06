package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目进展情况
 * @author liuliang
 * @since 2023-05-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_evolve")
public class ProjectImplEvolve extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;
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

}
