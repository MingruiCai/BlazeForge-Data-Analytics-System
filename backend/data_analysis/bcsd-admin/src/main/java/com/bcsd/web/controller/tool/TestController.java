package com.bcsd.web.controller.tool;

import com.alibaba.fastjson2.JSON;
import com.bcsd.common.annotation.Anonymous;
import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.vo.SmsVO;
import com.bcsd.project.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    SmsService smsService;

    @PostMapping("sms/send")
    public AjaxResult sendSms(@RequestBody SmsVO params){
        if (smsService.sendSms(params)){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    @GetMapping("sms/send/status")
    public String getSmsSendStatus(){
        return JSON.toJSONString(smsService.getSmsSendStatus());
    }

}
