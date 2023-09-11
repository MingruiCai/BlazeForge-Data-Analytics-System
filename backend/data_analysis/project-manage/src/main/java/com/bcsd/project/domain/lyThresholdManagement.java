package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 阈值管理信息
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ly_threshold_management")
public class lyThresholdManagement extends BaseInfo {
    /**
     * 阈值类型
     */
    private String type;
    /**
     * 阈值名称
     */
    private String name;
    /**
     * 阈值编码
     */
    private String code;
    /**
     * 状态
     * 0表示停用，1表示启用
     */
    private String status;
    /**
     * 上限值
     */
    private Integer upperLimit;
    /**
     * 下限值
     */
    private Integer lowerLimit;
}
