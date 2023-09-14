package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyRequirement;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface lyRequirementMapper {

    List<lyRequirement> selectRequirementList(lyRequirement requirement);

    int insertSelective(lyRequirement record);

    int updateByPrimaryKeySelective(lyRequirement record);

    int deleteByPrimaryKey(Long id);

    List<lyRequirement> selectCodeList(lyRequirement requirement);

    List<lyRequirement> checkCodeExists(lyRequirement requirement);

    @Select("select * from ly_requirement where date = #{dayDate}")
    List<lyRequirement> getRequirementList(String dayDate);


}
