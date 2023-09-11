package com.bcsd.project.domain.vo;

import lombok.Data;

/**
 * 空容器
 */
@Data
public class InventoryGapsNumberVO {

    private String id;
    private String matText;

    private String matCode;

    private Integer totalQty;

    private Integer processingStatus;

    private String date;

    private Integer quantity;

    private Integer gapNumber;


}