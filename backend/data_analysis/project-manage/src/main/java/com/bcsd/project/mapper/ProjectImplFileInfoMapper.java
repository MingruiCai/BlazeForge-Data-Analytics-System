package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProjectImplFileInfo;
import com.bcsd.project.domain.vo.ZipVO;
import com.bcsd.project.domain.vo.ProjectFileDownloadVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目实施附件 Mapper 接口
 *
 * @author liuliang
 * @since 2023-02-23
 */
@Repository
public interface ProjectImplFileInfoMapper extends BaseMapper<ProjectImplFileInfo> {

    List<ZipVO> selectByProjectNo(ProjectFileDownloadVO params);

}
