package com.bcsd.project.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class BindUserVO {

    /**
     * ID列表
     */
    @NotEmpty( message = "ID不能为空")
    private List<Long> ids;

    /**
     * 用户ID
     */
    @NotEmpty(message = "用户ID不能为空")
    private List<Long> userIds;

}
