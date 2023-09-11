package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyRequirement;

import java.util.List;

/**
 * @ClassName InventoryThresholdService
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
public interface lyRequirementService {
    List<lyRequirement> list(lyRequirement requirement);

    void addOrUpdate(lyRequirement requirement);

    AjaxResult delete(Long id);

    lyRequirement selectByPrimaryKey(Long id);
}
