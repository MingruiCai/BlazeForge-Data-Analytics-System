package com.bcsd.common.core.domain.entity;

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
@TableName("sys_group")
public class SysGroup extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*分组名称*/
    @NotBlank(message = "分组名称不能为空")
    private String groupName;

    /*排序*/
    private Integer orderNum;

    /*用户ID*/
    private Long userId;

}
