package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.domain.RuleSystem;
import com.bcsd.project.service.RuleSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规章制度表 控制器
 *
 * @author liuliang
 * @since 2023-03-16
 */
@Slf4j
@RestController
@RequestMapping("/rule/system")
public class RuleSystemController extends BaseController {

    @Autowired
    private RuleSystemService ruleSystemService;

    /**
     * 规章制度表保存
     */
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody RuleSystem ruleSystem){
        if (ruleSystem.getId()==null){
            ruleSystem.setCreateBy(getUsername());
            ruleSystem.setCreateTime(DateUtils.getNowDate());
            ruleSystem.setUpdateBy(getUsername());
            ruleSystem.setUpdateTime(ruleSystem.getCreateTime());
        }else{
            ruleSystem.setUpdateBy(getUsername());
            ruleSystem.setUpdateTime(DateUtils.getNowDate());
        }
        return toAjax(ruleSystemService.saveOrUpdate(ruleSystem));
    }

    /**
     * 删除规章制度表
     */
    @PostMapping("/delete/{id}")
    public AjaxResult deleteRuleSystem(@PathVariable("id") Long id){
        return toAjax(ruleSystemService.delete(id));
    }

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public TableDataInfo libraryList(@RequestBody RuleSystem params){
        startPage(params);
        List<RuleSystem> list = ruleSystemService.list(params);
        return getDataTable(list,true);
    }


}

