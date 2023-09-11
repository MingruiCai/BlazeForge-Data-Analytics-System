package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyThresholdManagement;

import java.util.List;

public interface lyThresholdManagementService {
    List<lyThresholdManagement> list(lyThresholdManagement thresholdManagement);

    void addOrUpdate(lyThresholdManagement thresholdManagement);

    AjaxResult delete(Long id);

    lyThresholdManagement selectByPrimaryKey(Long id);
}
