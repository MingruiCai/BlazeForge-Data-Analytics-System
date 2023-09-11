package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyRequirement;

import java.util.List;

/**
 * @ClassName InventoryThresholdMapper
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 * @Version V1.0
 **/
public interface lyRequirementMapper {

    List<lyRequirement> selectInventoryThresholdList(lyRequirement requirement);
    int insertSelective(lyRequirement record);
    int updateByPrimaryKeySelective(lyRequirement record);
    int deleteByPrimaryKey(Long id);

    lyRequirement selectByPrimaryKey(Long id);



}
