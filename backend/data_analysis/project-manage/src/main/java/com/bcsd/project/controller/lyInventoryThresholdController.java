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
 * @ClassName InventoryThresholdController
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
@Slf4j
@RestController
@RequestMapping("/InventoryThreshold")
public class lyInventoryThresholdController extends BaseController{

    @Autowired
    private lyInventoryThresholdService inventoryThresholdService;

    /**
     * 列表分页
     * @param inventoryThreshold
     * @return
     */
    @ApiOperation("查看零件库存阈值分页")
    @PostMapping("/patientConsultationList")
    public TableDataInfo list(@RequestBody lyInventoryThreshold inventoryThreshold) {
        startPage(inventoryThreshold);
        List<lyInventoryThreshold> list= inventoryThresholdService.list(inventoryThreshold);
        return getDataTable(list);
    }
    /**
     * 新增修改
     * @param lyInventoryThreshold
     * @return
     */
    @ApiOperation("新增或修改零件库存阈值")
    @PostMapping("/addOrUpdate")
    public AjaxResult addOrUpdate(@RequestBody lyInventoryThreshold inventoryThreshold) {
        inventoryThresholdService.addOrUpdate(inventoryThreshold);
        return AjaxResult.success();
    }
    /**
     * 删除零件库存阈值
     * @param jsonObject
     * @return
     */
    @ApiOperation("删除零件库存阈值")
    @PostMapping({"/deleteByid"})
    public AjaxResult deleteByid(@RequestBody JSONObject jsonObject) {
        return inventoryThresholdService.delete(jsonObject.getLong("id"));
    }

}
