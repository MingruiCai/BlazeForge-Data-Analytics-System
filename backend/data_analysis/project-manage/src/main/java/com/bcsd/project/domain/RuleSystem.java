package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 规章制度表
 * @author liuliang
 * @since 2023-03-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("rule_system")
public class RuleSystem extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*文件名称*/
    @NotBlank(message = "文件名称不能为空")
    private String originalFileName;

    /*文件地址*/
    @NotBlank(message = "文件地址不能为空")
    private String url;

    /*数据列别 1-管理文件 2-成果文件*/
    @NotNull(message = "数据列别 1-管理文件 2-成果文件不能为空")
    private Integer dataType;

    /*文件类别*/
    private String fileType;

    /*发文日期*/
    private String issueDate;

    /*发文编号*/
    private String issueNo;

    /*发文单位*/
    private String issueUnit;

    /*文件类别名称*/
    @TableField(exist = false)
    private String fileTypeName;

}
