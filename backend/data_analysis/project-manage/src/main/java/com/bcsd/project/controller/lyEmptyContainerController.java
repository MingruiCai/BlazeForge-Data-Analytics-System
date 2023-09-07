package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyEmptyContainer;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.service.lyEmptyContainerImplService;
import com.bcsd.project.service.lyInventoryImplService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emptyContainer")
public class lyEmptyContainerController extends BaseController {

    @Autowired
    lyEmptyContainerImplService emptyContainerImplService;

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
        List<lyEmptyContainer> list;
        try {
            ExcelUtil<lyEmptyContainer> util = new ExcelUtil<>(lyEmptyContainer.class);
            list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list) && list.size()<1){
                return error("未读取到数据,检查是否为空！");
            }
            return emptyContainerImplService.importData(list,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }


    /**
     * 空容器统计
     * @param
     * @return
     */
    @PostMapping("/getContainerCount")
    public AjaxResult getContainerCount(){
        return AjaxResult.success(emptyContainerImplService.getContainerCount());
    }
    /**
     * 空容器统计导出
     * @param
     * @return
     */
    @PostMapping(value = "/excelDownload")
    public void excelDownload(HttpServletRequest request, HttpServletResponse response) {
        emptyContainerImplService.excelDownload(response,request);
    }

    /**
     * 空容器统计列表
     * @param params
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody lyEmptyContainer params){
        startPage(params);
        return getDataTable(emptyContainerImplService.listPage(params));
    }

    /**
     * 空容器统计列表导出
     * @param
     * @return
     */
    @PostMapping(value = "/excelDownload2")
    public void excelDownload2(HttpServletRequest request, HttpServletResponse response,lyEmptyContainer params) {
        emptyContainerImplService.excelDownload2(response,request,params);
    }

    /**
     * 列表
     * @param params
     * @return
     */
//    @PostMapping("/list")
//    public TableDataInfo list(@RequestBody ProjectImplVersion params){
//        startPage(params);
//        return getDataTable(lyInventoryImplService.listPage(params));
//    }

    /**
     * 获取详情
     */
//    @PostMapping("/info/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id){
//        return success(lyInventoryImplService.getInfo(id));
//    }



}
