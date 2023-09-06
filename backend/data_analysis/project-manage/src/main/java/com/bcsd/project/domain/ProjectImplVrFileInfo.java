package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施VR附件
 *
 * @author liuliang
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_vr_file_info")
public class ProjectImplVrFileInfo extends BaseInfo {
    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;

    /*文件名*/
    private String originalFileName;

    /*文件URL*/
    private String url;

    /*文件大小*/
    private String fileSize;
    

}
