package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface lyInventoryThresholdMapper {

    List<lyInventoryThreshold> selectInventoryThresholdList(lyInventoryThreshold inventoryThreshold);
    int insertSelective(lyInventoryThreshold record);
    int updateByPrimaryKeySelective(lyInventoryThreshold record);
    int deleteByPrimaryKey(Long id);

    lyInventoryThreshold selectByPrimaryKey(Long id);

    @Select("select * from ly_inventory_threshold")
    List<lyInventoryThreshold> getList();

}
