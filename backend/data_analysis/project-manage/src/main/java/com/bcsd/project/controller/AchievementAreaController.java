package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.AchievementArea;
import com.bcsd.project.domain.ProjectPlanManage;
import com.bcsd.project.service.AchievementAreaService;
import com.bcsd.project.service.ProjectPlanManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/achievement/area")
public class AchievementAreaController extends BaseController {

    @Autowired
    private AchievementAreaService areaService;

    @PostMapping("/add")
    public AjaxResult add(@RequestBody AchievementArea area){
        area.setDeptId(getDeptId());
//        if (areaService.exists(area)){
//            return error("区县年度绩效已存在！");
//        }
        area.setCreateBy(getUsername());
        area.setCreateTime(DateUtils.getNowDate());
        area.setStatus(Constants.ACHIEVEMENT_STATUS_DRAFT);
        return toAjax(areaService.add(area));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody AchievementArea area) {
        area.setUpdateBy(getUsername());
        area.setUpdateTime(DateUtils.getNowDate());
        return toAjax(areaService.update(area));
    }

    @PostMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(areaService.delete(id));
    }

    @PostMapping("/review")
    public AjaxResult review(@RequestBody AchievementArea area) {
        area.setStatus(Constants.ACHIEVEMENT_STATUS_REVIEWING);

        if (area.getDeptId() == null) {
            area.setDeptId(getDeptId());
            if (areaService.exists(area)){
                return error("区县年度绩效已存在！");
            }
            area.setCreateBy(getUsername());
            area.setCreateTime(DateUtils.getNowDate());

            return toAjax(areaService.add(area));
        }

        area.setUpdateBy(getUsername());
        area.setUpdateTime(DateUtils.getNowDate());
        return toAjax(areaService.update(area));
    }

    @PostMapping("/reviewed")
    public AjaxResult reviewed(@RequestBody AchievementArea area) {
        area.setStatus(Constants.ACHIEVEMENT_STATUS_REVIEWED);
        area.setUpdateBy(getUsername());
        area.setUpdateTime(DateUtils.getNowDate());
        return toAjax(areaService.update(area));
    }

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody AchievementArea area){
        startPage(area);
        List<AchievementArea> list = areaService.list(area);
        return getDataTable(list);
    }

    @PostMapping("/{id}")
    public AjaxResult info(@PathVariable("id") Long id){
        return success(areaService.getById(id));
    }
}

