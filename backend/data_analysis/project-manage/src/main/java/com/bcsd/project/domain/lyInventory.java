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
 * 库存
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ly_inventory")
public class lyInventory extends BaseInfo {

    @TableField("podTypCode")
    private String podTypCode;

    @TableField("dateLastInv")
    private String dateLastInv;

    @Excel(name = "总计数量")
    @ApiModelProperty(value = "总计数量")
    @TableField("totalQty")
    private Double totalQty;

    @Excel(name = "最近入库时间",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "最近入库时间")
    @TableField("dateLastRm")
    private String dateLastRm;

    @Excel(name = "最近转储单编号")
    @ApiModelProperty(value = "最近转储单编号")
    @TableField("toNumLastMov")
    private String toNumLastMov;

    @TableField("blkReaText")
    private String blkReaText;

    @TableField("operType")
    private String operType;

    @TableField("batchAttr08")
    private String batchAttr08;

    @Excel(name = "零件状态")
    @ApiModelProperty(value = "零件状态")
    @TableField("batchAttr07")
    private String batchAttr07;

    @Excel(name = "货架编号")
    @ApiModelProperty(value = "货架编号")
    @TableField("podCode")
    private String podCode;

    @TableField("batchAttr09")
    private String batchAttr09;

    @TableField("qualityStatus")
    private String qualityStatus;

    @Excel(name = "可用库存")
    @ApiModelProperty(value = "可用库存")
    @TableField("availableQty")
    private Double availableQty;

    @TableField("containerTypeCode")
    private String containerTypeCode;

    @TableField("inStockInterval")
    private String inStockInterval;

    @Excel(name = "物料单位")
    @ApiModelProperty(value = "物料单位")
    @TableField("matUnit")
    private String matUnit;

    @TableField("boxFlag")
    private String boxFlag;

    @TableField("boxCode")
    private String boxCode;

    @TableField("boxFlagText")
    private String boxFlagText;

    @TableField("outBlkUserFlag")
    private String outBlkUserFlag;

    @TableField("stgTypText")
    private String stgTypText;

    @TableField("stgBinTypCode")
    private String stgBinTypCode;

    @TableField("batchAttr11")
    private String batchAttr11;

    @TableField("batchAttr10")
    private String batchAttr10;

    private String priority;

    @TableField("batchAttr15")
    private String batchAttr15;

    @TableField("batchAttr14")
    private String batchAttr14;

    @TableField("pickNum")
    private String pickNum;

    @TableField("batchAttr13")
    private String batchAttr13;

    @TableField("batchAttr12")
    private String batchAttr12;

    @TableField("desBinCode")
    private String desBinCode;

    @Excel(name = "仓位别名")
    @ApiModelProperty(value = "仓位别名")
    @TableField("binName")
    private String binName;

    @TableField("weiUnit")
    private String weiUnit;

    private String sequence;

    @TableField("realAvailableQty")
    private String realAvailableQty;

    @TableField("whCode")
    private String whCode;

    @TableField("blkUser")
    private String blkUser;

    @Excel(name = "最近出库时间",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "最近出库时间")
    @TableField("dateLastPm")
    private Date dateLastPm;

    @Excel(name = "储位别名")
    @ApiModelProperty(value = "储位别名")
    @TableField("berthAlias")
    private String berthAlias;

    @Excel(name = "盘点标识")
    @ApiModelProperty(value = "盘点标识")
    @TableField("invFlagText")
    private String invFlagText;

    @Excel(name = "零件颜色")
    @ApiModelProperty(value = "零件颜色")
    @TableField("matText")
    private String matText;

    @Excel(name = "最近转储项目")
    @ApiModelProperty(value = "最近转储项目")
    @TableField("toItemLastMov")
    private String toItemLastMov;

    @TableField("invFlag")
    private String invFlag;

    @TableField("stockStr1")
    private String stockStr1;

    @TableField("intoBlkUserFlagText")
    private String intoBlkUserFlagText;

    @TableField("stockStr2")
    private String stockStr2;

    @Excel(name = "生产日期",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "生产日期")
    @TableField("dateGen")
    private Date dateGen;

    @Excel(name = "货主")
    @ApiModelProperty(value = "货主")
    @TableField("ownerCode")
    private String ownerCode;

    @TableField("outStockInterval")
    private String outStockInterval;

    @TableField("podTypText")
    private String podTypText;

    @TableField("matAdjustFlag")
    private String matAdjustFlag;

    @TableField("stockStr3")
    private String stockStr3;

    @Excel(name = "理货标识")
    @ApiModelProperty(value = "理货标识")
    @TableField("matAdjustFlagText")
    private String matAdjustFlagText;

    @TableField("stockStr4")
    private String stockStr4;

    @TableField("stockStr5")
    private String stockStr5;

    private String layer;

    @TableField("adjustQty")
    private Double adjustQty;

    @TableField("stockStr6")
    private String stockStr6;

    @TableField("blkReaCode")
    private String blkReaCode;

    @TableField("packFormat")
    private String packFormat;

    @TableField("ownerName")
    private String ownerName;

    @Excel(name = "库存编号")
    @ApiModelProperty(value = "库存编号")
    @TableField("stkCode")
    private String stkCode;

    @TableField("matWei")
    private String matWei;

    @Excel(name = "质检状态")
    @ApiModelProperty(value = "质检状态")
    @TableField("qualityStatusText")
    private String qualityStatusText;

    @Excel(name = "iWMS批次")
    @ApiModelProperty(value = "iWMS批次")
    @TableField("wmsBatchNum")
    private String wmsBatchNum;

    @Excel(name = "地图")
    @ApiModelProperty(value = "地图")
    @TableField("mapName")
    private String mapName;

    private String direction;

    @TableField("channelCode")
    private String channelCode;

    @TableField("directionTex")
    private String directionTex;

    @Excel(name = "失效日期",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "失效日期")
    @TableField("dateExpire")
    private Date dateExpire;

    @Excel(name = "仓位编号")
    @ApiModelProperty(value = "仓位编号")
    @TableField("binCode")
    private String binCode;

    @TableField("batchNum")
    private String batchNum;

    @Excel(name = "是否出库冻结")
    @ApiModelProperty(value = "是否出库冻结")
    @TableField("outBlkUserFlagText")
    private String outBlkUserFlagText;

    @Excel(name = "仓库")
    @ApiModelProperty(value = "仓库")
    @TableField("whText")
    private String whText;

    @TableField("binUtilization")
    private String binUtilization;

    @TableField("traceCode")
    private String traceCode;

    @TableField("seqCode")
    private String seqCode;

    @Excel(name = "过期标识")
    @ApiModelProperty(value = "过期标识")
    @TableField("dateExpireFlag")
    private String dateExpireFlag;

    @TableField("blkReaType")
    private String blkReaType;

    @Excel(name = "入库日期",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "入库日期")
    @TableField("dateInto")
    private Date dateInto;

    @Excel(name = "库区编号")
    @ApiModelProperty(value = "库区编号")
    @TableField("stgTypCode")
    private String stgTypCode;

    @Excel(name = "最近转储时间",  dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.IMPORT)
    @ApiModelProperty(value = "最近转储时间")
    @TableField("dateLastMov")
    private Date dateLastMov;

    @Excel(name = "零件号")
    @ApiModelProperty(value = "零件号")
    @TableField("matCode")
    private String matCode;

    @TableField("hotIndex")
    private String hotIndex;

    @TableField("containerTypeName")
    private String containerTypeName;

    @ApiModelProperty(value = "库存状态（0：正常，1：低下限告警，2：超上限告警）")
    @TableField("inventoryStatus")
    private Integer inventoryStatus;

    @ApiModelProperty(value = "缺口处理状态（0：无需处理，1：未处理，2：已处理）")
    @TableField("processingStatus")
    private Integer processingStatus;

    @TableField(exist = false)
    private List<String> batchAttr07s;

    @TableField(exist = false)
    private String date;
}