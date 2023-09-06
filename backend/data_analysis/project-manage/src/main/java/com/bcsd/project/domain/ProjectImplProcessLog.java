package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施过程记录
 * @author liuliang
 * @since 2023-02-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_process_log")
public class ProjectImplProcessLog extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;

    /*描述*/
    private String description;

    /*类型 1-提交 2-驳回 3-通过*/
    private Integer type;

    /*步骤*/
    private String step;

}
