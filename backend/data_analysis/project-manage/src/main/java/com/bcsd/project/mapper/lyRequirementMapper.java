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

    lyRequirement selectByPrimaryKey(Long id);

    List<lyInventoryThreshold> checkCodeExists(String code);
    @Select("select * from ly_requirement where date = #{dayDate}")
    List<lyRequirement> getRequirementList(String dayDate);


}
