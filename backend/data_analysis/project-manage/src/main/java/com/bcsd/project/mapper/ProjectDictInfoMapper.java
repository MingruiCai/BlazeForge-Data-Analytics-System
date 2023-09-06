package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectDictInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目字典 Mapper 接口
 * @author liuliang
 * @since 2023-02-20
 */
@Repository
public interface ProjectDictInfoMapper extends BaseMapper<ProjectDictInfo> {

    List<ProjectDictInfo> selectByPid(String pid);

    String get145TypeByName(List<String> list);

    String getTypeByName(List<String> list);

}
