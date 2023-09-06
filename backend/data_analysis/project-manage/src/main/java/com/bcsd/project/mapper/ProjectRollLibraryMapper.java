package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectRollLibrary;
import com.bcsd.project.domain.vo.ProjectRollLibrarySummaryVO;
import com.bcsd.project.domain.vo.StatisticsQueryVO;
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
public interface ProjectRollLibraryMapper extends BaseMapper<ProjectRollLibrary> {

    List<ProjectRollLibrary> listPage(ProjectRollLibrary prl);

    List<Map> selectByPN(ProjectRollLibrary prl);

    Map statisticsNumAndFund(Long rollCycleId);

    List<ProjectRollLibrarySummaryVO> statisticsGbQx(StatisticsQueryVO params);

    List<Map> statisticsGb145(StatisticsQueryVO params);

    List<Map> statisticsGbType(StatisticsQueryVO params);

    List<Map> statisticsGbPlan(StatisticsQueryVO params);

    List<Map> statisticsGbBuildStatus(StatisticsQueryVO params);

}
