package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.annotation.Excel;
import com.bcsd.common.core.domain.BaseInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 空容器
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ly_empty_container")
public class lyEmptyContainer extends BaseInfo {

    @ApiModelProperty(value = "货架类型编号")
    @TableField("podTypCode")
    private String podTypCode;

    @TableField("podStr8")
    private String podStr8;

    @Excel(name = "入库冻结(用户)")
    @ApiModelProperty(value = "入库冻结(用户)")
    @TableField("intoBlkUserFlagText")
    private String intoBlkUserFlagText;

    @TableField("agvFlag")
    private String agvFlag;

    @TableField("emptyPodFlag")
    private String emptyPodFlag;

    @TableField("podStr4")
    private String podStr4;

    @TableField("podStr5")
    private String podStr5;

    @TableField("podStr6")
    private String podStr6;

    @TableField("podStr7")
    private String podStr7;

    @Excel(name = "货架类型名称")
    @ApiModelProperty(value = "货架类型名称")
    @TableField("podTypText")
    private String podTypText;

    @TableField("podStr1")
    private String podStr1;

    @TableField("blkReaText")
    private String blkReaText;

    @TableField("initStatus")
    private String initStatus;

    @TableField("podStr2")
    private String podStr2;

    @Excel(name = "储位编号")
    @ApiModelProperty(value = "储位编号")
    @TableField("berthCode")
    private String berthCode;

    @TableField("podStr3")
    private String podStr3;

    @TableField("podLayout")
    private String podLayout;

    private String layer;

    @TableField("blkReaCode")
    private String blkReaCode;

    @Excel(name = "货架编号")
    @ApiModelProperty(value = "货架编号")
    @TableField("podCode")
    private String podCode;

    @TableField("cooY")
    private Double cooY;

    @TableField("cooX")
    private Double cooX;

    @TableField("mapName")
    private String mapName;

    @ApiModelProperty(value = "货架名称")
    @TableField("podText")
    private String podText;

    @TableField("distanceWb")
    private Double distanceWb;

    @Excel(name = "AGV存储")
    @ApiModelProperty(value = "AGV存储")
    @TableField("agvFlagText")
    private String agvFlagText;

    @TableField("stgTypText")
    private String stgTypText;

    @ApiModelProperty(value = "出库冻结状态（状态码）")
    @TableField("outBlkUserFlag")
    private String outBlkUserFlag;

    private String middle;

    @TableField("batchAttrValue")
    private String batchAttrValue;

    @TableField("podIntoTempLockText")
    private String podIntoTempLockText;

    @Excel(name = "出库冻结(用户)")
    @ApiModelProperty(value = "出库冻结(用户)")
    @TableField("outBlkUserFlagText")
    private String outBlkUserFlagText;

    @TableField("emptyBinCount")
    private Integer emptyBinCount;

    @Excel(name = "仓库")
    @ApiModelProperty(value = "仓库")
    @TableField("whText")
    private String whText;

    @Excel(name = "空仓位数/总仓位数")
    @ApiModelProperty(value = "空仓位数/总仓位数")
    @TableField("binUtilization")
    private String binUtilization;

    @Excel(name = "初始化状态")
    @ApiModelProperty(value = "初始化状态")
    @TableField("initStatusText")
    private String initStatusText;

    @TableField("whCode")
    private String whCode;

    @ApiModelProperty(value = "入库冻结状态（状态码）")
    @TableField("intoBlkUserFlag")
    private String intoBlkUserFlag;

    @Excel(name = "冻结人")
    @ApiModelProperty(value = "冻结人")
    @TableField("blkUser")
    private String blkUser;

    @TableField("berthAlias")
    private String berthAlias;

    @TableField("podIntoTempLock")
    private String podIntoTempLock;

    @Excel(name = "库区编号")
    @ApiModelProperty(value = "库区编号")
    @TableField("stgTypCode")
    private String stgTypCode;

    @Excel(name = "冷热度指数")
    @ApiModelProperty(value = "冷热度指数")
    @TableField("hotIndex")
    private String hotIndex;

    @TableField("mapCode")
    private String mapCode;

    @TableField("podWei")
    private String podWei;

    @TableField(exist = false)
    private List<String> podCodes;
}