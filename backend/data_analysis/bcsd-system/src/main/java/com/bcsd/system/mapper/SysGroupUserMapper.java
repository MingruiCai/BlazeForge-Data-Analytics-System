package com.bcsd.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.system.domain.SysGroupUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自定义分组用户绑定 Mapper 接口
 * @author liuliang
 * @since 2023-03-16
 */
@Repository
public interface SysGroupUserMapper extends BaseMapper<SysGroupUser> {

    /**
     * 批量新增分组用户信息
     *
     * @param list 分组用户列表
     * @return 结果
     */
    public int batchInsert(List<SysGroupUser> list);

}
