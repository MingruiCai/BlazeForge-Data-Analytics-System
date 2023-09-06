package com.bcsd.project.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bcsd.common.utils.StringUtils;
import com.bcsd.project.domain.AchievementProject;
import com.bcsd.project.domain.AchievementProjectDetail;
import com.bcsd.project.mapper.AchievementProjectDetailMapper;
import com.bcsd.project.mapper.AchievementProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bcsd.project.constants.Constants.DELETE_TAG_0;
import static com.bcsd.project.constants.Constants.DELETE_TAG_1;

@Slf4j
@Service
public class AchievementProjectService extends ServiceImpl<AchievementProjectMapper, AchievementProject> implements IService<AchievementProject> {

    @Autowired
    private AchievementProjectDetailMapper projectDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    public boolean add(AchievementProject project) {
        super.save(project);
        for (AchievementProjectDetail projectDetail:project.getDetailList()) {
            projectDetail.setAchievementId(project.getId());
            projectDetail.setCreateBy(project.getCreateBy());
            projectDetail.setCreateTime(project.getCreateTime());
            projectDetailMapper.insert(projectDetail);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(AchievementProject project) {
        super.updateById(project);

        LambdaQueryWrapper<AchievementProjectDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProjectDetail::getAchievementId, project.getId());
        projectDetailMapper.delete(wrapper);
        for (AchievementProjectDetail projectDetail:project.getDetailList()) {
            projectDetail.setAchievementId(project.getId());
            projectDetail.setUpdateBy(project.getUpdateBy());
            projectDetail.setUpdateTime(project.getUpdateTime());
            projectDetailMapper.insert(projectDetail);
        }

        return true;
    }

    public boolean delete(Long id) {
        AchievementProject project = new AchievementProject();
        project.setId(id);
        project.setDeleteTag(DELETE_TAG_1);
        return super.updateById(project);
    }

    public List<AchievementProject> list(AchievementProject project) {
        LambdaQueryWrapper<AchievementProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProject::getDeleteTag, DELETE_TAG_0);

        if (project.getYear() != null){
            wrapper.eq(AchievementProject::getYear, project.getYear());
        }

        if (StringUtils.isNotEmpty(project.getStatus())) {
            wrapper.eq(AchievementProject::getStatus, project.getStatus());
        }

        if (StringUtils.isNotEmpty(project.getTransferPaymentName())) {
            wrapper.like(AchievementProject::getTransferPaymentName, project.getTransferPaymentName());
        }

        wrapper.orderByDesc(AchievementProject::getCreateTime);
        return super.baseMapper.selectList(wrapper);
    }

    public AchievementProject getById(Long id) {
        AchievementProject project = super.getById(id);
        LambdaQueryWrapper<AchievementProjectDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProjectDetail::getAchievementId, project.getId());
        List<AchievementProjectDetail> detailList = projectDetailMapper.selectList(wrapper);
        project.setDetailList(detailList);

        return project;
    }

    public boolean exists(AchievementProject project) {
        LambdaQueryWrapper<AchievementProject> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AchievementProject::getDeleteTag, DELETE_TAG_0);
        wrapper.eq(AchievementProject::getDeptId, project.getDeptId());
        wrapper.eq(AchievementProject::getProjectId, project.getProjectId());
        wrapper.eq(AchievementProject::getYear, project.getYear());
        return super.baseMapper.selectCount(wrapper) > 0;

    }
}
