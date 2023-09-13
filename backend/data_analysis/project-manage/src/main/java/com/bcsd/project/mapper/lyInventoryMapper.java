package com.bcsd.project.mapper;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.vo.InventoryGapsNumberVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface lyInventoryMapper extends BaseMapper<lyInventory> {

    @Select("SELECT count(*) FROM ly_inventory WHERE batchAttr07 = '已装配'")
    Integer getAssembledCount();

    @Select("SELECT mapName,count(1) AS count FROM ly_inventory WHERE batchAttr07 = '已装配' AND mapName is not null AND mapName != ''  GROUP BY mapName ORDER BY mapName")
    List<Map<String,Object>> getMapNameCount();

    //@Select("SELECT matCode,matText,totalQty,availableQty,batchAttr07 FROM ly_inventory WHERE batchAttr07 = '已装配'")
    List<Map<String,Object>> getAssembledList(lyInventory params);

    @Select("SELECT batchAttr07,count(*) as batchAttr07Count FROM ly_inventory WHERE batchAttr07 is not null AND batchAttr07 != '' GROUP BY batchAttr07 ")
    List<Map<String,Object>> getBatchAttr07();

    Map<String,Object> getCount(lyInventory params);

    lyInventory selectByStkCode(lyInventory params);

    List<Map<String,Object>> inventoryAlarmCount(lyInventory params);

    List<InventoryGapsNumberVO> getGapsNumber(lyInventory params);

    int updProcessingStatus(JSONObject params);

    Map<String,Object> getFhjhljqkqkCount();

    List<Map<String,Object>> getFhjhljqkqkList();

    Map<String,Object> getKcyjCount();

    List<Map<String,Object>> getKcyjList();

    List<lyInventory> getByMatCode(String matCode);

}