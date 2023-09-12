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
    @ApiOperation("新增或修改阈值管理")
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody lyThresholdManagement thresholdManagement) {
        thresholdManagementService.addOrUpdate(thresholdManagement);
        return AjaxResult.success();
    }
    /**
     * 删除
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除阈值管理")
    @PostMapping({"/delete"})
    public AjaxResult delete(@RequestBody JSONObject jsonObject) {
        return thresholdManagementService.delete(jsonObject.getLong("id"));
    }
    /**
     * 根据id查询
     * @param jsonObject
     * @return
     */
    @ApiOperation("根据id查询")
    @PostMapping({"/getById"})
    public AjaxResult getById(@RequestBody JSONObject jsonObject) {
        lyThresholdManagement thresholdManagement=thresholdManagementService.selectByPrimaryKey(jsonObject.getLong("id"));
        return AjaxResult.success(thresholdManagement);
    }

}
