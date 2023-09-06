package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.domain.WorkPlanManage;
import com.bcsd.project.service.WorkPlanManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 规划管理 控制器
 *
 * @author liuliang
 * @since 2023-03-16
 */
@Slf4j
@RestController
@RequestMapping("/work/plan/manage")
public class WorkPlanManageController extends BaseController {

    @Autowired
    private WorkPlanManageService workPlanManageService;

    /**
     * 保存
     * @param params
     * @return
     */
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody WorkPlanManage params){
        WorkPlanManage wpm = workPlanManageService.getByType(params.getType());
        if (wpm==null){
            params.setCreateBy(getUsername());
            params.setCreateTime(DateUtils.getNowDate());
        }else{
            params.setId(wpm.getId());
            params.setUpdateBy(getUsername());
            params.setUpdateTime(DateUtils.getNowDate());
        }
        return toAjax(workPlanManageService.saveOrUpdate(params));
    }

    /**
     * 根据类型获取
     * @param type
     * @return
     */
    @PostMapping("/get/{type}")
    public AjaxResult getByType(@PathVariable("type") Integer type){
        return success(workPlanManageService.getByType(type));
    }
}

