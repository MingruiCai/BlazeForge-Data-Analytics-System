package com.bcsd.project.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.annotation.Excel;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 需求计划设置信息
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ly_requirement")
public class lyRequirement extends BaseInfo {
    /**
     * 零件号
     */
    @Excel(name = "零件号")
    @ApiModelProperty(value = "零件号")
    @TableField("code")
    private String code;
    /**
     * 零件颜色
     */
    @Excel(name = "零件颜色")
    @ApiModelProperty(value = "零件颜色")
    @TableField("color")
    private String color;
    /**
     * 计划日期
     */
    @Excel(name = "计划日期",  dateFormat = "yyyy-MM-dd", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "计划日期")
    @TableField("date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    /**
     * 计划发货数量
     */
    @Excel(name = "计划发货数量")
    @ApiModelProperty(value = "计划发货数量")
    @TableField("quantity")
    private Integer quantity;

    /**
     * 计划发货数量
     */
    @Excel(name = "缺口处理状态")
    @ApiModelProperty(value = "缺口处理状态（0：无需处理，1：未处理，2：已处理）")
    @TableField("processingStatus")
    private Integer processingStatus;

}
