package com.bcsd.project.controller;

import com.bcsd.common.core.controller.BaseController;
import com.bcsd.common.core.page.TableDataInfo;
import com.bcsd.project.domain.RuleSystem;
import com.bcsd.project.domain.SmsSendRecord;
import com.bcsd.project.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController extends BaseController {

    @Autowired
    SmsService smsService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public TableDataInfo listPage(@RequestBody SmsSendRecord params){
        startPage(params);
        List<SmsSendRecord> list = smsService.listPage(params);
        return getDataTable(list);
    }

}
