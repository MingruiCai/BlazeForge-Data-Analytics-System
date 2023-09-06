package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.project.domain.vo.FileVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 问题整改通知
 * @author liuliang
 * @since 2023-03-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("problem_rectify_notice")
public class ProblemRectifyNotice extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*通知名称*/
    @NotBlank(message = "通知名称不能为空！")
    private String noticeName;

    /*通知文号*/
    @NotBlank(message = "通知文号不能为空！")
    private String noticeNo;

    /*通知附件JSON*/
    @JsonIgnore
    private String noticeFile;

    /*通知附件List*/
    @NotEmpty(message = "通知附件不能为空！")
    @TableField(exist = false)
    private List<FileVO> noticeFileList;

    public List<FileVO> getNoticeFileList() {
        if (StringUtils.isNotBlank(noticeFile)){
            List<FileVO> list = JSON.parseArray(noticeFile,FileVO.class);
            return list;
        }
        return noticeFileList;
    }
}
