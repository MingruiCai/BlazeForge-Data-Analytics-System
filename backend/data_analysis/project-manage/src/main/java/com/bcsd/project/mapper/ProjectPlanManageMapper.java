package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectPlanManage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 计划管理 Mapper 接口
 * @author liuliang
 * @since 2023-02-20
 */
@Repository
public interface ProjectPlanManageMapper extends BaseMapper<ProjectPlanManage> {

    /**
     * 分页列表
     * @param ppm
     * @return
     */
    List<ProjectPlanManage> listPage(ProjectPlanManage ppm);

    /**
     * 按145类别和计划年度查询规划资金
     * @return
     */
    List<Map> getFundGb145AndYear();

    /**
     * 按库区、区县和计划年度查询规划资金
     * @return
     */
    List<Map> getFundGbQXAndYear();

    /**
     * 根据区县和计划年度查询
     * @param params
     * @return
     */
    List<ProjectPlanManage> selectByDeptIdAndPlanYear(ProjectPlanManage params);

}
