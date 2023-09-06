package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.LedgerDistrictProgressLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 区县进展台账审核记录 Mapper 接口
 *
 * @author liuliang
 * @since 2023-04-12
 */
@Repository
public interface LedgerDistrictProgressLogMapper extends BaseMapper<LedgerDistrictProgressLog> {

    List<Map> selectByLDPId(Long id);

}
