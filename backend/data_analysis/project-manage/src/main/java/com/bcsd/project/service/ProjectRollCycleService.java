package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectRollCycle;
import com.bcsd.project.mapper.ProjectRollCycleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bcsd.project.constants.Constants.*;

/**
 * 滚动周期表 服务实现类
 *
 * @author liuliang
 * @since 2023-02-16
 */
@Slf4j
@Service
public class ProjectRollCycleService extends ServiceImpl<ProjectRollCycleMapper, ProjectRollCycle> implements IService<ProjectRollCycle> {

    /**
     * 新增
     * @param params
     * @return
     */
    public boolean add(ProjectRollCycle params){
        params.setCreateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(params.getFileList())){
            params.setFileInfo(JSON.toJSONString(params.getFileList()));
        }
        return super.save(params);
    }

    /**
     * 更新
     * @param params
     * @return
     */
    public boolean update(ProjectRollCycle params){
        params.setUpdateTime(DateUtils.getNowDate());
        if (StringUtils.isNotEmpty(params.getFileList())){
            params.setFileInfo(JSON.toJSONString(params.getFileList()));
        }else{
            params.setFileInfo("");
        }
        return super.updateById(params);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public boolean delete(Long id){
        ProjectRollCycle params = new ProjectRollCycle();
        params.setId(id);
        params.setDeleteTag(DELETE_TAG_1);
        return super.updateById(params);
    }

    /**
     * 查询所有
     * @return
     */
    public List<ProjectRollCycle> findAll(){
        LambdaQueryWrapper<ProjectRollCycle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectRollCycle::getDeleteTag,DELETE_TAG_0);
        wrapper.orderByDesc(ProjectRollCycle::getBeginYear);
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 根据条件查询数量
     * @param params
     * @return
     */
    public int selectByParams(ProjectRollCycle params){
        LambdaQueryWrapper<ProjectRollCycle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectRollCycle::getBeginYear,params.getBeginYear());
        wrapper.eq(ProjectRollCycle::getEndYear,params.getEndYear());
        wrapper.eq(ProjectRollCycle::getDeleteTag,DELETE_TAG_0);
        if (params.getId()!=null){
            wrapper.ne(ProjectRollCycle::getId,params.getId());
        }
        return super.baseMapper.selectCount(wrapper);
    }


}
