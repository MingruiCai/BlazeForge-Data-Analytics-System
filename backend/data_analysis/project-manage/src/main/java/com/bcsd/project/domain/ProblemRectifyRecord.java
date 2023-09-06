package com.bcsd.project.domain;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import com.bcsd.project.domain.vo.FileVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * 问题整改记录
 * @author liuliang
 * @since 2023-03-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("problem_rectify_record")
public class ProblemRectifyRecord extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*整改通知ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long noticeId;

    /*监测预警ID*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long earlyWarningId;

    /** 部门ID */
    private Long deptId;

    @NotBlank(message = "项目编号不能为空")
    /*项目编号*/
    private String projectNo;

    /*项目名称*/
    private String projectName;

    /*通知附件JSON*/
    @JsonIgnore
    private String noticeSubFile;

    /*通知附件List*/
    @NotEmpty(message = "通知附件不能为空！")
    @TableField(exist = false)
    private List<FileVO> noticeSubFileList;

    public List<FileVO> getNoticeSubFileList() {
        if (StringUtils.isNotBlank(noticeSubFile)){
            List<FileVO> list = JSON.parseArray(noticeSubFile,FileVO.class);
            return list;
        }
        return noticeSubFileList;
    }

    /*监测时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date monitorTime;

    /*问题来源*/
    private String problemSource;

    /*问题*/
    private String problemContent;

    /*问题类型*/
    private String problemType;

    /*问题等级*/
    private String problemLevel;

    /*整改期限*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rectifyDeadline;

    /*整改时间*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rectifyTime;

    /*整改状态（落实情况）*/
    private String rectifyStatus;

    /*整改证明文件*/
    @JsonIgnore
    private String rectifyCertificate;

    /*通整改证明文件List*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FileVO> rectifyCertificateList;

    public List<FileVO> getRectifyCertificateList() {
        if (StringUtils.isNotBlank(rectifyCertificate)){
            List<FileVO> list = JSON.parseArray(rectifyCertificate,FileVO.class);
            return list;
        }
        return rectifyCertificateList;
    }

    /* 用户ID */
    private Long userId;

    /* 知会人 */
    private String inform;

    /*用户名*/
    @TableField(exist = false)
    private String userName;

    /* 知会人名称 */
    @TableField(exist = false)
    private String informName;

    /*通知名称*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String noticeName;

    /*通知附件JSON*/
    @JsonIgnore
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String noticeFile;

    /*通知附件List*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FileVO> noticeFileList;

    public List<FileVO> getNoticeFileList() {
        if (StringUtils.isNotBlank(noticeFile)){
            List<FileVO> list = JSON.parseArray(noticeFile,FileVO.class);
            return list;
        }
        return noticeFileList;
    }

    /*监测时间区间*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> monitorTimeList;
    /*整改期限区间*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> rectifyDeadlineList;
    /*整改时间区间*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> rectifyTimeList;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String monitorTimeBegin;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String monitorTimeEnd;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rectifyDeadlineBegin;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rectifyDeadlineEnd;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rectifyTimeBegin;
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rectifyTimeEnd;


}
