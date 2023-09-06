package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 滚动周期表
 *
 * @author liuliang
 * @since 2023-02-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("achievement_province")
public class AchievementProvince extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /**年度*/
    private Integer year;
    /**转移支付名称*/
    private String transferPaymentName;
    /**中央主管部门*/
    private String departmentCentral;
    /**地方主管部门*/
    private String departmentLocal;
    /**资金使用单位*/
    private String departmentFundUser;
    /**预算资金总数*/
    private String budgetFundTotal;
    /**中央财政预算资金*/
    private String budgetFundCentral;
    /**地方财政预算资金*/
    private String budgetFundLocal;
    /**其他预算资金*/
    private String budgetFundOther;
    /**执行资金总数*/
    private String executeFundTotal;
    /**执行中央财政资金*/
    private String executeFundCentral;
    /**执行地方财政资金*/
    private String executeFundLocal;
    /**执行其他资金*/
    private String executeFundOther;
    /**总体目标*/
    private String goalOverall;
    /**总体目标实际完成情况*/
    private String goalExecute;
    @TableField(exist = false)
    private List<AchievementProvinceDetail> detailList;
}
