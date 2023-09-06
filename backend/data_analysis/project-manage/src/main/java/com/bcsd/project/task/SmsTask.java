package com.bcsd.project.task;

import com.bcsd.project.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsTask {

    @Autowired
    SmsService smsService;

    @Scheduled(cron = "0 0 10 15 * ?")
    public void monthly15th(){
        smsService.sendSmsTask();
    }


}
