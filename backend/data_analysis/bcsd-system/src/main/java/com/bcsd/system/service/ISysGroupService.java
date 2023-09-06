package com.bcsd.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bcsd.common.core.domain.entity.SysGroup;
import com.bcsd.system.domain.vo.GroupUserVo;

import java.util.List;

public interface ISysGroupService extends IService<SysGroup> {

    /**
     * 分组列表
     * @param userId
     * @return
     */
    List<SysGroup> getGroupListByUserId(Long userId);

    /**
     * 删除分组
     * @param params
     * @return
     */
    boolean groupDel(SysGroup params);

    /**
     * 绑定分组用户
     * @param params
     */
    void bindUser(GroupUserVo params);

}
