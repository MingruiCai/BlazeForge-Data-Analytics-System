package com.bcsd.project.mapper;

import com.bcsd.project.domain.lyThresholdManagement;

import java.util.List;

public interface lyThresholdManagementMapper {

    List<lyThresholdManagement> selectThresholdManagementList(lyThresholdManagement thresholdManagement);
    int insertSelective(lyThresholdManagement record);
    int updateByPrimaryKeySelective(lyThresholdManagement record);
    int deleteByPrimaryKey(Long id);

    lyThresholdManagement selectByPrimaryKey(Long id);



}
