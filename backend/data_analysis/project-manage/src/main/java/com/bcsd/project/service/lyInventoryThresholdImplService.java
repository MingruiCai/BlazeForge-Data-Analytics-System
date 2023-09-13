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
     * 零件号列表
     *
     * @param inventoryThreshold
     */
    @Override
    public List<lyInventoryThreshold> codeList(lyInventoryThreshold inventoryThreshold) {
        return inventoryThresholdMapper.selectCodeList(inventoryThreshold);
    }

    /**
     * 新增更新
     *
     * @param inventoryThreshold
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addOrUpdate(lyInventoryThreshold inventoryThreshold) {
        if (inventoryThreshold.getId() != null) {
            inventoryThreshold.setUpdateBy(getUsername());
            inventoryThreshold.setUpdateTime(new Date());
            inventoryThresholdMapper.updateByPrimaryKeySelective(inventoryThreshold);
            return AjaxResult.success("更新成功");
        } else {
            List<lyInventoryThreshold> result = inventoryThresholdMapper.checkCodeExists(inventoryThreshold.getCode());
            if (!result.isEmpty()) {
                return AjaxResult.error("零件号重复");
            } else {
                inventoryThreshold.setCreateBy(getUsername());
                inventoryThreshold.setCreateTime(new Date());
                inventoryThresholdMapper.insertSelective(inventoryThreshold);
                return AjaxResult.success("新增成功");
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

