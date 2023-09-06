package com.bcsd.project.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.ProjectImplInfoSnapshot;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.mapper.ProjectImplInfoSnapshotMapper;
import com.bcsd.project.mapper.ProjectImplVersionMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.bcsd.project.constants.Constants.DELETE_TAG_0;

/**
 * 项目实施信息快照 服务实现类
 *
 * @author liuliang
 * @since 2023-02-28
 */
@Slf4j
@Service
public class ProjectImplInfoSnapshotService extends ServiceImpl<ProjectImplInfoSnapshotMapper, ProjectImplInfoSnapshot> implements IService<ProjectImplInfoSnapshot> {

    @Autowired
    ProjectImplVersionMapper versionMapper;

    /**
     * 生成数据快照
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult generateDataSnapshot(String year,String month,String userName){
        //判断是否存在
        if (isExists(year,month)){
            deleteData(year,month);
        }
        //获取最新版本数据
        List<ProjectImplVersion> versions = versionMapper.selectNewData();
        if (CollectionUtils.isEmpty(versions)){
            return AjaxResult.error("没有项目实施信息！");
        }
        //项目实施信息
        saveImplInfoSnapshot(versions,year,month,userName);
        return AjaxResult.success();
    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    public List<ProjectImplInfoSnapshot> list(ProjectImplInfoSnapshot params){
        LambdaQueryWrapper<ProjectImplInfoSnapshot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplInfoSnapshot::getDeleteTag,DELETE_TAG_0);
        if (StringUtils.isNotEmpty(params.getProjectNo())){
            wrapper.like(ProjectImplInfoSnapshot::getProjectNo,params.getProjectNo());
        }
        if (StringUtils.isNotEmpty(params.getProjectName())){
            wrapper.like(ProjectImplInfoSnapshot::getProjectName,params.getProjectName());
        }
        if (StringUtils.isNotEmpty(params.getCityDistrict())){
            wrapper.eq(ProjectImplInfoSnapshot::getCityDistrict,params.getCityDistrict());
        }
        if (StringUtils.isNotEmpty(params.getYearMonth())){
            String[] ym = params.getYearMonth().split("-");
            wrapper.eq(ProjectImplInfoSnapshot::getDataYear,ym[0]);
            wrapper.eq(ProjectImplInfoSnapshot::getDataMonth,ym[1]);
        }
        wrapper.orderByDesc(ProjectImplInfoSnapshot::getCreateTime);
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 保存实施信息数据快照
     * @param versions
     * @param year
     * @param month
     * @param userName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveImplInfoSnapshot(List<ProjectImplVersion> versions,String year,String month,String userName){
        Date currentDate = DateUtils.getNowDate();
        for (ProjectImplVersion version:versions){
            ProjectImplInfoSnapshot implInfoSnapshot = JSON.parseObject(version.getBasicInfo(),ProjectImplInfoSnapshot.class);
            implInfoSnapshot.setVersionId(version.getId());
            implInfoSnapshot.setDataYear(year);
            implInfoSnapshot.setDataMonth(month);
            implInfoSnapshot.setCreateTime(currentDate);
            implInfoSnapshot.setCreateBy(userName);
            implInfoSnapshot.setId(null);
            implInfoSnapshot.setUpdateBy(null);
            implInfoSnapshot.setUpdateTime(null);
            super.baseMapper.insert(implInfoSnapshot);
        }
    }

    /**
     * 是否存在
     * @param year
     * @param month
     * @return
     */
    private boolean isExists(String year,String month){
        LambdaQueryWrapper<ProjectImplInfoSnapshot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplInfoSnapshot::getDeleteTag,DELETE_TAG_0);
        wrapper.eq(ProjectImplInfoSnapshot::getDataYear,year);
        wrapper.eq(ProjectImplInfoSnapshot::getDataMonth,month);
        return super.count(wrapper)>0;
    }

    /**
     * 删除数据
     * @param year
     * @param month
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteData(String year,String month){
        LambdaQueryWrapper<ProjectImplInfoSnapshot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectImplInfoSnapshot::getDataYear,year);
        wrapper.eq(ProjectImplInfoSnapshot::getDataMonth,month);
        super.baseMapper.delete(wrapper);
    }





}
