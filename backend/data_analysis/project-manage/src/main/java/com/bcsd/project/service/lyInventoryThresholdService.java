package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.framework.log.Jackson;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.Consultation;

import java.util.List;

/**
 * @ClassName InventoryThresholdService
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
public interface lyInventoryThresholdService {
    List<lyInventoryThreshold> list(lyInventoryThreshold inventoryThreshold);

    void addOrUpdate(lyInventoryThreshold inventoryThreshold);

    AjaxResult delete(Long id);
}
