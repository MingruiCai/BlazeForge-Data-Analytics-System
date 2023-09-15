package com.bcsd.project.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.annotation.Excel;
import com.bcsd.common.core.domain.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 零件库存阈值信息
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ly_inventory_threshold")
public class lyInventoryThreshold extends BaseInfo {
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
     * 库存上限数量
     */
    @Excel(name = "库存上限数量")
    @ApiModelProperty(value = "库存上限数量")
    @TableField("upperLimit")
    private Integer upperLimit;
    /**
     * 库存下限数量
     */
    @Excel(name = "库存下限数量")
    @ApiModelProperty(value = "库存下限数量")
    @TableField("lowerLimit")
    private Integer lowerLimit;

}
