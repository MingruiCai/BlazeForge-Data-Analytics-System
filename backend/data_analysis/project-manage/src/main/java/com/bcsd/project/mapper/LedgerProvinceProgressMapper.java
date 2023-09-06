package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.LedgerProvinceProgress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 省市进展台账 Mapper 接口
 *
 * @author liuliang
 * @since 2023-04-13
 */
@Repository
public interface LedgerProvinceProgressMapper extends BaseMapper<LedgerProvinceProgress> {

    List<LedgerProvinceProgress> listPage(LedgerProvinceProgress lpp);
}
