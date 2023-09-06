package com.bcsd.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.core.domain.entity.SysGroup;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.system.domain.SysGroupUser;
import com.bcsd.system.domain.vo.GroupUserVo;
import com.bcsd.system.mapper.SysGroupMapper;
import com.bcsd.system.mapper.SysGroupUserMapper;
import com.bcsd.system.service.ISysGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements ISysGroupService {

    @Autowired
    SysGroupUserMapper groupUserMapper;

    /**
     * 分组列表
     * @param userId
     * @return
     */
    @Override
    public List<SysGroup> getGroupListByUserId(Long userId) {
        LambdaQueryWrapper<SysGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroup::getUserId,userId);
        wrapper.eq(SysGroup::getDeleteTag,0);
        wrapper.orderByAsc(SysGroup::getOrderNum);
        return this.list(wrapper);
    }

    /**
     * 删除分组
     * @param params
     * @return
     */
    @Override
    public boolean groupDel(SysGroup params) {
        params.setDeleteTag(1);
        params.setUpdateTime(DateUtils.getNowDate());
        return updateById(params);
    }

    /**
     * 绑定用户
     * @param params
     */
    @Override
    public void bindUser(GroupUserVo params) {
        //删除绑定关系
        LambdaQueryWrapper<SysGroupUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysGroupUser::getGroupId,params.getGroupId());
        groupUserMapper.delete(wrapper);
        //重新绑定
        if (StringUtils.isNotEmpty(params.getUserIds())){
            List<SysGroupUser> list = new ArrayList<>();
            for (Long userId:params.getUserIds()) {
                SysGroupUser sgu = new SysGroupUser();
                sgu.setGroupId(params.getGroupId());
                sgu.setUserId(userId);
                list.add(sgu);
            }
            groupUserMapper.batchInsert(list);
        }
    }
}
