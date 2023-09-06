package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.constants.Constants;
import com.bcsd.project.domain.AchievementArea;
import com.bcsd.project.domain.AchievementProvince;
import com.bcsd.project.service.AchievementAreaService;
import com.bcsd.project.service.AchievementProvinceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/achievement/province")
public class AchievementProvinceController extends BaseController {

    @Autowired
    private AchievementProvinceService provinceService;

    @PostMapping("/compute/{year}")
    public AjaxResult compute(@PathVariable("year") Integer year){
        return success(provinceService.compute(year));
    }


    @PostMapping("/add")
    public AjaxResult add(@RequestBody AchievementProvince province){
        AchievementProvince hProvince = provinceService.getByYear(province.getYear());
        if (hProvince != null) {
            delete(province.getId());
        }
        province.setCreateBy(getUsername());
        province.setCreateTime(DateUtils.getNowDate());
        return toAjax(provinceService.add(province));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody AchievementProvince province) {
        province.setUpdateBy(getUsername());
        province.setUpdateTime(DateUtils.getNowDate());
        return toAjax(provinceService.update(province));
    }

    @PostMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(provinceService.delete(id));
    }

    @PostMapping("/list")
    public TableDataInfo list(@RequestBody AchievementProvince province){
        startPage(province);
        List<AchievementProvince> list = provinceService.list(province);
        return getDataTable(list);
    }

    @PostMapping("/{id}")
    public AjaxResult info(@PathVariable("id") Long id){
        return success(provinceService.getById(id));
    }
}

