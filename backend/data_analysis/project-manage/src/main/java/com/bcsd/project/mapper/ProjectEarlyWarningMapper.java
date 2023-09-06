package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectEarlyWarning;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 项目预警 Mapper 接口
 *
 * @author liuliang
 * @since 2023-05-04
 */
@Repository
public interface ProjectEarlyWarningMapper extends BaseMapper<ProjectEarlyWarning> {

    Map statistics(ProjectEarlyWarning params);

}
