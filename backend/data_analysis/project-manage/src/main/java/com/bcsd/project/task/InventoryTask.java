package com.bcsd.project.task;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bcsd.common.utils.http.HttpUtilsNew;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.service.lyInventoryImplService;
import com.bcsd.project.service.lyRequirementImplService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @ClassName InventoryTask
 * @Description: TODO
 * @Author zhaofei
 * @Date 2023/9/6
 * @Version V1.0
 **/
@Slf4j
@Component
public class InventoryTask {
    @Autowired
    lyInventoryImplService inventoryImplService;
    @Value("${http.url}")
    private String url;
    @Autowired
    lyRequirementImplService requirementImplService;
    
    
    //半个小时执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    public void monthly15th(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", 1);
        jsonObject.put("pageSize", 100000);
        jsonObject.put("sourceId", "REST010101");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("whCode","2022");
        jsonObject.put("param", jsonObject1);
        log.error("库存请求参数：" + jsonObject.toString());
        try {
            String res = HttpUtilsNew.jsonPost(url, jsonObject.toString());
            //log.error("库存返回参数：" + res);
            JSONObject jsonObject2 = JSONObject.parseObject(res);
            log.error("库存返回个数：total："+jsonObject2.getString("total"));
            if (!jsonObject2.getString("code").equals("0")) {
                log.error("库存获取数据失败" + jsonObject2.getString("message"));
                return;
            }
            String data = jsonObject2.getString("data");
            List<lyInventory> entityList = JSON.parseArray(data, lyInventory.class);
            log.error("库存返回个数：size："+entityList.size());
            Set<String> matCodeSet = inventoryImplService.add(entityList);
            requirementImplService.updinventoryStatus(matCodeSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
