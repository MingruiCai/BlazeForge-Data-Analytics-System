package com.bcsd.project.controller;

import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.project.domain.lyThresholdManagement;
import com.bcsd.project.service.lyThresholdManagementService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName 统计阈值管理 lyThresholdManagementController
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/11
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/lyThresholdManagement")
public class lyThresholdManagementController extends BaseController{

    @Autowired
    private lyThresholdManagementService thresholdManagementService;

    /**
     * 列表分页
     * @param thresholdManagement
     * @return
     */
    @ApiOperation("查看阈值管理分页")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody lyThresholdManagement thresholdManagement) {
        startPage(thresholdManagement);
        List<lyThresholdManagement> list= thresholdManagementService.list(thresholdManagement);
        return getDataTable(list);
    }
    /**
     * 新增修改
     * @param thresholdManagement
     * @return
     */
    @ApiOperation("修改阈值管理上下限值")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody lyThresholdManagement thresholdManagement) {
        thresholdManagementService.update(thresholdManagement);
        return AjaxResult.success();
    }
}
