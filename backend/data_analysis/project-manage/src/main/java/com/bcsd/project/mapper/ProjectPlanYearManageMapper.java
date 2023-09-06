package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectPlanYearManage;
import org.springframework.stereotype.Repository;

/**
 * 计划年度表 Mapper 接口
 * @author liuliang
 * @since 2023-03-20
 */
@Repository
public interface ProjectPlanYearManageMapper extends BaseMapper<ProjectPlanYearManage> {

    Integer getMaxPlanYear();

}
