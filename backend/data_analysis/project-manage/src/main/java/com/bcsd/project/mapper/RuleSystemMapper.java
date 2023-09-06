package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.RuleSystem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 规章制度表 Mapper 接口
 * @author liuliang
 * @since 2023-03-16
 */
@Repository
public interface RuleSystemMapper extends BaseMapper<RuleSystem> {

    /**
     * 分页查询
     * @param params
     * @return
     */
    List<RuleSystem> listPage(RuleSystem params);

}
