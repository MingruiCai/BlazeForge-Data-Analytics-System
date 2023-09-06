package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectPlanLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 计划审核记录 Mapper 接口
 *
 * @author liuliang
 * @since 2023-06-02
 */
@Repository
public interface ProjectPlanLogMapper extends BaseMapper<ProjectPlanLog> {

    List<Map> selectByPlanId(Long id);

}
