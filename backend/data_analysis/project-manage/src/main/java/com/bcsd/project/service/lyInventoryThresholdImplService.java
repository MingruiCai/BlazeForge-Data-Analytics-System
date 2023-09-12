package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.vo.ThirdSession;
import com.bcsd.project.mapper.lyInventoryThresholdMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bcsd.common.utils.SecurityUtils.*;

/**
 * 零件库存阈值实现类
 *
 * @ClassName lyInventoryThresholdServiceImpl
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/6
 **/
@Slf4j
@Service
public class lyInventoryThresholdImplService implements lyInventoryThresholdService {

    @Autowired
    private lyInventoryThresholdMapper inventoryThresholdMapper;

    /**
     * 列表
     *
     * @param inventoryThreshold
     */
    @Override
    public List<lyInventoryThreshold> list(lyInventoryThreshold inventoryThreshold) {

        return inventoryThresholdMapper.selectInventoryThresholdList(inventoryThreshold);

    }

    /**
     * 新增更新
     *
     * @param inventoryThreshold
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(lyInventoryThreshold inventoryThreshold) {
        List<String> codeList = inventoryThresholdMapper.getCodeList();
        boolean codeExists = false;
        for (String code : codeList) {
            if (code.equals(inventoryThreshold.getCode())) {
                codeExists = true;
                break;
            }
        }
        if (codeExists) {
            if (inventoryThreshold.getId() != null) {
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

    /**
     * 删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delete(Long id) {
        inventoryThresholdMapper.deleteByPrimaryKey(id);
        return AjaxResult.success();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return inventoryThreshold
     */
    @Override
    public lyInventoryThreshold selectByPrimaryKey(Long id) {
        return inventoryThresholdMapper.selectByPrimaryKey(id);
    }
}

