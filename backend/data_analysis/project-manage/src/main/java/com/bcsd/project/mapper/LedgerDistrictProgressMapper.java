package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.LedgerDistrictProgress;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 区县进展台账 Mapper 接口
 *
 * @author liuliang
 * @since 2023-04-12
 */
@Repository
public interface LedgerDistrictProgressMapper extends BaseMapper<LedgerDistrictProgress> {

    /**
     * 分页列表
     * @param ldp
     * @return
     */
    List<LedgerDistrictProgress> listPage(LedgerDistrictProgress ldp);

    /**
     * 查询未创建月度台账的区县
     * @param ldp
     * @return
     */
    List<LedgerDistrictProgress> selectNotCreateDistrict(LedgerDistrictProgress ldp);

    /**
     * 查询创建月度台账的区县
     * @param ldp
     * @return
     */
    List<LedgerDistrictProgress> selectCreateDistrict(LedgerDistrictProgress ldp);

}
