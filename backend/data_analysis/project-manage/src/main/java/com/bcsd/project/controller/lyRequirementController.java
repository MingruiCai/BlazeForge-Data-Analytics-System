package com.bcsd.project.controller;

import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.project.domain.lyRequirement;
import com.bcsd.project.service.lyRequirementService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName InventoryThresholdController
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/lyRequirement")
public class lyRequirementController extends BaseController{

    @Autowired
    private lyRequirementService requirementService;

    /**
     * 列表分页
     * @param requirement
     * @return
     */
    @ApiOperation("查看需求分页")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody lyRequirement requirement) {
        startPage(requirement);
        List<lyRequirement> list= requirementService.list(requirement);
        return getDataTable(list);
    }
    /**
     * 新增修改
     * @param inventoryThreshold
     * @return
     */
    @ApiOperation("新增或修改需求计划")
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody lyRequirement requirement) {
        requirementService.addOrUpdate(requirement);
        return AjaxResult.success();
    }
    /**
     * 删除
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除需求计划")
    @PostMapping({"/delete"})
    public AjaxResult delete(@RequestBody JSONObject jsonObject) {
        return requirementService.delete(jsonObject.getLong("id"));
    }
    /**
     * 根据id查询
     * @param jsonObject
     * @return
     */
    @ApiOperation("根据id查询")
    @PostMapping({"/getById"})
    public AjaxResult getById(@RequestBody JSONObject jsonObject) {
        lyRequirement requirement=requirementService.selectByPrimaryKey(jsonObject.getLong("id"));
        return AjaxResult.success(requirement);
    }

}
