package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.ProblemRectifyRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问题整改记录 Mapper 接口
 *
 * @author liuliang
 * @since 2023-03-20
 */
@Repository
public interface ProblemRectifyRecordMapper extends BaseMapper<ProblemRectifyRecord> {

    List<ProblemRectifyRecord> listPage(ProblemRectifyRecord params);

    ProblemRectifyRecord getById(Long id);

}
