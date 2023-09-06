package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.domain.vo.InformationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目实施版本记录 Mapper 接口
 *
 * @author liuliang
 * @since 2023-02-27
 */
@Repository
public interface ProjectImplVersionMapper extends BaseMapper<ProjectImplVersion> {

    List<ProjectImplVersion> selectNewData();

    ProjectImplVersion selectNewOne(String projectNo);

    List<ProjectImplVersion> selectGPData();

    List<ProjectImplVersion> selectNewDataByBuildStatus();

    List<ProjectImplVersion> selectDataByYM(InformationVO params);

    List<ProjectImplVersion> selectNewDataByBuildStatusAndYear(String year);

    List<ProjectImplVersion> selectNewDataByPlanYearAndDeptId(@Param("planYear") String planYear,@Param("deptId") Long deptId);

    List<ProjectImplVersion> selectDataByYearAndCity(InformationVO params);

    List<ProjectImplVersion> listPage(ProjectImplVersion params);
}
