package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.lyEmptyContainer;
import com.bcsd.project.domain.lyInventory;
import com.bcsd.project.domain.vo.lyEmptyContainerVO;

import java.util.List;
import java.util.Map;

public interface lyEmptyContainerMapper extends BaseMapper<lyEmptyContainer> {

    List<Map<String,Object>> getContainerCount();

    List<lyEmptyContainerVO> getList(lyEmptyContainer param);

    Map<String,Object> getListCount(lyEmptyContainer params);

    lyEmptyContainer selectByPodCode(lyEmptyContainer param);
}