package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyRequirement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface lyRequirementService {
    List<lyRequirement> list(lyRequirement requirement);

    List<lyRequirement> codeList(lyRequirement requirement);

    AjaxResult addOrUpdate(lyRequirement requirement);

    AjaxResult delete(Long id);

    void excelDownload(HttpServletResponse response, HttpServletRequest request);

    AjaxResult importData(List<lyRequirement> data, String userName);


}
