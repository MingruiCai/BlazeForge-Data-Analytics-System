package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.AchievementProject;
import com.bcsd.project.service.AchievementProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/achievement/project")
public class AchievementProjectController extends BaseController {

    @Autowired
    private AchievementProjectService projectService;

    @PostMapping("/add")
    public AjaxResult add(@RequestBody AchievementProject project){
        project.setDeptId(getDeptId());
        if (projectService.exists(project)){
            return error("区县年度绩效已存在！");
        }
        project.setCreateBy(getUsername());
        project.setCreateTime(DateUtils.getNowDate());
        project.setStatus(Constants.ACHIEVEMENT_STATUS_DRAFT);
        return toAjax(projectService.add(project));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody AchievementProject project) {
        project.setUpdateBy(getUsername());
        project.setUpdateTime(DateUtils.getNowDate());
        return toAjax(projectService.update(project));
    }

    @PostMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(projectService.delete(id));
    }

    @PostMapping("/review")
    public AjaxResult review(@RequestBody AchievementProject project) {
        project.setStatus(Constants.ACHIEVEMENT_STATUS_REVIEWING);

        if (project.getDeptId() == null) {
            project.setDeptId(getDeptId());
            if (projectService.exists(project)){
                return error("区县年度绩效已存在！");
            }
            project.setCreateBy(getUsername());
            project.setCreateTime(DateUtils.getNowDate());

            return toAjax(projectService.add(project));
        }

        project.setUpdateBy(getUsername());
        project.setUpdateTime(DateUtils.getNowDate());
        return toAjax(projectService.update(project));
    }

    @PostMapping("/reviewed")
    public AjaxResult reviewed(@RequestBody AchievementProject project) {
        project.setStatus(Constants.ACHIEVEMENT_STATUS_REVIEWED);
        project.setUpdateBy(getUsername());
        project.setUpdateTime(DateUtils.getNowDate());
        return toAjax(projectService.update(project));
    }

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody AchievementProject project){
        startPage(project);
        List<AchievementProject> list = projectService.list(project);
        return getDataTable(list);
    }

    @PostMapping("/{id}")
    public AjaxResult info(@PathVariable("id") Long id){
        return success(projectService.getById(id));
    }
}

