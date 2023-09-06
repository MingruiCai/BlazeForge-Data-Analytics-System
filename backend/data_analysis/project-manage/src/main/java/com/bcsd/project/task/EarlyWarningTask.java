package com.bcsd.project.task;

import com.bcsd.common.utils.DateUtils;
import com.bcsd.project.service.ProjectEarlyWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EarlyWarningTask {

    @Autowired
    ProjectEarlyWarningService earlyWarningService;

    @Scheduled(cron = "0 0 0 21 * ?")
    public void run(){
        String dataTime = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
        earlyWarningService.generateWarning(dataTime,"系统");
    }


}
