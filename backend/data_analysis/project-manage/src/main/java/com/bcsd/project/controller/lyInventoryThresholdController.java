package com.bcsd.project.controller;

import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.service.lyInventoryThresholdService;
import com.bcsd.common.core.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class lyInventoryThresholdController extends BaseController {

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
        List<lyInventoryThreshold> list = inventoryThresholdService.list(inventoryThreshold);
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
        List<lyInventoryThreshold> list = inventoryThresholdService.codeList(inventoryThreshold);
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
     * 模板下载
     * @param
     * @return
     */
    @PostMapping(value = "/excelDownload")
    public void excelDownload(HttpServletRequest request, HttpServletResponse response) {
        inventoryThresholdService.excelDownload(response,request);
    }
    /**
     * 导入数据
     * @param file 数据文件
     * @return 返回结果
     */
    @PostMapping("/import")
    public AjaxResult importData(MultipartFile file){
        if (file==null||file.isEmpty()){
            return error("导入文件不能为空");
        }
        List<lyInventoryThreshold> list;
        try {
            ExcelUtil<lyInventoryThreshold> util = new ExcelUtil<>(lyInventoryThreshold.class);
            list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list) && list.size()<1){
                return error("未读取到数据,检查是否为空！");
            }
            return inventoryThresholdService.importData(list,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }
}
