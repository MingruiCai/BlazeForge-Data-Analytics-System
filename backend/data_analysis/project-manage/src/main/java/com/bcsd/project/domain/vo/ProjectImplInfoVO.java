package com.bcsd.project.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.bcsd.project.domain.*;
import com.bcsd.project.domain.vo.ProjectImplFileTypeVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ProjectImplInfoVO {

    /**
     * 基本信息
     */
    private ProjectImplBasicInfo basicInfo;

    /**
     * 实施情况
     */
    private ProjectImplStateInfo implInfo;

    /**
     * 资金情况
     */
    private ProjectImplFundInfo fundsInfo;

    /**
     * 进展情况
     */
    private ProjectImplEvolve evolveInfo;

    /**
     * 附件资料
     */
    private List<ProjectImplFileTypeVO> filesInfo;

    /**
     * VR资料
     */
    private List<ProjectImplVrFileInfo> vrInfo;

    /**
     * CCTV
     */
    private List<ProjectImplCctv> cctvs;

    /**
     * 用户名称
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    /**
     * 角色标识
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> roleKeys;

    /**
     * 描述
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    /**
     * 类型 -2-草稿
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    /*操作记录*/
    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Map> logs;

}
