package com.bcsd.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bcsd.project.domain.lyEmptyContainer;
import com.bcsd.project.domain.vo.lyEmptyContainerVO;

import java.util.List;

public interface lyEmptyContainerMapper extends BaseMapper<lyEmptyContainer> {

    List<lyEmptyContainerVO> getList(lyEmptyContainer param);

    lyEmptyContainer selectByPodCode(lyEmptyContainer param);
}