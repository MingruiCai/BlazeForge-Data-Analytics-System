package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.domain.ProjectEarlyWarning;
import com.bcsd.project.service.ProjectEarlyWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project/early/warning")
public class ProjectEarlyWarningController extends BaseController {

    @Autowired
    ProjectEarlyWarningService earlyWarningService;

    /**
     * 统计
     * @return
     */
    @PostMapping("/statistics")
    public AjaxResult statistics(@RequestBody ProjectEarlyWarning params){
        return success(earlyWarningService.statistics(params));
    }

    /**
     * 分页列表
     * @param params
     * @return
     */
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ProjectEarlyWarning params){
        startPage(params);
        return getDataTable(earlyWarningService.listPage(params),true);
    }

    /**
     * 生成预警
     * @return
     */
    @PostMapping("/generate")
    public AjaxResult generate(){
        String dataTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
        return earlyWarningService.generateWarning(dataTime,getUsername());
    }

    /**
     * 转办
     * @param id
     * @return
     */
    @PostMapping("/transfer/{id}")
    public AjaxResult transfer(@PathVariable("id") Long id){
        return earlyWarningService.transfer(id,getUsername());
    }



}
