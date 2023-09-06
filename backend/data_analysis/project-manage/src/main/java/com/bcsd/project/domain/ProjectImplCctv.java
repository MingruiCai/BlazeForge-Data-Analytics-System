package com.bcsd.project.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bcsd.common.core.domain.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 项目实施CCTV
 *
 * @author liuliang
 * @since 2023-02-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("project_impl_cctv")
public class ProjectImplCctv extends BaseInfo {

    private static final long serialVersionUID = 1L;

    /*项目编号*/
    private String projectNo;

    /*标题*/
    private String title;

    /*URL*/
    private String url;

}
