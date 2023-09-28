package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.vo.ArchiveProjectVO;
import com.bcsd.project.service.ProjectImplVersionService;
import com.bcsd.project.service.lyInventoryImplService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/inventory")
public class lyInventoryController extends BaseController {

    @Autowired
    lyInventoryImplService inventoryImplService;

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
        List<lyInventory> list;
        try {
            ExcelUtil<lyInventory> util = new ExcelUtil<>(lyInventory.class);
            list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list) && list.size()<1){
                return error("未读取到数据,检查是否为空！");
            }
            return inventoryImplService.importData(list,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }

    /**
     * 已装配零件统计
     * @param
     * @return
     */
    @PostMapping("/getAssembled")
    public AjaxResult getAssembled(){
        return inventoryImplService.getAssembled();
    }

    /**
     * 已装配零件列表
     * @param params
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody lyInventory params){
        startPage(params);
        return getDataTable(inventoryImplService.listPage(params));
    }

    /**
     * 已装配零件列表
     * @param params
     * @return
     */
    @PostMapping("/getAllCount")
    public Map<String,Object> getAllCount(@RequestBody lyInventory params){
        return inventoryImplService.getAllCount(params);
    }

    /**
     * 已装配零件导出
     *
     * @param response
     * @param
     */
    @PostMapping(value = "/excelDownload")
    public void excelDownload(HttpServletRequest request, HttpServletResponse response,lyInventory params) {
        inventoryImplService.excelDownload(response,request,params);
    }

    /**
     * 零件状态统计
     * @param
     * @return
     */
    @PostMapping("/getBatchAttr07")
    public AjaxResult getBatchAttr07(){
        return inventoryImplService.getBatchAttr07();
    }


    /**
     * 零件状态统计导出
     *
     * @param response
     * @param
     */
    @PostMapping(value = "/excelDownload2")
    public void excelDownload2(HttpServletRequest request, HttpServletResponse response, lyInventory params) {
        inventoryImplService.excelDownload2(response,request,params);
    }

    /**
     * 零件库存告警统计列表
     * @param params
     * @return
     */
    @PostMapping("/inventoryAlarmList")
    public TableDataInfo inventoryAlarmList(@RequestBody lyInventory params){
        startPage(params);
        return getDataTable(inventoryImplService.inventoryAlarmCount(params));
    }

    /**
     * 零件库存告警统计导出
     *
     * @param response
     * @param
     */
    @PostMapping(value = "/excelDownload3")
    public void excelDownload3(HttpServletRequest request, HttpServletResponse response, lyInventory params) {
        inventoryImplService.excelDownload3(response,request,params);
    }

    /**
     * 零件计划缺口统计列表
     * @param params
     * @return
     */
    @PostMapping("/getGapsNumberList")
    public TableDataInfo getGapsNumberList(@RequestBody lyInventory params){
        startPage(params);
        return getDataTable(inventoryImplService.getGapsNumber(params));
    }

    /**
     * 零件计划缺口统计导出
     *
     * @param response
     * @param
     */
    @PostMapping(value = "/excelDownload4")
    public void excelDownload4(HttpServletRequest request, HttpServletResponse response, lyInventory params) {
        inventoryImplService.excelDownload4(response,request,params);
    }

    /**
     * 零件计划缺口统计修改状态
     * @param params
     */
    @PostMapping(value = "/updProcessingStatus")
    public void updProcessingStatus(@RequestBody JSONObject params) {
        inventoryImplService.updProcessingStatus(params,getUsername());
    }


    /**
     * 中央大屏（左侧）-零件计划缺口统计明细
     * @param
     * @return
     */
    @PostMapping("/getFhjhljqkqkList")
    public AjaxResult getFhjhljqkqkList(){
        return AjaxResult.success(inventoryImplService.getFhjhljqkqkList());
    }

    /**
     * 中央大屏（左侧）-零件计划缺口统计
     * @param
     * @return
     */
    @PostMapping("/getFhjhljqkqkCount")
    public AjaxResult getFhjhljqkqkCount(){
        return AjaxResult.success(inventoryImplService.getFhjhljqkqkCount());
    }


    /**
     * 中央大屏（右侧）-零件库存预警情况明细
     * @param
     * @return
     */
    @PostMapping("/getKcyjList")
    public AjaxResult getKcyjList(){
        return AjaxResult.success(inventoryImplService.getKcyjList());
    }

    /**
     * 中央大屏（右侧）-零件库存预警情况统计
     * @param
     * @return
     */
    @PostMapping("/getKcyjCount")
    public AjaxResult getKcyjCount(){
        return AjaxResult.success(inventoryImplService.getKcyjCount());
    }



    @PostMapping({"/getkc"})
    public AjaxResult getkc() {
        inventoryImplService.add();
        return AjaxResult.success();
    }
}
