package com.bcsd.project.service;

import com.bcsd.project.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    CommonMapper commonMapper;

    /**
     * 待办事项
     * @param userId
     * @return
     */
    public List<Map> notDone(Long userId){
        return commonMapper.selectDByUserId(userId);
    }

}
