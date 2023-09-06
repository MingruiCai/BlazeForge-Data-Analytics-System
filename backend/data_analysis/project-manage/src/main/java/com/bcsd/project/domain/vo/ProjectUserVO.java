package com.bcsd.project.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProjectUserVO {

    /**
     * 项目ID列表
     */
    @NotEmpty( message = "项目ID不能为空")
    private List<Long> projectIds;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}
