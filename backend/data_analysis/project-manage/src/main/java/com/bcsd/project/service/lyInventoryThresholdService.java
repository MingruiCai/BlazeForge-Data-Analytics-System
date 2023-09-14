package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
//import com.bcsd.framework.log.Jackson;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.Consultation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface lyInventoryThresholdService {
    List<lyInventoryThreshold> list(lyInventoryThreshold inventoryThreshold);

    List<lyInventoryThreshold> codeList(lyInventoryThreshold inventoryThreshold);

    AjaxResult addOrUpdate(lyInventoryThreshold inventoryThreshold);

    AjaxResult delete(Long id);

    void excelDownload(HttpServletResponse response, HttpServletRequest request);

    AjaxResult importData(List<lyInventoryThreshold> data, String userName);


}
