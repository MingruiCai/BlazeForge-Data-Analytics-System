package com.bcsd.project.task;

import com.bcsd.project.service.lyEmptyContainerImplService;
import com.bcsd.project.service.lyInventoryImplService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName InventoryTask
 * @Description: TODO
 * @Author zhaofei
 * @Date 2023/9/6
 * @Version V1.0
 **/
@Slf4j
@Component
public class EmptyContainerTask {
    @Autowired
    lyEmptyContainerImplService emptyContainerImplService;
    //半个小时执行一次
    @Scheduled(cron = "0 0/30 * * * ?")
    public void monthly15th(){
        emptyContainerImplService.add();
    }
}
