package com.bcsd.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和分组关联 sys_group_user
 * 
 * @author bcsd
 */
@Data
@TableName("sys_group_user")
public class SysGroupUser {

    /** 分组ID */
    private Long groupId;
    /** 用户ID */
    private Long userId;

}
