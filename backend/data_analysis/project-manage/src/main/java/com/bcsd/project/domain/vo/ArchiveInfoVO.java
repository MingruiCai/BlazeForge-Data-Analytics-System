package com.bcsd.project.domain.vo;

import com.bcsd.project.domain.ProjectImplFundInfo;
import com.bcsd.project.domain.ProjectImplVersion;
import lombok.Data;

import java.util.List;

@Data
public class ArchiveInfoVO {

    /**
     * 基础信息
     */
    private ProjectImplVersion basicInfo;

    /**
     * 资金情况
     */
    private ProjectImplFundInfo fundsInfo;

    /**
     * 附件资料
     */
    private List<ProjectImplFileTypeVO> filesInfo;

}
