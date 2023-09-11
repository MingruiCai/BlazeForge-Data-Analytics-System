package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
//import com.bcsd.framework.log.Jackson;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.Consultation;

import java.util.List;

public interface lyInventoryThresholdService {
    List<lyInventoryThreshold> list(lyInventoryThreshold inventoryThreshold);

    void addOrUpdate(lyInventoryThreshold inventoryThreshold);

    AjaxResult delete(Long id);

    lyInventoryThreshold selectByPrimaryKey(Long id);
}
