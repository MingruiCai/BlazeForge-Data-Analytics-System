package com.bcsd.project.service;

import com.bcsd.common.core.domain.AjaxResult;
import com.bcsd.common.utils.DateUtils;
import com.bcsd.common.utils.poi.ExcelUtil;
import com.bcsd.project.domain.lyInventoryThreshold;
import com.bcsd.project.domain.lyInventoryThreshold;
//import com.bcsd.project.domain.vo.ThirdSession;
import com.bcsd.project.mapper.lyInventoryThresholdMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
    @Autowired
    private lyInventoryImplService inventoryImplService;

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

        } else {
            List<lyInventoryThreshold> result = inventoryThresholdMapper.checkCodeExists(inventoryThreshold.getCode());
            if (!result.isEmpty()) {
                return AjaxResult.error("零件号重复");
            } else {
                inventoryThreshold.setCreateBy(getUsername());
                inventoryThreshold.setCreateTime(new Date());
                inventoryThreshold.setUpdateBy(getUsername());
                inventoryThreshold.setUpdateTime(new Date());
                inventoryThresholdMapper.insertSelective(inventoryThreshold);
            }

        }
        inventoryImplService.updProcessingStatus(inventoryThreshold);
        return AjaxResult.success();
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
     * 模板下载
     *
     * @param response
     * @param request
     * @return
     */
    @Override
    public void excelDownload(HttpServletResponse response, HttpServletRequest request) {
        List<List<String>> dataList = new ArrayList<>();
        List<String> headList = new ArrayList<>();
//        headList.add("序号");
        headList.add("零件号");
        headList.add("零件颜色");
        headList.add("库存上限数量");
        headList.add("库存下限数量");
        ExcelUtil.uploadExcelAboutUser(request, response, "零件库存阈值设置导入模板", headList, dataList);
    }

    /**
     * 导入数据
     *
     * @param data
     * @param userName
     * @return
     */
    @Override
    public AjaxResult importData(List<lyInventoryThreshold> data, String userName) {
        for (lyInventoryThreshold inventoryThreshold : data) {
            if (!inventoryThreshold.getCode().isEmpty()) {
                List<lyInventoryThreshold> result = inventoryThresholdMapper.checkCodeExists(inventoryThreshold.getCode());
                if (!result.isEmpty()) {
                    inventoryThreshold.setId(result.get(0).getId());
                    inventoryThreshold.setUpdateBy(userName);
                    inventoryThreshold.setUpdateTime(DateUtils.getNowDate());
                    inventoryThresholdMapper.updateByPrimaryKeySelective(inventoryThreshold);
                } else {
                    inventoryThreshold.setCreateBy(userName);
                    inventoryThreshold.setCreateTime(DateUtils.getNowDate());
                    inventoryThresholdMapper.insertSelective(inventoryThreshold);
                }
            }
            inventoryImplService.updProcessingStatus(inventoryThreshold);
        }

        return AjaxResult.success();

    }

}

