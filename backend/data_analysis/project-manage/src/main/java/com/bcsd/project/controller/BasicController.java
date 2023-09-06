package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BasicController extends BaseController {

    @Autowired
    CommonService commonService;

    @PostMapping("/not/done")
    public AjaxResult notDone(){
        return success(commonService.notDone(getUserId()));
    }

}
