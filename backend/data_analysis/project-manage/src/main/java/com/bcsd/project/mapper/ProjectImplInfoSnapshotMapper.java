package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectImplInfoSnapshot;
import com.bcsd.project.domain.vo.ProjectImplStatisticsVO;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.domain.vo.InformationVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目实施信息快照 Mapper 接口
 *
 * @author liuliang
 * @since 2023-02-28
 */
@Repository
public interface ProjectImplInfoSnapshotMapper extends BaseMapper<ProjectImplInfoSnapshot> {

    /**
     * 根据年份获取数据
     * @param params
     * @return
     */
    List<ProjectImplVersion> getDataByYear(ProjectImplStatisticsVO params);

    /**
     * 获取数据
     * @param params
     * @return
     */
    List<ProjectImplVersion> selectData(InformationVO params);

}
