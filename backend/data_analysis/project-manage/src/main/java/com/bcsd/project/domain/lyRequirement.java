package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 零件库存阈值信息
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ly_inventory_threshold")
public class lyRequirement extends BaseInfo {
    /**
     * 零件号
     */
    private String code;
    /**
     * 零件颜色
     */
    private String color;
    /**
     * 计划日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    /**
     * 计划发货数量
     */
    private Integer quantity;

}
