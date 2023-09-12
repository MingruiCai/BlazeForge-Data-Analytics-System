package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.project.domain.lyThresholdManagement;
import com.bcsd.project.mapper.lyThresholdManagementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.bcsd.common.utils.SecurityUtils.getUsername;

/**
 * 统计阈值管理实现类
 *
 * @ClassName lyThresholdManagementServiceImpl
 * @Description: TODO
 * @Author Mingrui
 * @Date 2023/9/11
 **/
@Slf4j
@Service
public class lyThresholdManagementImplService implements lyThresholdManagementService {

    @Autowired
    private lyThresholdManagementMapper thresholdManagementMapper;

    /**
     * 列表
     *
     * @param thresholdManagement
     */
    @Override
    public List<lyThresholdManagement> list(lyThresholdManagement thresholdManagement) {
        return thresholdManagementMapper.selectThresholdManagementList(thresholdManagement);
    }

    /**
     * 新增更新
     *
     * @param thresholdManagement
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdate(lyThresholdManagement thresholdManagement) {
        List<String> codeList = thresholdManagementMapper.getCodeList();
        boolean codeExists = false;
        for (String code : codeList) {
            if (code.equals(thresholdManagement.getCode())) {
                codeExists = true;
                break;
            }
        }
        if (codeExists) {
            if (thresholdManagement.getId() != null) {
                thresholdManagement.setUpdateBy(getUsername());
                thresholdManagement.setUpdateTime(new Date());
                thresholdManagementMapper.updateByPrimaryKeySelective(thresholdManagement);
            } else {
                thresholdManagement.setCreateBy(getUsername());
                thresholdManagement.setCreateTime(new Date());
                thresholdManagementMapper.insertSelective(thresholdManagement);
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
        thresholdManagementMapper.deleteByPrimaryKey(id);
        return AjaxResult.success();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    @Override
    public lyThresholdManagement selectByPrimaryKey(Long id) {
        return thresholdManagementMapper.selectByPrimaryKey(id);
    }
}

