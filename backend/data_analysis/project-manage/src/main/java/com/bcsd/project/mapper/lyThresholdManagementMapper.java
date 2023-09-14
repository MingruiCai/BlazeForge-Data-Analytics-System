package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyThresholdManagement;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface lyThresholdManagementMapper {
    List<lyThresholdManagement> selectThresholdManagementList(lyThresholdManagement thresholdManagement);

    int updateByPrimaryKeySelective(lyThresholdManagement record);

    @Select("select * from ly_threshold_management where type = #{type} AND status = '1'")
    List<lyThresholdManagement> getListByType(String type);
}
