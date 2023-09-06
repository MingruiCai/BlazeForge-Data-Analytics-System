package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 区县进展台账审核记录
 * @author liuliang
 * @since 2023-04-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ledger_district_progress_log")
public class LedgerDistrictProgressLog extends BaseInfo {
    
    private static final long serialVersionUID = 1L;

    /*区县进展台账ID*/
    private Long ledgerDistrictProgressId;

    /*描述*/
    private String description;

    /*步骤*/
    private String step;

    /*类型*/
    private Integer type;

}
