package com.bcsd.project.service;

import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.vo.ThirdSession;
import com.bcsd.project.mapper.lyInventoryThresholdMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.bcsd.common.utils.SecurityUtils.*;

/**
 * 用药咨询
 * @ClassName InventoryThresholdServiceImpl
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 **/
@Slf4j
@Service
public class lyInventoryThresholdImplService implements lyInventoryThresholdService {

    @Autowired
    private lyInventoryThresholdMapper inventoryThresholdMapper;
    @Override
    public List<lyInventoryThreshold> list(lyInventoryThreshold lyinventoryThreshold) {

        return inventoryThresholdMapper.selectInventoryThresholdList(lyinventoryThreshold);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(lyInventoryThreshold inventoryThreshold) {
        if (inventoryThreshold.getId()!=null) {
            inventoryThreshold.setUpdateBy(getUsername());
            inventoryThreshold.setUpdateTime(new Date());
            inventoryThresholdMapper.updateByPrimaryKeySelective(inventoryThreshold);
        } else {
            inventoryThreshold.setCreateBy(getUsername());
            inventoryThreshold.setCreateTime(new Date());
            inventoryThresholdMapper.insertSelective(inventoryThreshold);
        }
    }

}

