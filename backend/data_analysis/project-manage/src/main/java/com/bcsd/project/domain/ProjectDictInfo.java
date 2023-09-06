package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("project_dict_info")
public class ProjectDictInfo {

    /**
     * ID
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父ID
     */
    private String pid;

    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String pName;


}
