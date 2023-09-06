package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectImplProcessLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 项目实施过程记录 Mapper 接口
 * @author liuliang
 * @since 2023-02-27
 */
@Repository
public interface ProjectImplProcessLogMapper extends BaseMapper<ProjectImplProcessLog> {

    List<Map> selectByProjectNo(String projectNo);

}
