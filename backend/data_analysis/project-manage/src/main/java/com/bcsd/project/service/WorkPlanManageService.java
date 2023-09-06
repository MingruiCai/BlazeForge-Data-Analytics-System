package com.bcsd.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.project.domain.WorkPlanManage;
import com.bcsd.project.mapper.WorkPlanManageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bcsd.project.constants.Constants.DELETE_TAG_0;

/**
 * 规划管理 服务实现类
 * @author liuliang
 * @since 2023-03-16
 */
@Slf4j
@Service
public class WorkPlanManageService extends ServiceImpl<WorkPlanManageMapper, WorkPlanManage> implements IService<WorkPlanManage> {

    /**
     * 根据类型获取
     * @param type
     * @return
     */
    public WorkPlanManage getByType(Integer type){
        LambdaQueryWrapper<WorkPlanManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkPlanManage::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(WorkPlanManage::getType,type);
        return baseMapper.selectOne(wrapper);
    }

}
