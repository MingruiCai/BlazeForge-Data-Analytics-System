package com.bcsd.project.controller;

import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyRequirement;
import com.bcsd.project.service.lyRequirementService;
import io.swagger.annotations.ApiOperation;
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

/**
 * @ClassName 需求计划设置 lyRequirementController
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/8
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
     * 零件号分页
     * @param requirement
     * @return
     */
    @ApiOperation("查看零件号分页")
    @PostMapping("/codeList")
    public TableDataInfo codeList(@RequestBody lyRequirement requirement) {
        startPage(requirement);
        List<lyRequirement> list = requirementService.codeList(requirement);
        return getDataTable(list);
    }
    /**
     * 新增修改
     * @param requirement
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
     * 模板下载
     * @param
     * @return
     */
    @PostMapping(value = "/excelDownload")
    public void excelDownload(HttpServletRequest request, HttpServletResponse response) {
        requirementService.excelDownload(response,request);
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
        List<lyRequirement> list;
        try {
            ExcelUtil<lyRequirement> util = new ExcelUtil<>(lyRequirement.class);
            list = util.importExcel(file.getInputStream());
            if (CollectionUtils.isEmpty(list) && list.size()<1){
                return error("未读取到数据,检查是否为空！");
            }
            return requirementService.importData(list,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }

}
