package com.bcsd.project.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonMapper{

    /**
     * 查询待办事项
     * @param userId
     * @return
     */
    List<Map> selectDByUserId(Long userId);

}
