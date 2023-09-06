package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 省市进展台账
 *
 * @author liuliang
 * @since 2023-04-13
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ledger_province_progress")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LedgerProvinceProgress extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*部门ID*/
    private Long deptId;

    /*数据年*/
    @NotNull(message = "年度不能为空！")
    private Integer dataYear;

    /*数据周*/
    @NotNull(message = "周不能为空！")
    private Integer dataWeek;

    /*汇总区县列表*/
    @TableField(exist = false)
    List<LedgerDistrictProjectProgress> lpdpList;
}
