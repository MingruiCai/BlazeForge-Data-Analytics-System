package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectRollLibraryLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 滚动项目库 Mapper 接口
 *
 * @author liuliang
 * @since 2023-02-16
 */
@Repository
public interface ProjectRollLibraryLogMapper extends BaseMapper<ProjectRollLibraryLog> {

    List<Map> selectByRollLibraryId(Long id);

}
