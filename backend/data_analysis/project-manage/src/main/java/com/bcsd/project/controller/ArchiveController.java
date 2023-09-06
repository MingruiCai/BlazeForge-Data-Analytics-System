package com.bcsd.project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.project.domain.ProjectImplVersion;
import com.bcsd.project.domain.vo.ArchiveProjectVO;
import com.bcsd.project.service.ProjectImplVersionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/archive")
public class ArchiveController extends BaseController {

    @Autowired
    ProjectImplVersionService projectImplVersionService;

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
        try {
            //读取数据
            List<ArchiveProjectVO> data = EasyExcel.read(file.getInputStream()).head(ArchiveProjectVO.class).headRowNumber(6).doReadAllSync();
            if (CollectionUtils.isEmpty(data)&&data.size()<1){
                return error("未读取到数据,检查模板是否为空！");
            }
            return projectImplVersionService.importData(data,getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return error("数据导入失败！");
        }
    }

    /**
     * 发送列表
     * @param params
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ProjectImplVersion params){
        startPage(params);
        return getDataTable(projectImplVersionService.listPage(params));
    }

    /**
     * 获取详情
     */
    @PostMapping("/info/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id){
        return success(projectImplVersionService.getInfo(id));
    }

    /**
     * 修改信息
     * @param params
     * @return
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody JSONObject params){
        return toAjax(projectImplVersionService.updateInfo(params,getUsername()));
    }

    /**
     * 删除项目
     * @param projectNo
     * @return
     */
    @PostMapping("/del/{id}")
    public AjaxResult delete(@PathVariable("id") String projectNo){
        return toAjax(projectImplVersionService.delete(projectNo));
    }

}
