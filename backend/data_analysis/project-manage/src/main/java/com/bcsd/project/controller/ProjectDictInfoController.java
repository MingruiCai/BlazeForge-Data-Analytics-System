package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.service.ProjectDictInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目字典 控制器
 * @author liuliang
 * @since 2023-02-20
 */
@Slf4j
@RestController
@RequestMapping("/project/dict")
public class ProjectDictInfoController extends BaseController {

    @Autowired
    private ProjectDictInfoService dictInfoService;


    @PostMapping("/tree/{no}")
    public AjaxResult getTree(@PathVariable("no") String no){
        return success(dictInfoService.getTreeByNo(no));
    }

}

