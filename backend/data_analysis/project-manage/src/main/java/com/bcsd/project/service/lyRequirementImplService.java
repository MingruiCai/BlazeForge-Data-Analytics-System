package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyRequirement;
import com.bcsd.project.mapper.lyRequirementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.bcsd.common.utils.SecurityUtils.getUsername;

/**
 * 用药咨询
 *
 * @ClassName InventoryThresholdServiceImpl
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 **/
@Slf4j
@Service
public class lyRequirementImplService implements lyRequirementService {

    @Autowired
    private lyRequirementMapper requirementMapper;

    /**
     * 列表
     * @param requirement
     */
    @Override
    public List<lyRequirement> list(lyRequirement requirement) {

        return requirementMapper.selectInventoryThresholdList(requirement);

    }

    /**
     * 新增更新
     * @param requirement
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(lyRequirement requirement) {
        if (requirement.getId() != null) {
            requirement.setUpdateBy(getUsername());
            requirement.setUpdateTime(new Date());
            requirementMapper.updateByPrimaryKeySelective(requirement);
        } else {
            requirement.setCreateBy(getUsername());
            requirement.setCreateTime(new Date());
            requirementMapper.insertSelective(requirement);
        }
    }

    /**
     * 删除
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        requirementMapper.deleteByPrimaryKey(id);
        return AjaxResult.success();
    }

    /**
     * 根据ID查询
     * @param id
     * @return inventoryThreshold
     */
    @Override
    public lyRequirement selectByPrimaryKey(Long id) {
        return requirementMapper.selectByPrimaryKey(id);
    }
}

