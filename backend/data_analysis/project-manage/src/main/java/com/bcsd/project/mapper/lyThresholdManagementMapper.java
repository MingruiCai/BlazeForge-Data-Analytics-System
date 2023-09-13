package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyThresholdManagement;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface lyThresholdManagementMapper {

    List<lyThresholdManagement> selectThresholdManagementList(lyThresholdManagement thresholdManagement);

    int insertSelective(lyThresholdManagement record);

    int updateByPrimaryKeySelective(lyThresholdManagement record);

    int deleteByPrimaryKey(Long id);

    lyThresholdManagement selectByPrimaryKey(Long id);
    @Select("select code, COUNT(*) as count from ly_threshold_management group by code having COUNT(*) > 1 ")
    String checkCodeDuplicate(String code);
    @Select("select * from ly_threshold_management where type = #{type} AND status = '1'")
    List<lyThresholdManagement> getListByType(String type);
}
