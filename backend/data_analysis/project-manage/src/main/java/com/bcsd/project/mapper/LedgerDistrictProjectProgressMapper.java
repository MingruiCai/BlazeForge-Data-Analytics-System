package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.LedgerDistrictProjectProgress;
import com.bcsd.project.domain.LedgerProvinceProgress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目进展台账 Mapper 接口
 *
 * @author liuliang
 * @since 2023-04-12
 */
@Repository
public interface LedgerDistrictProjectProgressMapper extends BaseMapper<LedgerDistrictProjectProgress> {

    /**
     * 根据时间查询
     * @param params
     * @return
     */
    List<LedgerDistrictProjectProgress> selectByDateTime(LedgerProvinceProgress params);

}
