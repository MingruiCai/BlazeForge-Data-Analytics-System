package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 区县进展台账
 * @author liuliang
 * @since 2023-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ledger_district_progress")
public class LedgerDistrictProgress extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*部门ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /*数据年*/
    @NotNull(message = "年不能为空")
    private Integer dataYear;

    /*数据周*/
    @NotNull(message = "月不能为空")
    private Integer dataWeek;

    /*区县编号*/
    private String cityDistrict;

    /*状态*/
    private String state;

    /*用户ID*/
    private Long userId;

    /*项目进度列表*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<LedgerDistrictProjectProgress> projectProgressList;

    /*操作记录*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Map> logs;

}
