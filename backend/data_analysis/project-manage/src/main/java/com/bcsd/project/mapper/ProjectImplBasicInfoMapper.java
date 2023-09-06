package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectImplBasicInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目实施基本信息 Mapper 接口
 * @author liuliang
 * @since 2023-02-22
 */
@Repository
public interface ProjectImplBasicInfoMapper extends BaseMapper<ProjectImplBasicInfo> {

    List<ProjectImplBasicInfo> listPage(ProjectImplBasicInfo pibi);

    int selectUnauditedCount(String year);

    List<ProjectImplBasicInfo> selectByYear(String year);

    List<Long> selectUserId(String year);

}
