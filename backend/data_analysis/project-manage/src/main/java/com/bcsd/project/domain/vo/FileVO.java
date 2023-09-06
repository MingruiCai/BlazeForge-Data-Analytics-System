package com.bcsd.project.domain.vo;

import lombok.Data;

/**
 * 文件VO类
 *
 * @author liuliang
 * @since 2023-02-23
 */
@Data
public class FileVO{

    /*文件名*/
    private String originalFileName;

    /*文件URL*/
    private String url;

    /*文件大小*/
    private String fileSize;

    /*文件新名称*/
    private String newFileName;

    /*创建人*/
    private String createBy;

    /*创建时间*/
    private String createTime;

}
