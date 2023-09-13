package com.bcsd.project.controller;

import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
//import com.bcsd.project.domain.Consultation;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.service.lyInventoryThresholdService;
import com.bcsd.common.core.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName 零件库存阈值 lyInventoryThresholdController
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/lyInventoryThreshold")
public class lyInventoryThresholdController extends BaseController{

    @Autowired
    private lyInventoryThresholdService inventoryThresholdService;

    /**
     * 列表分页
     * @param inventoryThreshold
     * @return
     */
    @ApiOperation("查看零件库存阈值分页")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody lyInventoryThreshold inventoryThreshold) {
        startPage(inventoryThreshold);
        List<lyInventoryThreshold> list= inventoryThresholdService.list(inventoryThreshold);
        return getDataTable(list);
    }
    /**
     * 零件号分页
     * @param inventoryThreshold
     * @return
     */
    @ApiOperation("查看零件号分页")
    @PostMapping("/codeList")
    public TableDataInfo codeList(@RequestBody lyInventoryThreshold inventoryThreshold) {
        startPage(inventoryThreshold);
        List<lyInventoryThreshold> list= inventoryThresholdService.codeList(inventoryThreshold);
        return getDataTable(list);
    }
    /**
     * 新增修改
     * @param inventoryThreshold
     * @return
     */
    @ApiOperation("新增或修改零件库存阈值")
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody lyInventoryThreshold inventoryThreshold) {
        return inventoryThresholdService.addOrUpdate(inventoryThreshold);
    }
    /**
     * 删除
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除零件库存阈值")
    @PostMapping({"/delete"})
    public AjaxResult delete(@RequestBody JSONObject jsonObject) {
        return inventoryThresholdService.delete(jsonObject.getLong("id"));
    }
    /**
     * 根据id查询
     * @param jsonObject
     * @return
     */
    @ApiOperation("根据id查询")
    @PostMapping({"/getById"})
    public AjaxResult getById(@RequestBody JSONObject jsonObject) {
        lyInventoryThreshold inventoryThreshold=inventoryThresholdService.selectByPrimaryKey(jsonObject.getLong("id"));
        return AjaxResult.success(inventoryThreshold);
    }

}
