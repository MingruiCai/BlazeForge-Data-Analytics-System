package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface lyInventoryThresholdMapper {

    List<lyInventoryThreshold> selectInventoryThresholdList(lyInventoryThreshold inventoryThreshold);

    int insertSelective(lyInventoryThreshold record);

    int updateByPrimaryKeySelective(lyInventoryThreshold record);

    int deleteByPrimaryKey(Long id);

    List<lyInventoryThreshold> selectCodeList(lyInventoryThreshold inventoryThreshold);

    List<lyInventoryThreshold> checkCodeExists(String code);

    @Select("select * from ly_inventory_threshold")
    List<lyInventoryThreshold> getList();

    @Select("select * from ly_inventory_threshold where id = #{id}")
    lyInventoryThreshold selectById(Long id);

}
