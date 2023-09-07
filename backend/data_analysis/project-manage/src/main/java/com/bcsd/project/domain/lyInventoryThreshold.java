package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
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
     * 零件颜色
     */
    private String color;
    /**
     * 库存上限数量
     */
    private Integer upperLimit;
    /**
     * 库存下限数量
     */
    private Integer lowerLimit;

}
