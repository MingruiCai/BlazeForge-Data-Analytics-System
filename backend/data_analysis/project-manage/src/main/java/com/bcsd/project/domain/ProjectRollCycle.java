package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.project.domain.vo.FileVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 滚动周期表
 * @author liuliang
 * @since 2023-02-16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_roll_cycle")
public class ProjectRollCycle extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*开始年份*/
    @NotNull(message = "开始年份不能为空")
    private Integer beginYear;

    /*结束年份*/
    @NotNull(message = "结束年份不能为空")
    private Integer endYear;

    /*文件信息*/
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fileInfo;

    /*文件信息*/
    @TableField(exist = false)
    private List<FileVO> fileList;

    public List<FileVO> getFileList() {
        if (StringUtils.isNotBlank(fileInfo)){
            List<FileVO> list = JSON.parseArray(fileInfo,FileVO.class);
            return list;
        }
        return fileList;
    }
}
