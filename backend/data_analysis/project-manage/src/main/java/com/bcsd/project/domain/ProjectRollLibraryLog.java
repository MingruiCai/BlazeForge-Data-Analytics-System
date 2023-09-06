package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("project_roll_library_log")
public class ProjectRollLibraryLog extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*滚动项目库ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long rollLibraryId;

    /*描述*/
    private String description;

    /*步骤*/
    private String step;

    /*类型*/
    private Integer type;

}
