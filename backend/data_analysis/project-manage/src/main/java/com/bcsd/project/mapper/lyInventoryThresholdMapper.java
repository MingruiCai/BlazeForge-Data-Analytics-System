package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;

import java.util.List;

/**
 * @ClassName InventoryThresholdMapper
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
public interface lyInventoryThresholdMapper {

    List<lyInventoryThreshold> selectInventoryThresholdList(lyInventoryThreshold inventoryThreshold);
    int insertSelective(lyInventoryThreshold record);
    int updateByPrimaryKeySelective(lyInventoryThreshold record);
}
