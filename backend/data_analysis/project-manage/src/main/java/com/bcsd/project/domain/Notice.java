package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.project.domain.vo.FileVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 通知公告表
 *
 * @author liuliang
 * @since 2023-05-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("notice")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notice extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*公告标题*/
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /*公告内容*/
    private String content;

    /*公告附件*/
    @JsonIgnore
    private String fileJson;

    /*部门ID */
    @JsonIgnore
    private Long deptId;

    /*状态 0-暂存 1-已发布*/
    @NotNull(message = "通知状态不能为空")
    private Integer status;

    /*角色标识 */
    @JsonIgnore
    private String roleKey;

    /*用户ID*/
    @JsonIgnore
    private Long userId;

    @NotEmpty(message = "通知用户不能为空！")
    @TableField(exist = false)
    private List<Long> userIds;

    @TableField(exist = false)
    private List<FileVO> files;

    public List<FileVO> getFiles() {
        if (StringUtils.isNotBlank(fileJson)){
            return JSON.parseArray(fileJson,FileVO.class);
        }
        return files;
    }
    /*已读数量*/
    @TableField(exist = false)
    private Integer num;
    /*总数量*/
    @TableField(exist = false)
    private Integer total;
    /*是否已读 0-未读 1-已读*/
    @TableField(exist = false)
    private Integer state;
    @TableField(exist = false)
    private String msgId;
    @TableField(exist = false)
    private List<Map> userList;
}
